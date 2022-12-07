package com.ondemand.pinnacle.analyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ondemand.pinnacle.analyzer.models.enums.CallCategory;
import lombok.AllArgsConstructor;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SegregatedStack implements Serializable {
    private static final long serialVersionUID = -1698763054978439285L;

    private String qualifierName;
    private CallCategory callCategory;
    private boolean hasThresholdExceeded ;
    private String callId;
    private long iterationCount;
    private long timeTaken;
    private long memoryConsumption;

}
