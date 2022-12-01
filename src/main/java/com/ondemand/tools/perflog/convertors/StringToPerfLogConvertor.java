package com.ondemand.tools.perflog.convertors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.tools.perflog.kafka.models.CallStack;
import com.ondemand.tools.perflog.kafka.models.PerfLog;
import com.ondemand.tools.perflog.services.NextSequenceService;
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
public class StringToPerfLogConvertor implements Converter<String,PerfLog> {
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
                //removes any extra brackets in the json string
                stackStr = stackStr.replace("\"\"", "\"").trim();
                callStack = objectMapper.readValue(stackStr,new TypeReference<CallStack>(){});

            } catch (JsonProcessingException ex) {
                log.error("Error occurred while processing CallStack json,exception details : \n {}"
                            , ex.getMessage());
            }
        }
        perfLogObj = perfLogMap.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(perfMap -> PerfLog.builder()
                        .perfLogId(String.valueOf(nextSequenceIdGeneratorService.getNextSequence("perfLogId-")))
                        .timeStamp(perfLogMap.getOrDefault("timeStamp","no entry found: timeStamp"))
                        .dc(perfLogMap.getOrDefault("dc","no entry found: dc"))
                        .plv(perfLogMap.getOrDefault("plv","no entry found: plv"))
                        .cip(perfLogMap.getOrDefault("cip","no entry found: cip"))
                        .cmid(perfLogMap.getOrDefault("cmid","no entry found: cmid"))
                        .cmn(perfLogMap.getOrDefault("cmn","no entry found:cmn"))
                        .sn(perfLogMap.getOrDefault("sn","no entry found:sn"))
                        .dpn(perfLogMap.getOrDefault("dpn","no entry found:dpn"))
                        .iuid(perfLogMap.getOrDefault("iuid","no entry found:iuid"))
                        .eid(perfLogMap.getOrDefault("eid","no entry found:eid"))
                        .agn(perfLogMap.getOrDefault("agn","no entry found:agn"))
                        .rid(perfLogMap.getOrDefault("rid","no entry found:rid"))
                        .mtd(perfLogMap.getOrDefault("mtd","no entry found:mtd"))
                        .url(perfLogMap.getOrDefault("url","no entry found:url"))
                        .rqt(perfLogMap.getOrDefault("rqt","no entry found:rqt"))
                        .mid(perfLogMap.getOrDefault("mid","no entry found:mid"))
                        .pid(perfLogMap.getOrDefault("pid","no entry found:pid"))
                        .pq(perfLogMap.getOrDefault("pq","no entry found:pq"))
                        .sub(perfLogMap.getOrDefault("sub","no entry found:sub"))
                        .mem(perfLogMap.getOrDefault("mem","no entry found:mem"))
                        .cpu(perfLogMap.getOrDefault("cpu","no entry found:cpu"))
                        .ucpu(perfLogMap.getOrDefault("ucpu","no entry found:ucpu"))
                        .scpu(perfLogMap.getOrDefault("scpu","no entry found:scpu"))
                        .fre(perfLogMap.getOrDefault("fre","no entry found:fre"))
                        .fwr(perfLogMap.getOrDefault("fwr","no entry found:fwr"))
                        .nre(perfLogMap.getOrDefault("nre","no entry found:nre"))
                        .nwr(perfLogMap.getOrDefault("nwr","no entry found:nwr"))
                        .sqlc(perfLogMap.getOrDefault("sqlc","no entry found:sqlc"))
                        .sqlt(perfLogMap.getOrDefault("sqlt","no entry found:sqlt"))
                        .rps(perfLogMap.getOrDefault("rps","no entry found:rps"))
                        .sid(perfLogMap.getOrDefault("sid","no entry found:sid"))
                        .gid(perfLogMap.getOrDefault("gid","no entry found:gid"))
                        .hsid(perfLogMap.getOrDefault("hsid","no entry found:hsid"))
                        .csl(perfLogMap.getOrDefault("csl","no entry found:csl"))
                        .ccon(perfLogMap.getOrDefault("ccon","no entry found:ccon"))
                        .csup(perfLogMap.getOrDefault("csup","no entry found:csup"))
                        .loc(perfLogMap.getOrDefault("loc","no entry found:loc"))
                        .cloc(perfLogMap.getOrDefault("cloc","no entry found:cloc"))
                        .cext(perfLogMap.getOrDefault("cext","no entry found:cext"))
                        .crem(perfLogMap.getOrDefault("crem","no entry found:crem"))
                        .stk(callStack)
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
        return res;
    }


    private static  String afterEquals(String input) {
        return input.substring(input.indexOf('=') + 1)
                .replaceAll("\"","");
    }
}

