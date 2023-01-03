package com.ondemand.pinnacle.analyzer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.analyzer.models.CallStack;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 11:30 AM
 * @description
 */
@Slf4j
@Data
@Service("fetchPerfLogDataService")
public class FetchPerfLogDataService {

    private Optional<String> stackData = Optional.of("{\n" +
            "    \"n\": \"/xi/ajax/remoting/call/plaincall/adhocReportBuilderControllerProxy.getReportList.dwr\",\n" +
            "    \"i\": 1,\n" +
            "    \"t\": 219720,\n" +
            "    \"slft\": 217894,\n" +
            "    \"sub\":\n" +
            "    [\n" +
            "        {\n" +
            "            \"n\": \"SCA:com.successfactors.legacy.service.GetSysConfig\",\n" +
            "            \"i\": 1912,\n" +
            "            \"t\": 321,\n" +
            "            \"slft\": 178,\n" +
            "            \"q\": 0,\n" +
            "            \"qq\": 0,\n" +
            "            \"u\": 0,\n" +
            "            \"uu\": 0,\n" +
            "            \"m\": 407747,\n" +
            "            \"sub\":\n" +
            "            [\n" +
            "                {\n" +
            "                    \"n\": \"DISABLED-SYS_CONFIG_WITHOUT_BLOB-com.successfactors.cachelegacy.impl.MigrationCacheAdapter.get\",\n" +
            "                    \"i\": 1912,\n" +
            "                    \"t\": 34,\n" +
            "                    \"slft\": 34\n" +
            "                },\n" +
            "                {\n" +
            "                    \"n\": \"CONCURRENT-SysConfigCache-com.successfactors.cachelegacy.impl.MigrationCacheAdapter.get\",\n" +
            "                    \"i\": 1912,\n" +
            "                    \"t\": 25,\n" +
            "                    \"slft\": 25\n" +
            "                }\n" +
            "              ]  \n" +
            "        \t},\n" +
            "        {\n" +
            "            \"n\": \"SQL:select * from dc5prd_STOCKPM8945.REPORT_DEF where deleted = 'N' AND validation_flag = 0 AND SOURCES IS NULL  AND (REPORT_TYPE IS NULL OR REPORT_TYPE =?)\",\n" +
            "            \"i\": 1,\n" +
            "            \"t\": 31,\n" +
            "            \"slft\": 31,\n" +
            "            \"st\": 168916,\n" +
            "            \"m\": 102364288,\n" +
            "            \"nr\": 129383,\n" +
            "            \"rt\": 1143,\n" +
            "            \"rn\": 10173,\n" +
            "            \"fs\": 0\n" +
            "        }\n" +
            "         ]\n" +
            "        }");

            public CallStack fetchData() throws JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();
                CallStack callStack = objectMapper.readValue(getStackData().get(),CallStack.class);

                log.info("Value of n: \t {}",callStack.getCallNode());
                log.info("Value of t: \t {}",callStack.getTotalInvokeTimeInMs());

                return callStack;

            }

}
