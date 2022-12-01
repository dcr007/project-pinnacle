package com.ondemand.tools.perflog.services;

import com.ondemand.tools.perflog.kafka.models.CallStack;
import com.ondemand.tools.perflog.kafka.models.SegregatedStack;
import com.ondemand.tools.perflog.kafka.models.enums.CallCategory;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:01 PM
 * @description
 */
public interface SegregateStackService {
    Map<CallCategory, ArrayList<SegregatedStack>> segregatedStack(CallStack stack, Map<CallCategory,ArrayList<SegregatedStack>> classStackMap);

}
