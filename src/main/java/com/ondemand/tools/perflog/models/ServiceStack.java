package com.ondemand.tools.perflog.models;

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
public class ServiceStack implements Serializable {

    private static final long serialVersionUID = -1638163054778439285L;

    private String svcCall;
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

}
