package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 11:17 AM
 * @description
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
@Document(collection = "SplunkPayLoad")
public class SplunkPayLoad implements Serializable {

    private static final long serialVersionUID = -1238163054776439285L;

//    @javax.persistence.Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
////    @Column(name = "id", nullable = false)
//    private Long id;


//    @javax.persistence.Id
    @Id
    private String sid;

    @JsonAlias({"results_link"})
    private  String resultsLink ;
    private  SplunkResult result ;
    private String time;



}
