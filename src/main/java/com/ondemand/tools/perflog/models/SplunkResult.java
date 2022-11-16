package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 11:17 AM
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplunkResult implements Serializable {

    private static final long serialVersionUID = -1698863054778439285L;

    @JsonAlias({"SFDC"})
    private String dc;
    @JsonAlias({"URL"})
    private String url;
    @JsonAlias({"_raw"})
    private String raw;
}
