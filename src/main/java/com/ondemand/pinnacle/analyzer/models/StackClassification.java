package com.ondemand.pinnacle.analyzer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 2:23 PM
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StackClassification implements Serializable {
    private static final long serialVersionUID = -1698763054978439285L;

    private String qualifierName;
    @JsonIgnore
    private StackCategory stackCategory;
    private boolean hasThresholdExceeded ;
    private String callId;
    long invokedCount;
    long totalInvokeTimeInMs;
    long executionTimeInMs;
    long distinctSqlQueryCount;
    long totalSqlQueryCount;
    long distinctSqlUpdates;
    long totalSqlUpdates;
    long memoryUsedByThreadInKb;
    long networkReadInKb;
    long accumulatedTimeInMs;
    long resultSetCount;
    long fetchSize;
}
