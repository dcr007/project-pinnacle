package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.analyzer.models.SegregatedStack;
import com.ondemand.pinnacle.analyzer.models.enums.CallCategory;
import com.ondemand.pinnacle.ingestion.models.PerfLog;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:01 PM
 * @description
 */
public interface PerfLogAnalyzerService {
    Map<?,?>analyzePerfLog(PerfLog perfLog);
    Map<CallCategory, ArrayList<SegregatedStack>> analyzeCallStack(CallStack stack, Map<CallCategory,ArrayList<SegregatedStack>> classStackMap);

}
