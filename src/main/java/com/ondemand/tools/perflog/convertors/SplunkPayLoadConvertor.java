package com.ondemand.tools.perflog.convertors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.tools.perflog.kafka.producer.ExecutionResponse;
import com.ondemand.tools.perflog.kafka.models.CallStack;
import com.ondemand.tools.perflog.kafka.models.PerfLog;
import com.ondemand.tools.perflog.kafka.models.SplunkPayLoad;
import com.ondemand.tools.perflog.kafka.models.SplunkResult;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author Chandu D - i861116
 * @created 29/11/2022 - 2:31 PM
 * @description
 */
@Slf4j
@Component
public class SplunkPayLoadConvertor implements Converter<SplunkPayLoad, SplunkPayLoad> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static PerfLog perfLogObj;
    static CallStack callStack;
    @Autowired
    ConversionService conversionService;

    @Override
    public SplunkPayLoad convert(@NotNull SplunkPayLoad splunkPayLoad) {
        String topicName = "topic-dwr-call";
        String messageId = topicName+"-".concat(((SplunkPayLoad) splunkPayLoad).getSid())+
                "-"+(Instant.now().toEpochMilli());
        ExecutionResponse eresp = new ExecutionResponse();
        splunkPayLoad.setSid(messageId);
        SplunkResult result = splunkPayLoad.getResult();

        return splunkPayLoad;
    }

}