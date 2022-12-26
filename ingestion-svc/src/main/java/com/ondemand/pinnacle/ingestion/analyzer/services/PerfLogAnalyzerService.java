package com.ondemand.pinnacle.ingestion.analyzer.services;

import com.ondemand.pinnacle.ingestion.analyzer.models.DwrRcaSummary;
import com.ondemand.pinnacle.ingestion.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.ingestion.analyzer.models.StackClassification;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:01 PM
 * @description
 */
public interface PerfLogAnalyzerService<PerfLog,OP1, OP2>  {
    OP2 generateReport(PerfLog perfLog);

    DwrRcaSummary getRcaSummary(com.ondemand.pinnacle.ingestion.models.PerfLog perfLog);

    Map<StackCategory, ArrayList<StackClassification>> getCallStackAnalysis(com.ondemand.pinnacle.ingestion.models.PerfLog perfLog);

    //    OP1 getPerfLogSummary(PerfLog perfLog);
    Map<StackCategory, ArrayList<StackClassification>> analyzeCallStack(CallStack stack, Map<StackCategory,ArrayList<StackClassification>> classStackMap);

    OP1 getLogSummary(PerfLog perfLog);
}
