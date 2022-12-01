package com.ondemand.tools.perflog.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.tools.perflog.convertors.PerfLogConvertor;
import com.ondemand.tools.perflog.convertors.StringToPerfLogConvertor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * @author Chandu D - i861116
 * @created 13/10/2022 - 11:17 AM
 * @description
 */

@Jacksonized
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplunkResult implements Serializable {

    private static final long serialVersionUID = -1698863054778439285L;

    @JsonAlias({"SFDC"})
    private String dc;
    @JsonAlias({"URL"})
    private String url;

//    @JsonAlias({"_raw"})
    private String raw;
    private String _raw;

//    @JsonAlias({"raw"})
//    private String _raw;



//    @Convert(converter = StringToPerfLogConvertor.class)
    private List<PerfLog> perfLog;

   /* public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public PerfLog getPerfLog() {
        return perfLog;
    }

    public void setPerfLog(PerfLog perfLog) {
        this.perfLog = perfLog;
    }*/

    //        private static final ObjectMapper objectMapper = new ObjectMapper();
   /* public void serializePerfLog() throws JsonProcessingException{
        this.setPerfLog(objectMapper.writeValueAsString(perfLog));
    }*/

   /* public void deserializePerfLog() throws IOException {
        this.setPerfLog(objectMapper.readValue(this.getRaw(),
                new TypeReference<PerfLog>(){}));
    }*/
}
