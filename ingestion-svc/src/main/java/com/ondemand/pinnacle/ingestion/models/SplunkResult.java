package com.ondemand.pinnacle.ingestion.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

//import javax.validation.Valid;
import java.io.Serializable;

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

    @JsonProperty("SFDC")
    private String dc;
    @JsonAlias({"URL","url"})
    private String url;

    @JsonProperty("_raw")
    @JsonAlias({"raw","_raw"})
    private String raw;
//    private String _raw;
//    @Valid
    private PerfLog perfLog;
}
