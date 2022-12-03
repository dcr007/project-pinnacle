package com.ondemand.pinnacle.ingestion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.pinnacle.ingestion.kafka.models.CallStack;
import com.ondemand.pinnacle.ingestion.kafka.models.JsonPayLoad;
import com.ondemand.pinnacle.ingestion.kafka.models.SplunkPayLoad;
import com.ondemand.pinnacle.ingestion.repository.CallStackRepository;
import com.ondemand.pinnacle.ingestion.repository.JsonPayLoadRepository;
import com.ondemand.pinnacle.ingestion.repository.SplunkPayLoadRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:13 PM
 * @description
 */
@Data
@Slf4j
@Service("callStackService")
public class CallStackServiceImpl implements CallStackService {

    @Autowired
    private CallStackRepository callStackRepository ;

    @Autowired
    private SplunkPayLoadRepository splunkPayLoadRepository;

    @Autowired
    private JsonPayLoadRepository rawDataRepository;

    @Override
    public String save(CallStack stack) {
        return callStackRepository.save(stack).getId();
    }

    @Override
    @Transactional
    public HttpStatus save(JsonNode stack) {
        JsonPayLoad rawEntity = new JsonPayLoad();
        rawEntity.setPayload(stack);
        rawEntity.setId(LocalDateTime.now().toString());
       try{
           if(rawDataRepository.save(rawEntity)!=null){
               log.info("Payload saved: \n{}", stack.toPrettyString());
               return HttpStatus.OK;
           }else return HttpStatus.ALREADY_REPORTED;
       }catch (DataAccessException e){
           log.error("Unable to save stackId: {}\t Exception thrown: \n {}",rawEntity.getId(),e.getLocalizedMessage());
           return HttpStatus.BAD_REQUEST;
       }catch (RuntimeException e){
           log.error("Runtime exception occurred while saving stackId: {}\t " +
                   "Exception thrown: \n {}",rawEntity.getId(),e.getLocalizedMessage());
           return HttpStatus.INTERNAL_SERVER_ERROR;
       }
    }

    @Override
    @Transactional
    public HttpStatus save(SplunkPayLoad splunkPayLoad) {
//        JsonPayLoad rawEntity = new JsonPayLoad();
//        rawEntity.setPayload(stack);
//        rawEntity.setId(LocalDateTime.now().toString());
        try{
            if(splunkPayLoadRepository.save(splunkPayLoad)!=null){
                log.info("PayloadId saved: \n{}", splunkPayLoad);
                return HttpStatus.OK;
            }else return HttpStatus.ALREADY_REPORTED;
        }catch (DataAccessException e){
            log.error("Unable to save stackId: {}\t Exception thrown: \n {}",splunkPayLoad.getSid(),e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }catch (RuntimeException e){
            log.error("Runtime exception occurred while saving stackId: {}\t " +
                    "Exception thrown: \n {}",splunkPayLoad.getSid(),e.getLocalizedMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public CallStack getCallStackById(String id) {
        return callStackRepository.findById(id).get();
    }
}
