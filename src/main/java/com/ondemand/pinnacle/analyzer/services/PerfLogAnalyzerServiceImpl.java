package com.ondemand.pinnacle.analyzer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.analyzer.models.DwrAnalysisResult;
import com.ondemand.pinnacle.analyzer.models.DwrPerfLogSummary;
import com.ondemand.pinnacle.analyzer.models.DwrRcaSummary;
import com.ondemand.pinnacle.analyzer.models.StackClassification;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 1:44 PM
 * @description This service will classify stack calls into diffrent layers based on the call statck.
 */
@Data
@Slf4j
@Service("analyzePerfLogServiceImpl")
public class PerfLogAnalyzerServiceImpl implements
        PerfLogAnalyzerService<PerfLog, DwrPerfLogSummary, DwrAnalysisResult> {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DwrAnalysisResult generateReport(PerfLog perfLog) {

        log.info("Analysing perflog {}", perfLog.toString());

        DwrPerfLogSummary dwrPerfLogSummary = getLogSummary(perfLog);

//        Map<StackCategory, ArrayList<StackClassification>> callStackAnalysis =
//               analyzeCallStack(perfLog.getCallStack(),new HashMap<>());

        DwrRcaSummary rcaSummary = getRcaSummary(perfLog);
        log.info("Call stack analyzed successfully \n {}", rcaSummary);

        DwrAnalysisResult dwrAnalysisResultBuild;

        dwrAnalysisResultBuild = DwrAnalysisResult.builder()
                .dwrPerfLogSummary(dwrPerfLogSummary)
                .dwrRcaSummary(rcaSummary)
                .build();

        log.info("DwrAnalysisResult successfully generated \n{}", dwrAnalysisResultBuild);

        return dwrAnalysisResultBuild;
    }

    @Override
    public DwrPerfLogSummary getLogSummary(PerfLog perfLog) {
        return DwrPerfLogSummary.builder().build().generateSummary(perfLog);
    }

    @Override
    public DwrRcaSummary getRcaSummary(PerfLog perfLog) {
        Map<StackCategory, ArrayList<StackClassification>> analyzedCallStack = getCallStackAnalysis(perfLog);
        return DwrRcaSummary.builder().callStackBreakDown(analyzedCallStack).build();
    }

    @Override
    public Map<StackCategory, ArrayList<StackClassification>> getCallStackAnalysis(PerfLog perfLog) {

        return analyzeCallStack(perfLog.getCallStack(), new HashMap<>());
    }

    @Override
    public Map<StackCategory, ArrayList<StackClassification>> analyzeCallStack(CallStack stack, Map<StackCategory,
            ArrayList<StackClassification>> classStackMap) {

        if (stack.getCallNode().contains(".dwr")) {
            log.info("Call node :{}", stack.getCallNode());


            StackClassification dwrStack = buildClassifiedStack(stack, StackCategory.DWR);

            List<StackClassification> stackClassificationList;

            if (classStackMap.get(StackCategory.DWR) != null) {
                stackClassificationList = (classStackMap.get(StackCategory.SERVICE));
                stackClassificationList.add(dwrStack);
            } else
                stackClassificationList = List.of(dwrStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(dwrStack.getStackCategory(), (ArrayList<StackClassification>) stackClassificationList);
        }

        if (stack.getCallNode().contains(".service.")) {
            log.info("parsing call :{}", stack.getCallNode());


            StackClassification segregatedServiceStack = buildClassifiedStack(stack, StackCategory.SERVICE);

            List<StackClassification> stackClassificationList;

            if (classStackMap.get(StackCategory.SERVICE) != null) {
                stackClassificationList = (classStackMap.get(StackCategory.SERVICE));
                stackClassificationList.add(segregatedServiceStack);
            } else
                stackClassificationList = List.of(segregatedServiceStack)
                        .stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedServiceStack.getStackCategory(), (ArrayList<StackClassification>) stackClassificationList);
        }

        if (stack.getCallNode().contains("SQL:")) {
            log.info("parsing call :{}", stack.getCallNode());

            StackClassification segregatedJdbcStack = buildClassifiedStack(stack, StackCategory.JDBC);

            List<StackClassification> stackClassificationList;

            if (classStackMap.get(StackCategory.JDBC) != null) {
                stackClassificationList = (classStackMap.get(StackCategory.JDBC));
                stackClassificationList.add(segregatedJdbcStack);
            } else
                stackClassificationList = List.of(segregatedJdbcStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedJdbcStack.getStackCategory(), (ArrayList<StackClassification>) stackClassificationList);

        }

        if (stack.getCallNode().contains(".cachelegacy.impl.")) {
            log.info("parsing call :{}", stack.getCallNode());
            StackClassification segregatedCacheStack = buildClassifiedStack(stack, StackCategory.CACHE);
            List<StackClassification> stackClassificationList;

            if (classStackMap.get(StackCategory.CACHE) != null) {
                stackClassificationList = (classStackMap.get(StackCategory.CACHE));
                stackClassificationList.add(segregatedCacheStack);

            } else
                stackClassificationList = List.of(segregatedCacheStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedCacheStack.getStackCategory(), (ArrayList<StackClassification>) stackClassificationList);

        }

        List<CallStack> callStacks = stack.getSub();
        if (callStacks != null) {
            for (CallStack stk : callStacks) {
                log.info("executing call stack {}", stk.toString());
                analyzeCallStack(stk, classStackMap);
            }
        }

        return classStackMap;

    }

    private StackClassification buildClassifiedStack(CallStack stack, StackCategory service) {
        return new StackClassification()
                .toBuilder()
                .qualifierName(stack.getCallNode())
                .stackCategory(service)
                .callId(stack.getCallStackId())
                .invokedCount(stack.getInvokedCount())
                .totalInvokeTimeInMs(stack.getTotalInvokeTimeInMs())
                .executionTimeInMs(stack.getExecutionTimeInMs())
                .distinctSqlQueryCount(stack.getDistinctSqlQueryCount())
                .totalSqlUpdates(stack.getTotalSqlUpdates())
                .memoryUsedByThreadInKb(stack.getMemoryUsedByThreadInKb())
                .networkReadInKb(stack.getNetworkReadInKb())
                .accumulatedTimeInMs(stack.getAccumulatedTimeInMs())
                .resultSetCount(stack.getResultSetCount())
                .fetchSize(stack.getFetchSize())
                .build();
    }


}

