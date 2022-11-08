package com.ondemand.tools.perflog.services;

import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.repository.CallStackRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:13 PM
 * @description
 */
@Data
@Service("callStackService")
public class CallStackServiceImpl implements CallStackService {

    @Autowired
    private CallStackRepository callStackRepository ;

    @Override
    public String save(CallStack stack) {
        return callStackRepository.save(stack).getId();
    }

    @Override
    public CallStack getCallStackById(String id) {
        return callStackRepository.findById(id).get();
    }
}
