package com.ondemand.tools.perflog.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.models.JsonPayLoad;
import com.ondemand.tools.perflog.models.RawData;
import com.ondemand.tools.perflog.models.SplunkPayLoad;
import com.ondemand.tools.perflog.repository.CallStackRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
