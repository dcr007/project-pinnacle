package com.ondemand.pinnacle.ingestion.kafka.consumer;

import com.ondemand.pinnacle.ingestion.kafka.models.*;
import com.ondemand.pinnacle.ingestion.repository.CallStackRepository;
import com.ondemand.pinnacle.ingestion.repository.SplunkPayLoadRepository;
import com.ondemand.pinnacle.ingestion.services.NextSequenceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 2:13 PM
 * @description
 */
@Service
@AllArgsConstructor
@Slf4j
public class CallStackConsumerService {

    @Autowired
    private CallStackRepository callStackRepository ;
    @Autowired
    ConversionService conversionService;
    @Autowired
    NextSequenceService nextSequenceIdGeneratorService;

    @Autowired
    private SplunkPayLoadRepository splunkPayLoadRepository;

    @KafkaListener(topics = "topic-callStack-call", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final @Payload CallStack callStack,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, callStack, offset, key, partition, topic));

        log.info("Persisting message : {}",callStack);
        callStackRepository.insert(callStack);
    }

    @KafkaListener(topics = "topic-dwr-call", containerFactory = "kafkaSplunkPayLoadListenerContainerFactory")
    public void consume(final @Payload SplunkPayLoad splunkPayLoad,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("\n#### -> Consumed message -> \n TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, splunkPayLoad, offset, key, partition, topic));
        splunkPayLoad.setPayLoadId(String.valueOf(nextSequenceIdGeneratorService.getNextSequence("payLoadId-topic:"+topic+"-")));
        splunkPayLoad.setTime(String.valueOf(ts));

        SplunkResult result = splunkPayLoad.getResult();
        result.setResultId(String.valueOf(nextSequenceIdGeneratorService.getNextSequence("resultId-topic:"+topic+"-")));
        List<PerfLog> perfLogList = new ArrayList<>();
        PerfLog rawData;

//                Trigger the converter here :
        if(result.getRaw()!=null){
            rawData= conversionService.convert(result.getRaw(), PerfLog.class);
            perfLogList.add(rawData);
            result.setPerfLog(perfLogList);
        }
        if(result.get_raw()!=null){
            rawData= conversionService.convert(result.get_raw(), PerfLog.class);
            perfLogList.add(rawData);
            result.setPerfLog(perfLogList);
        }

        log.info("===============================");
        log.info("Converting PerfLog  \n{}",splunkPayLoad.getResult().getPerfLog());
        log.info("===============================");

        log.info("Persisting message id: {}",splunkPayLoad.getSid());

//        log.info("Raw data is : {} ",splunkPayLoad.getResult().getPerfLog());
        splunkPayLoadRepository.insert(splunkPayLoad);
    }

}
