package com.ondemand.pinnacle.ingestion.analyzer.models;

/**
 * @author Chandu D - i861116
 * @created 13/12/2022 - 5:24 PM
 * @description
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"dwrPerfLogSummary", "dwrRcaSummary"})
public class DwrAnalysisResult implements AnalysisResult {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @JsonProperty("perfLogSummary")
    DwrPerfLogSummary dwrPerfLogSummary;
    @JsonProperty("rcaSummary")
    DwrRcaSummary dwrRcaSummary;

    public JsonNode getDwrAnalysisResult() {
        JsonNode callStackAnalysisAsJsonNode = objectMapper.convertValue(this.dwrRcaSummary, JsonNode.class);
        JsonNode dwrPerfLogSummaryAsJsonNode = objectMapper.convertValue(this.dwrPerfLogSummary, JsonNode.class);
        return merge(dwrPerfLogSummaryAsJsonNode,callStackAnalysisAsJsonNode);
    }

    @Override
    public JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        return AnalysisResult.super.merge(mainNode, updateNode);
    }


}
