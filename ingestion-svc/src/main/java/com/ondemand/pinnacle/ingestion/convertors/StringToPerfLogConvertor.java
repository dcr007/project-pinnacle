package com.ondemand.pinnacle.ingestion.convertors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import com.ondemand.pinnacle.ingestion.services.NextSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chandu D - i861116
 * @created 21/11/2022 - 3:08 PM
 * @description
 */
@Slf4j
@Component
public class StringToPerfLogConvertor implements Converter<String, PerfLog> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private NextSequenceService nextSequenceIdGeneratorService;

    static PerfLog perfLogObj;
    static CallStack callStack;

    @Override
    public PerfLog convert(@NotNull String pLog) {
        Map<String,String> perfLogMap = logToMapConvertor(pLog);
        log.info("Converting Log  :{} ",pLog);
        String stackStr="";
        if (perfLogMap.containsKey("stk")) {
            try {
                stackStr = perfLogMap.get("stk");
                // log.info("value of stackStr is \n{}", stackStr);
                //removes any extra brackets in the json string
                stackStr = stackStr.replace("\"\"", "\"").trim();
                callStack = objectMapper.readValue(stackStr,new TypeReference<CallStack>(){});
                callStack = callStack.toBuilder()
                        .callStackId(String.valueOf(nextSequenceIdGeneratorService
                                .getNextSequence("callStackId"))).build();
                log.info("CallStack Id is "+callStack.getCallStackId());
            } catch (JsonProcessingException ex) {
                log.error("Encountered invalid json string for key:STK (:-----((  \n {}"
                            , pLog.substring(pLog.indexOf("STK=")));
                try{
                    log.info("\nattempting to apply a fix,  the invalid json string:...\n");
                    String StkString = pLog.substring(pLog.indexOf("STK="));
                    int begin = StkString.indexOf("{", StkString.indexOf("STK=") );
                    int end1= StkString.length();
                    StkString = StkString.substring(begin, end1);
                    StkString= StkString.concat("\"}]}]}]}");
                    callStack = objectMapper.readValue(StkString,new TypeReference<CallStack>(){});
                    log.info("Fixing json string - PASS");
                } catch(JsonProcessingException e){
                    log.error("Fixing json string - FAIL");
                    log.error("Error occurred while processing CallStack json,exception details : \n {}"
                            , e.getMessage());

                }
            }
        }
        perfLogObj = perfLogMap.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(perfMap -> PerfLog.builder()
                        .perfLogId(String.valueOf(nextSequenceIdGeneratorService.getNextSequence("perfLogId")))
                        .timeStamp(perfLogMap.getOrDefault("timeStamp","no entry found: timeStamp"))
                        .dataCenter(perfLogMap.getOrDefault("dc","no entry found: dc"))
                        .perfLevel(perfLogMap.getOrDefault("plv","no entry found: plv"))
                        .clientIp(perfLogMap.getOrDefault("cip","no entry found: cip"))
                        .companyId(perfLogMap.getOrDefault("cmid","no entry found: cmid"))
                        .companyName(perfLogMap.getOrDefault("cmn","no entry found:cmn"))
                        .schemaName(perfLogMap.getOrDefault("sn","no entry found:sn"))
                        .dbPoolName(perfLogMap.getOrDefault("dpn","no entry found:dpn"))
                        .internalUserId(perfLogMap.getOrDefault("iuid","no entry found:iuid"))
                        .eventId(perfLogMap.getOrDefault("eid","no entry found:eid"))
                        .agent(perfLogMap.getOrDefault("agn","no entry found:agn"))
                        .requestId(perfLogMap.getOrDefault("rid","no entry found:rid"))
                        .requestMethod(perfLogMap.getOrDefault("mtd","no entry found:mtd"))
                        .url(perfLogMap.getOrDefault("url","no entry found:url"))
                        .requestExecutionTimeInMs(perfLogMap.getOrDefault("rqt","no entry found:rqt"))
                        .moduleId(perfLogMap.getOrDefault("mid","no entry found:mid"))
                        .pageId(perfLogMap.getOrDefault("pid","no entry found:pid"))
                        .pageQualifier(perfLogMap.getOrDefault("pq","no entry found:pq"))
                        .sub(perfLogMap.getOrDefault("sub","no entry found:sub"))
                        .totalMemory(perfLogMap.getOrDefault("mem","no entry found:mem"))
                        .totalCpu(perfLogMap.getOrDefault("cpu","no entry found:cpu"))
                        .userCpu(perfLogMap.getOrDefault("ucpu","no entry found:ucpu"))
                        .systemCpu(perfLogMap.getOrDefault("scpu","no entry found:scpu"))
                        .fileBytesReadInKb(perfLogMap.getOrDefault("fre","no entry found:fre"))
                        .fileBytesWriteInKb(perfLogMap.getOrDefault("fwr","no entry found:fwr"))
                        .networkBytesReadInKb(perfLogMap.getOrDefault("nre","no entry found:nre"))
                        .networkBytesWrittenInKb(perfLogMap.getOrDefault("nwr","no entry found:nwr"))
                        .totalSqlInvokingCount(perfLogMap.getOrDefault("sqlc","no entry found:sqlc"))
                        .totalSqlTimeInMs(perfLogMap.getOrDefault("sqlt","no entry found:sqlt"))
                        .httpStatusCode(perfLogMap.getOrDefault("rps","no entry found:rps"))
                        .sessionId(perfLogMap.getOrDefault("sid","no entry found:sid"))
                        .globalId(perfLogMap.getOrDefault("gid","no entry found:gid"))
                        .hashSessionId(perfLogMap.getOrDefault("hsid","no entry found:hsid"))
                        .callStackLevel(perfLogMap.getOrDefault("csl","no entry found:csl"))
                        .totalConcurrentCacheTime(perfLogMap.getOrDefault("ccon","no entry found:ccon"))
                        .totalSupersfedisCacheTime(perfLogMap.getOrDefault("csup","no entry found:csup"))
                        .locale(perfLogMap.getOrDefault("loc","no entry found:loc"))
                        .totalLocalsfedisCacheTime(perfLogMap.getOrDefault("cloc","no entry found:cloc"))
                        .totalExtremesfedisCacheTime(perfLogMap.getOrDefault("cext","no entry found:cext"))
                        .totalRemotesfedisCacheTime(perfLogMap.getOrDefault("crem","no entry found:crem"))
                        .callStack(callStack)
                        .build()
                ).findFirst().orElseThrow(()->new IllegalStateException("Could not initialize PerfLog"));
        return  perfLogObj;
    }

    public static Map<String, String> logToMapConvertor(String logString){
        final String stkRegEx = "(\\w+)=((?=\\{)(?:(?=.*?\\{(?!.*?\\3)(.*\\}(?!.*\\4).*))(?=.*?\\}(?!.*?\\4)(.*)).)+?.*?(?=\\3)[^{]*(?=\\4$)|\"{2}(.*?)\"{2}|(\\S+))";
        final String timeStampRegEx= "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d+)";
        final String dcRegEx = "(DC\\d+)";
        Pattern p3 = Pattern.compile(dcRegEx);
        Pattern p2 = Pattern.compile(timeStampRegEx);
        Pattern p = Pattern.compile(stkRegEx);
        Matcher m3 = p3.matcher(logString);
        Matcher m2 = p2.matcher(logString);
        Matcher m = p.matcher(logString);
        Map<String, String> res = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        log.debug("Extracting Log string :\n {}",logString);
        log.info("Parsing Logs , Key-Values extracted :");
        while(m.find()) {
            String val = m.group(2);
            if (m.group(5) != null) {
                val = m.group(5);
            }
            if (m.group(6) != null) {
                val = m.group(6);
            }
            res.put(m.group(1), val);
            log.info("{} => {} \n----",m.group(1),val );

        }


        while (m2.find()){
            if(m2.group()!=null){
                log.info("TimeStamp : {}",m2.group());
                res.put("timeStamp",m2.group());
            }
        }
        while (m3.find()){
            if(m3.group()!=null){
                log.info("DC : {}",m3.group());
                res.put("dc",m3.group());
            }


        }
        if(res.containsKey("STK"))
            log.info("Value of STK \n{}",res.get("STK"));
        return res;
    }


    private static  String afterEquals(String input) {
        return input.substring(input.indexOf('=') + 1)
                .replaceAll("\"","");
    }
}

