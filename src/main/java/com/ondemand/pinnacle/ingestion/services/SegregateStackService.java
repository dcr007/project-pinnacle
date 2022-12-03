package com.ondemand.pinnacle.ingestion.services;

import com.ondemand.pinnacle.ingestion.kafka.models.CallStack;
import com.ondemand.pinnacle.ingestion.kafka.models.SegregatedStack;
import com.ondemand.pinnacle.ingestion.kafka.models.enums.CallCategory;

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
