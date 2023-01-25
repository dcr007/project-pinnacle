package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.analyzer.app.clients.ExternalRestClientFactory;
import com.ondemand.pinnacle.analyzer.app.clients.constants.ingestion.IngestionEventStatus;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PinnacleIngestionQueryService {

    @Autowired
    private ExternalRestClientFactory restClientFactory;
    private final String ingestionQueryApi ="/svc/ingestion/rest/v1/api/internal/query";
    private final String ingestionCommandApi ="/svc/ingestion/rest/v1/api/internal/command";
    private final String findByIngestionStatusApi= "/findByIngestionSvcStatus";

    private final String  ingestionUpdateApi = "/updateIngestionStatusTo";
    @Value("${constants.services.gateway.url}")
    private String url;

    public PerfLogModel[] findByIngestionStatus(IngestionEventStatus status){

        log.info("Service request to collect  perf-logs in status {}",status);
        log.info("Querying with internal ingestion service call to: {}",this.url+this.ingestionQueryApi +
                this.findByIngestionStatusApi + "/"+status);

        ResponseEntity<PerfLogModel[]> response = restClientFactory
                .newInternalRestClient(this.url+this.ingestionQueryApi +
                        this.findByIngestionStatusApi + "/"+status)
                .doGet(PerfLogModel[].class);

        log.info("{} records were collected.", response.getBody()!=null?
                response.getBody().length : 0);

//        log.debug("Response body:{} ", Arrays.toString(response.getBody()));
        return  response.getBody();
    }


    public void updateIngestionStatus(IngestionEventStatus status, String perfLogIds) {
        log.info("Service request to update the IngestionEvent status to {} for perLogId(s) {}"
                ,status,perfLogIds);


        ResponseEntity<String> res = restClientFactory.newInternalRestClient
                (url+ingestionCommandApi+ingestionUpdateApi+"/"+status)
                .doPut(String.class,perfLogIds);
    }
}