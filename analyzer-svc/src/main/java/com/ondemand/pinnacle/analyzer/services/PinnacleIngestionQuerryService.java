package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.analyzer.app.ExternalRestClientFactory;
import com.ondemand.pinnacle.analyzer.models.IngestionEventStatus;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PinnacleIngestionQuerryService {

    @Autowired
    private ExternalRestClientFactory restClientFactory;
    private final String ingestionQueryApi ="/svc/ingestion/rest/v1/api/internal/query";
    private final String findByIngestionStatusApi= "/findByIngestionSvcStatus";
    @Value("${constants.services.gateway.url}")
    private String url;

    public PerfLogModel[] findByIngestionStatus(IngestionEventStatus status){
//        TODO: fetch all perflogs for the given status
        log.info("Service request to collect  perf-logs in status {}",status);

        ResponseEntity<PerfLogModel[]> response = restClientFactory
                .newInternalRestClient(this.url+this.ingestionQueryApi +
                        this.findByIngestionStatusApi + "/"+status)
                .doGet(PerfLogModel[].class);

        log.info("{} records were collected.", response.getBody()!=null?
                response.getBody().length : 0);

        log.debug("Response body:{} ", response.getBody());
        return  response.getBody();
    }


}