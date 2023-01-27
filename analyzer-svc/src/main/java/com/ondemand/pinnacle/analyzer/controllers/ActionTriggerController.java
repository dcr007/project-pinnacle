package com.ondemand.pinnacle.analyzer.controllers;

import com.ondemand.pinnacle.analyzer.api.constants.RequestTriggerType;
import com.ondemand.pinnacle.analyzer.services.ActionTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chandu D - i861116
 * @created 05/01/2023 - 2:33 PM
 * @description
 */
@Slf4j
@RestController
@RequestMapping(
        value = "/rest/v1"
//        consumes = MediaType.APPLICATION_JSON_VALUE,
//        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ActionTriggerController {
    @Autowired
    private ActionTriggerService actionTriggerService;

    @GetMapping("/trigger/{type}")
    public ResponseEntity<?> triggerRefresh(
            @PathVariable("type") RequestTriggerType type
    ){
        log.info("Controller request for trigger refresh with status {}",type);
        HttpStatus resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (type){
            case INITIALIZE:  {
                resStatus=HttpStatus.OK;
                return  ResponseEntity.of(this.actionTriggerService.triggerQueued(false));
            }
            case ANALYZE:  {
                resStatus=HttpStatus.OK;
                return  ResponseEntity.of(this.actionTriggerService.triggerAnalyze(false));
            }
            case VALIDATE:
                resStatus=HttpStatus.OK;
                return  ResponseEntity.of(this.actionTriggerService.triggerAnomalyDetection());
            case ALL:
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

    }

}
