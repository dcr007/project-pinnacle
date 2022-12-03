package com.ondemand.pinnacle.ingestion.services;

import com.ondemand.pinnacle.ingestion.kafka.models.CallStack;
import com.ondemand.pinnacle.ingestion.kafka.models.SegregatedStack;
import com.ondemand.pinnacle.ingestion.kafka.models.enums.CallCategory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Service("segregateStackService")
public class SegregateStackServiceImpl implements SegregateStackService {


    public static List<CallStack> getSqlStack() {

        return null;
    }

    public static List<CallStack> getServiceStack() {

        return null;
    }

    public Map<CallCategory, ArrayList<SegregatedStack>> segregatedStack(CallStack stack, Map<CallCategory,
            ArrayList<SegregatedStack>> classStackMap) {

        if (stack.getN().contains(".dwr")) {
            log.info("call :{}", stack.getN());

            SegregatedStack dwrStack = new SegregatedStack(stack.getN(), CallCategory.DWR, true,
                    "callId-000922", stack.getI(), stack.getT(), stack.getM());
            List<SegregatedStack> segregatedStackList;

            if (classStackMap.get(CallCategory.DWR) != null) {
                segregatedStackList = (classStackMap.get(CallCategory.SERVICE));
                segregatedStackList.add(dwrStack);
            } else segregatedStackList = List.of(dwrStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(dwrStack.getCallCategory(), (ArrayList<SegregatedStack>) segregatedStackList);
        }


        if (stack.getN().contains(".service.")) {
            log.info("parsing call :{}", stack.getN());

            SegregatedStack segregatedServiceStack = new SegregatedStack(stack.getN(), CallCategory.SERVICE
                    , true, "callId-000921", stack.getI(), stack.getT(), stack.getM());
            List<SegregatedStack> segregatedStackList;

            if (classStackMap.get(CallCategory.SERVICE) != null) {
                segregatedStackList = (classStackMap.get(CallCategory.SERVICE));
                segregatedStackList.add(segregatedServiceStack);
            } else
                segregatedStackList = List.of(segregatedServiceStack)
                        .stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedServiceStack.getCallCategory(), (ArrayList<SegregatedStack>) segregatedStackList);
        }

        if (stack.getN().contains("SQL:")) {
            log.info("parsing call :{}", stack.getN());
            SegregatedStack segregatedJdbcStack = new SegregatedStack(stack.getN(), CallCategory.JDBC, true, "callId-000926", stack.getI(), stack.getT(), stack.getM());

            List<SegregatedStack> segregatedStackList;

            if (classStackMap.get(CallCategory.JDBC) != null) {
                segregatedStackList = (classStackMap.get(CallCategory.JDBC));
                segregatedStackList.add(segregatedJdbcStack);
            } else
                segregatedStackList = List.of(segregatedJdbcStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedJdbcStack.getCallCategory(), (ArrayList<SegregatedStack>) segregatedStackList);

        }

        if (stack.getN().contains(".cachelegacy.impl.")) {
            log.info("parsing call :{}", stack.getN());
            SegregatedStack segregatedCacheStack = new SegregatedStack(stack.getN(), CallCategory.CACHE, true, "callId-000922", stack.getI(), stack.getT(), stack.getM());
            List<SegregatedStack> segregatedStackList;

            if (classStackMap.get(CallCategory.CACHE) != null) {
                segregatedStackList = (classStackMap.get(CallCategory.CACHE));
                segregatedStackList.add(segregatedCacheStack);

            } else
                segregatedStackList = List.of(segregatedCacheStack).stream().collect(Collectors.toCollection(ArrayList::new));

            classStackMap.put(segregatedCacheStack.getCallCategory(), (ArrayList<SegregatedStack>) segregatedStackList);

        }

        List<CallStack> callStacks = stack.getSub();
        if (callStacks != null) {
            for (CallStack stk : callStacks) {
                log.info("executing call stack {}", stk.toString());
                segregatedStack(stk, classStackMap);
            }
        }

        return classStackMap;

    }


}
