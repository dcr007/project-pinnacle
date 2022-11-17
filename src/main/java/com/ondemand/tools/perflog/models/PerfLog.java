package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Chandu D - i861116
 * @created 17/11/2022 - 11:11 AM
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerfLog implements Serializable {
    private static final long serialVersionUID = -5238163054776439285L;
    private String timeStamp;
    private String plv;
    private String cip;
    private String cmid;
    private String cmn;
    private String sn;
    private String dpn;
    private String uid;
    private String un;
    private String iuid;
    private String eid;
    private String agn;
    private String mtd;
    private String url;
    private String rqt;
    private String mid;
    private String pid;
    private String nwr;
    private String sqlc;
    private String sqlt;
    private String rps;
    private String sid;
    private String gid;
    private String hsid;
    private String csl;
    private String ccon;
    private String csup;
    private String cloc;
    private String cext;
    private String crem;
    private CallStack stk;
}
