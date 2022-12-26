package com.ondemand.pinnacle.ingestion.models;

/**
 * @author Chandu D - i861116
 * @created 01/12/2022 - 12:00 PM
 * @description - CustomSequences is just a simple class representing the collection.
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "customSequences")
public class CustomSequences {
    @Id
    private String id;
    private long seq;
}