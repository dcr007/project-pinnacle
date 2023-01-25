package com.ondemand.pinnacle.ingestion.analyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ondemand.pinnacle.ingestion.analyzer.models.enums.StackCategory;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 12/12/2022 - 9:50 AM
 * @description
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class DwrRcaSummary
//        implements
//        RcaSummary<PerfLogAnalyzerService,CallStack,
//                Map<StackCategory, ArrayList<StackClassification>>,DwrRcaSummary>
{

    Map<StackCategory, ArrayList<StackClassification>> callStackBreakDown;

    /*@Override
    public DwrRcaSummary generateRca(PerfLogAnalyzerService analyzePerfLogService, CallStack stack,
                                     Map<StackCategory, ArrayList<StackClassification>> callStackMap) {

        Map<StackCategory, ArrayList<StackClassification>> rca =
                analyzePerfLogService.analyzeCallStack(stack, callStackMap);

        return new DwrRcaSummary(rca);
    }*/

}
