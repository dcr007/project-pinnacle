package com.ondemand.pinnacle.analyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Chandu D - i861116
 * @created 12/12/2022 - 9:50 AM
 * @description
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwrPerfLogSummary implements PerfLogSummary<DwrPerfLogSummary>{
    PerfLog perfLogSummary;
    @Override
    public DwrPerfLogSummary generateSummary(PerfLog perfLog){

        PerfLog dwrPerfLog = PerfLog.builder()
                                    .perfLogId(perfLog.getPerfLogId())
                                    .timeStamp(perfLog.getTimeStamp())
                                    .perfLevel(perfLog.getPerfLevel())
                                    .companyId(perfLog.getCompanyId())
                                    .dataCenter(perfLog.getDataCenter())
                                    .url(perfLog.getUrl())
                                    .pageQualifier(perfLog.getPageQualifier())
                                    .requestId(perfLog.getRequestId())
                                    .requestExecutionTimeInMs(perfLog.getRequestExecutionTimeInMs())
                                    .totalMemory(perfLog.getTotalMemory())
                                    .totalCpu(perfLog.getTotalCpu())
                                    .userCpu(perfLog.getUserCpu())
                                    .systemCpu(perfLog.getSystemCpu())
                                    .totalSqlTimeInMs(perfLog.getTotalSqlTimeInMs())
                                    .totalSqlInvokingCount(perfLog.getTotalSqlInvokingCount())
                                    .networkBytesReadInKb(perfLog.getNetworkBytesReadInKb())
                                    .networkBytesWrittenInKb(perfLog.getFileBytesWriteInKb())
                                    .totalConcurrentCacheTime(perfLog.getTotalConcurrentCacheTime())
                                .build();

        return new DwrPerfLogSummary(dwrPerfLog);

    }


}
