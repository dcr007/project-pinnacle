package com.ondemand.pinnacle.analyzer.models.ingestion;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 11:24 AM
 * @description
 */
@NoArgsConstructor
@ToString
@Data
public class CallStackModel implements Serializable {
    private static final long serialVersionUID = -1698763054778439215L;
//    @Id
//    @JsonAlias("_id")
    String callStackId;
    @JsonAlias("n")
    String callNode;//n: Name for the call node to identify which function is execution,
    // it could be SCA, JDBC, SQL and any other name defined by developer
    @JsonAlias("i")
    long invokedCount;
    @JsonAlias("t")
    long totalInvokeTimeInMs;
    @JsonAlias("slft")
    long executionTimeInMs;//execution time taken by the function itself. slft = t - sum(sub.t)
    @JsonAlias("q")
    long distinctSqlQueryCount;
    @JsonAlias("qq")
    long totalSqlQueryCount;
    @JsonAlias("u")
    long distinctSqlUpdates;
    @JsonAlias("uu")
    long totalSqlUpdates;
    @JsonAlias("m")
    long memoryUsedByThreadInKb;
    @JsonAlias("nr")
    long networkReadInKb;
    @JsonAlias("rt")
    long accumulatedTimeInMs;   // rt: Accumulated time for rs.next() execution. [ms]. 
    // Time tracking logic is removed in b2211, since in Hana this field does not have obvious help,
    // to keep format compatible, in Perflog output, the field is still there , but value will be 0.
    @JsonAlias("rn")
    long resultSetCount; //Result number for the query. We get this by counting execution # of rs.next().
    @JsonAlias("fs")
    long fetchSize;

    List<CallStackModel> sub;
}