package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 14/11/2022 - 11:12 AM
 * @description
 */
@Slf4j
@Data
@Getter
@Setter
@Document(collection = "AnyJsonPayLoad")
public class JsonPayLoad implements Serializable {
    private static final long serialVersionUID = -7238163054776439285L;

    JsonNode payload;

    @Id
    private String id ;

    public String toString(){
        return  payload.toPrettyString();
    }
}
