package com.ondemand.pinnacle.ingestion.kafka.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 11:17 AM
 * @description
 */

@Jacksonized
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplunkResult implements Serializable {

    private static final long serialVersionUID = -1698863054778439285L;
    @Id
    String resultId;

    @JsonAlias({"SFDC"})
    private String dc;
    @JsonAlias({"URL"})
    private String url;
    private String raw;
    private String _raw;
    private List<PerfLog> perfLog;
}
