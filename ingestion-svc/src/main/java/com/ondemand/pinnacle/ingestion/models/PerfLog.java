package com.ondemand.pinnacle.ingestion.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 17/11/2022 - 11:11 AM
 * @description
 */

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document("PerfLog-#{@environment.getProperty('mongodb.collections.perf-logs.dwr-suffix')}")
public class PerfLog implements Serializable {
//public class PerfLog  {
    private static final long serialVersionUID = -5238163054776439285L;
    String perfLogId;
    @JsonAlias("_id")
    @Id
    @Indexed(unique = true,sparse = true)
    String timeStamp;

    @JsonAlias("dc")
    String dataCenter;

    @JsonAlias("plv")
    String perfLevel;

    @JsonAlias("cip")
    String clientIp;

    @JsonAlias("cmid")
    String companyId;

    @JsonAlias("cmn")
    String companyName;

    @JsonAlias("sn")
    String schemaName;

    @JsonAlias("dpn")
    String dbPoolName;

    @JsonAlias("uid")
    String userId;

    @JsonAlias("un")
    String userName;

    @JsonAlias("iuid")
    String internalUserId;

    @JsonAlias("eid")
    String eventId;

    @JsonAlias("agn")
    String agent;

    @JsonAlias("rid")
    String requestId;
    
    @JsonAlias("mtd")
    String requestMethod;

    @JsonAlias("url")
    String url;

    @JsonAlias("rqt")
    String requestExecutionTimeInMs;

    @JsonAlias("mid")
    String moduleId;

    @JsonAlias("pid")
    String pageId;

    @JsonAlias("pq")
    String pageQualifier;
    @JsonAlias("sub")
    String sub;

    @JsonAlias("mem")
    String totalMemory;
    @JsonAlias("cpu")
    String totalCpu;

    @JsonAlias("ucpu")
    String userCpu;

    @JsonAlias("scpu")
    String systemCpu;

    @JsonAlias("fre")
    String fileBytesReadInKb;

    @JsonAlias("fwr")
    String fileBytesWriteInKb;

    @JsonAlias("nre")
    String networkBytesReadInKb;

    @JsonAlias("nwr")
    String networkBytesWrittenInKb;

    @JsonAlias("sqlc")
    String totalSqlInvokingCount;

    @JsonAlias("sqlt")
    String totalSqlTimeInMs;

    @JsonAlias("rps")
    String httpStatusCode;

    @JsonAlias("sid")
    String sessionId;

    @JsonAlias("gid")
    String globalId;

    @JsonAlias("hsid")
    String hashSessionId;

    @JsonAlias("csl")
    String callStackLevel;

    @JsonAlias("ccon")
    String totalConcurrentCacheTime;

    @JsonAlias("csup")
    String totalSupersfedisCacheTime;

    @JsonAlias("loc")
    String locale;

    @JsonAlias("cloc")
    String totalLocalsfedisCacheTime;

    @JsonAlias("cext")
    String totalExtremesfedisCacheTime;

    @JsonAlias("crem")
    String totalRemotesfedisCacheTime;

    @JsonAlias("stk")
    CallStack callStack;
}
