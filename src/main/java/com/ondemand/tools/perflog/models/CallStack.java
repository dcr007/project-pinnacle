package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 11:24 AM
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "logs")
public class CallStack implements Serializable {
    private static final long serialVersionUID = -1698763054778439285L;
    @Id
    private String id;
    private String n;
    private  long i;
    private long t;
    private long slft;
    private long q;
    private long qq;
    private long u;
    private long uu;
    private long m;
    private long nr;
    private long rt;
    private long rn;
    private long fs;
    private List<CallStack> sub;
}
