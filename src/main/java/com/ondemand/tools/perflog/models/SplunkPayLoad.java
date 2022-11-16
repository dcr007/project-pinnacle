package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
@Document(collection = "SplunkPayLoad")
public class SplunkPayLoad implements Serializable {

    private static final long serialVersionUID = -1638163054776439285L;
    @Id
    private String sid;
    @JsonAlias({"results_link"})
    private  String resultsLink ;
    private  SplunkResult result ;
    private String time;
}
