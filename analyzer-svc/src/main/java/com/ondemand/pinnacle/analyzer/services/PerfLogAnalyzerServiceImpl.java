package com.ondemand.pinnacle.analyzer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.analyzer.models.DwrAnalysisResult;
import com.ondemand.pinnacle.analyzer.models.DwrPerfLogSummary;
import com.ondemand.pinnacle.analyzer.models.DwrRcaSummary;
import com.ondemand.pinnacle.analyzer.models.StackClassification;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.analyzer.models.ingestion.CallStackModel;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
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
        PerfLogAnalyzerService<PerfLogModel, DwrPerfLogSummary, DwrAnalysisResult> {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private PerfLogModel perfLog;


    PerfLogAnalyzerServiceImpl(@Autowired(required = false) PerfLogModel perfLogModel){
        this.perfLog = perfLogModel;
    }
    @Override
    public DwrAnalysisResult generateReport( PerfLogModel perfLog) {

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
    public DwrPerfLogSummary getLogSummary(PerfLogModel perfLog) {
        return DwrPerfLogSummary.builder().build().generateSummary(perfLog);
    }

    @Override
    public DwrRcaSummary getRcaSummary(PerfLogModel perfLog) {
        Map<StackCategory, ArrayList<StackClassification>> analyzedCallStack = getCallStackAnalysis(perfLog);
        return DwrRcaSummary.builder().callStackBreakDown(analyzedCallStack).build();
    }

    @Override
    public Map<StackCategory, ArrayList<StackClassification>> getCallStackAnalysis(PerfLogModel perfLog) {

        return analyzeCallStack(perfLog.getCallStack(), new HashMap<>());
    }

    @Override
    public Map<StackCategory, ArrayList<StackClassification>> analyzeCallStack(CallStackModel stack, Map<StackCategory,
            ArrayList<StackClassification>> classStackMap) {

        if (stack.getCallNode().contains(".dwr")) {
            log.info("parsing call node :{}", stack.getCallNode());


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
                stackClassificationList = List.of(segregatedJdbcStack).stream()
                        .collect(Collectors.toCollection(ArrayList::new));

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

        if (stack.getCallNode().contains(".dao.impl.")) {
            log.info("parsing call :{}", stack.getCallNode());
            StackClassification segregatedDaoStack = buildClassifiedStack(stack, StackCategory.DAO);
            List<StackClassification> stackClassificationList;

            if (classStackMap.get(StackCategory.DAO) != null) {
                stackClassificationList = (classStackMap.get(StackCategory.DAO));
                stackClassificationList.add(segregatedDaoStack);

            } else
                stackClassificationList = List.of(segregatedDaoStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedDaoStack.getStackCategory(), (ArrayList<StackClassification>) stackClassificationList);

        }

        List<CallStackModel> callStacks = stack.getSub();
        if (callStacks != null) {
            for (CallStackModel stk : callStacks) {
                log.debug("executing call stack for callNode :{}", stk.getCallNode());
                analyzeCallStack(stk, classStackMap);
            }
        }

        return classStackMap;

    }
    private StackClassification buildClassifiedStack(CallStackModel stack, StackCategory service) {
        return new StackClassification()
                .toBuilder()
                .perfLogId(this.perfLog.getPerfLogId())
                .qualifierName(stack.getCallNode())
                .stackCategory(service)
                .isMetricValidated(false)
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

