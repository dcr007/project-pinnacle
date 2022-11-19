package com.ondemand.tools.perflog.convertors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.models.PerfLog;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Chandu D - i861116
 * @created 17/11/2022 - 11:40 AM
 * @description
 */
@Slf4j
@Converter(autoApply = true)
public class PerfLogConvertor implements AttributeConverter<PerfLog,String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(PerfLog perfLog) {

        String perfLogJson = null;
        try {
            perfLogJson = objectMapper.writeValueAsString(perfLog);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return perfLogJson;
    }

    @Override
    public PerfLog convertToEntityAttribute(String pLog) {

        if(pLog!=null) return perfLogConvertor(pLog).get();
       else {
           log.error("Cannot convert PerfLog String to Entity ");
           return (PerfLog) Optional.empty().get();
       }
    }

    public static Optional<PerfLog> perfLogConvertor(String str){
        // The string to be searched on
        String actualString = str;

        //Regular expression to be applied on the actualString
        final String REGEX = "\\s(?=(([^\"]*\"){2})*[^\"]*$)\\s*";

        //Create pattern using the REGEX
        Pattern pattern = Pattern.compile(REGEX);

        // Split the text
        Stream<String> strSplitStream= pattern.splitAsStream(actualString);


        //build a new Instance of PerfLog based on the parsed data.
        PerfLog perfLog = new PerfLog();

        List<String> logList = strSplitStream.collect(Collectors.toList());
        String timeStamp = logList.get(0).concat(" "+logList.get(1));
        perfLog.setTimeStamp(timeStamp);
        logList.forEach(
                s -> {
                    if (s.contains("PLV")) perfLog.setPlv(afterEquals(s));
                    else if (s.contains("CIP")) perfLog.setCip(afterEquals(s));
                    else if (s.contains("CMID")) perfLog.setCmid(afterEquals(s));
                    else if (s.contains("CMN")) perfLog.setCmn(afterEquals(s));
                    else if (s.contains("SN")) perfLog.setSn(afterEquals(s));
                    else if (s.contains("DPN")) perfLog.setDpn(afterEquals(s));
                    else if (s.contains("UID")) perfLog.setUid(afterEquals(s));
                    else if (s.contains("UN")) perfLog.setUn(afterEquals(s));
                    else if (s.contains("IUID")) perfLog.setIuid(afterEquals(s));
                    else if (s.contains("LOC")) perfLog.setLoc(afterEquals(s));
                    else if (s.contains("EID")) perfLog.setEid(afterEquals(s));
                    else if (s.contains("AGN")) perfLog.setAgn(afterEquals(s));
                    else if (s.contains("RID")) perfLog.setRid(afterEquals(s));
                    else if (s.contains("MTD")) perfLog.setMtd(afterEquals(s));
                    else if (s.contains("RQT")) perfLog.setRqt(afterEquals(s));
                    else if (s.contains("MID")) perfLog.setMid(afterEquals(s));
                    else if (s.contains("PID")) perfLog.setPid(afterEquals(s));
                    else if (s.contains("PQ")) perfLog.setPq(afterEquals(s));
                    else if (s.contains("SUB")) perfLog.setSub(afterEquals(s));
                    else if (s.contains("MEM")) perfLog.setMem(afterEquals(s));
                    else if (s.contains("CPU")) perfLog.setCpu(afterEquals(s));
                    else if (s.contains("UCPU")) perfLog.setUcpu(afterEquals(s));
                    else if (s.contains("SCPU")) perfLog.setScpu(afterEquals(s));
                    else if (s.contains("FRE")) perfLog.setFre(afterEquals(s));
                    else if (s.contains("FWR")) perfLog.setFwr(afterEquals(s));
                    else if (s.contains("NRE")) perfLog.setNre(afterEquals(s));
                    else if (s.contains("NWR")) perfLog.setNwr(afterEquals(s));
                    else if (s.contains("SQLC")) perfLog.setSqlc(afterEquals(s));
                    else if (s.contains("SQLT")) perfLog.setSqlt(afterEquals(s));
                    else if (s.contains("RPS")) perfLog.setRps(afterEquals(s));
                    else if (s.contains("SID")) perfLog.setSid(afterEquals(s));
                    else if (s.contains("GID")) perfLog.setGid(afterEquals(s));
                    else if (s.contains("HSID")) perfLog.setHsid(afterEquals(s));
                    else if (s.contains("CSL")) perfLog.setCsl(afterEquals(s));
                    else if (s.contains("CCON")) perfLog.setCcon(afterEquals(s));
                    else if (s.contains("CSUP")) perfLog.setCsup(afterEquals(s));
                    else if (s.contains("CLOC")) perfLog.setCloc(afterEquals(s));
                    else if (s.contains("CEXT")) perfLog.setCext(afterEquals(s));
                    else if (s.contains("CREM")) perfLog.setCrem(afterEquals(s));
                    else if (s.contains("STK")) {
                        try {
                            CallStack stack = objectMapper.readValue(afterEquals(s),
                                    new TypeReference<CallStack>(){});
                            perfLog.setStk(stack);
                        } catch (JsonProcessingException e) {
                            log.error("Error occurred while processing CallStack json {}",e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }

        );
        log.info("Converted PerfLog String to Entity:");
        log.info(perfLog.toString());

        return Optional.of(perfLog);
    }
    private static  String afterEquals(String input) {
        return input.substring(input.indexOf('=') + 1);
    }
}
