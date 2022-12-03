package com.ondemand.pinnacle.ingestion.kafka.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 14/11/2022 - 10:08 AM
 * @description
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "rawData")
public class RawData implements Serializable {
    private static final long serialVersionUID = -1698763054778439285L;
    @Id
    private String id;
    private String body;

    @Override
    public String toString(){
        return
                "RawData{" +
                        "data = '" + body + '\'' +
                        "}";
    }
}
