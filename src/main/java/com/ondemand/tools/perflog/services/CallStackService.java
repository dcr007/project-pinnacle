package com.ondemand.tools.perflog.services;

import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.repository.CallStackRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:15 PM
 * @description
 */

public interface CallStackService {

    String save(CallStack stack);

    CallStack getCallStackById(String id);
}
