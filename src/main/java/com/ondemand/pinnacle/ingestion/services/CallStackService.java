package com.ondemand.pinnacle.ingestion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.ingestion.models.SplunkPayLoad;
import org.springframework.http.HttpStatus;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:15 PM
 * @description
 */

public interface CallStackService {

    String save(CallStack stack);
    HttpStatus save(JsonNode stack);
    HttpStatus save(SplunkPayLoad stack);


    CallStack getCallStackById(String id);
}
