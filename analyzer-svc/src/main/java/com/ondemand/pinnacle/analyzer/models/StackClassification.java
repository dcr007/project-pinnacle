package com.ondemand.pinnacle.analyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 2:23 PM
 * @description
 */
@Entity
@Table(name="analyzer_svc_stack_classification")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StackClassification implements Serializable {
    private static final long serialVersionUID = -1698763054978439285L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="perf_log_id", nullable = false)
    private String perfLogId;

    @Column(name ="qualifier_name")
    private String qualifierName;
//    @JsonIgnore
    @Column(name ="stack_category")
    @Enumerated(EnumType.STRING)
    private StackCategory stackCategory;
    @Column(name="has_threshold_exceeded")
    private boolean hasThresholdExceeded ;

    @Column(name="call_id")
    private String callId;
    @Column(name="invoked_count")
    long invokedCount;
    @Column(name="total_invoke_time_in_ms")
    long totalInvokeTimeInMs;
    @Column(name="execution_time_in_ms")
    long executionTimeInMs;

    @Column(name="distinct_sql_query_count")
    long distinctSqlQueryCount;
    @Column(name="total_sql_query_count")
    long totalSqlQueryCount;
    @Column(name="distinct_sql_updates")
    long distinctSqlUpdates;
    @Column(name="total_sql_updates")
    long totalSqlUpdates;
    @Column(name="memory_used_by_thread_in_kb")
    long memoryUsedByThreadInKb;
    @Column(name="network_read_in_kb")
    long networkReadInKb;
    @Column(name="accumulated_time_in_ms")
    long accumulatedTimeInMs;
    @Column(name="result_set_count")
    long resultSetCount;
    @Column(name="fetch_size")
    long fetchSize;
}
