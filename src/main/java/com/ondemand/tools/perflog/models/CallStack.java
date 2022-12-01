package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 11:24 AM
 * @description
 */
@Value
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "logs")
public class CallStack implements Serializable {
    private static final long serialVersionUID = -1698763054778439285L;
    @Id
    String id;
    String n;
    long i;
    long t;
    long slft;
    long q;
    long qq;
    long u;
    long uu;
    long m;
    long nr;
    long rt;
    long rn;
    long fs;
    List<CallStack> sub;
}
