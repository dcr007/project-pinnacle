package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.analyzer.models.DwrRcaSummary;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.analyzer.models.StackClassification;
import com.ondemand.pinnacle.analyzer.models.ingestion.CallStackModel;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 9:01 PM
 * @description
 */
public interface PerfLogAnalyzerService<PerfLogModel,OP1, OP2>  {
    OP2 generateReport(PerfLogModel perfLog);

    DwrRcaSummary getRcaSummary(PerfLogModel perfLog);

    Map<StackCategory, ArrayList<StackClassification>> getCallStackAnalysis(PerfLogModel perfLog);

    //    OP1 getPerfLogSummary(PerfLog perfLog);
    Map<StackCategory, ArrayList<StackClassification>> analyzeCallStack(CallStackModel stack, Map<StackCategory,ArrayList<StackClassification>> classStackMap);

    OP1 getLogSummary(PerfLogModel perfLog);
}
