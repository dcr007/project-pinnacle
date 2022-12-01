package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Chandu D - i861116
 * @created 17/11/2022 - 11:11 AM
 * @description
 */

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerfLog implements Serializable {
    private static final long serialVersionUID = -5238163054776439285L;
    String timeStamp;
    String dc;
    String plv;
    String cip;
    String cmid;
    String cmn;
    String sn;
    String dpn;
    String uid;
    String un;
    String iuid;
    String eid;
    String agn;
    String rid;
    String mtd;
    String url;
    String rqt;
    String mid;
    String pid;
    String pq;
    String sub;
    String mem;
    String cpu;
    String ucpu;
    String scpu;
    String fre;
    String fwr;
    String nre;
    String nwr;
    String sqlc;
    String sqlt;
    String rps;
    String sid;
    String gid;
    String hsid;
    String csl;
    String ccon;
    String csup;
    String loc;
    String cloc;
    String cext;
    String crem;
    CallStack stk;
//    Map<String,Object> stk;
}
