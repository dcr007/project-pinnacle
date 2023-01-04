package com.ondemand.pinnacle.analyzer.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ondemand.pinnacle.analyzer.app.RestClient.RestClientBuilder;
import org.springframework.http.HttpHeaders;
import java.util.Arrays;

/**
 * @author Chandu D - i861116
 * @created 03/01/2023 - 11:40 AM
 * @description
 */
@Service
@Slf4j
public class ExternalRestClientFactory {
    @Autowired
    @Qualifier("externalRestTemplate")
    private RestTemplate restTemplate;

//    TODO: To implement AppSecurity Utils to include jwt authentication
//    @Autowired
//    private AppSecurityUtils utils;

    public RestTemplate getRestTemplate() { return restTemplate; }

    public RestClientService newRestClient(String uri, HttpHeaders headers){
        return  this.getRestClientBuilder().restTemplate(this.restTemplate)
                .uri(uri)
                .build();
    }
    public RestClientBuilder getRestClientBuilder(){
        return RestClient.builder().restTemplate(restTemplate);
    }

    /**
     * @description  this uses basic Authenticaiton
     * @param uri
     * @param basicAuthUser
     * @param basicAuthPassword
     * @return
     */
    public RestClientService newRestClient(String uri, String basicAuthUser, String basicAuthPassword) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setBasicAuth(basicAuthUser, basicAuthPassword);

        return this.newRestClient(uri, headers);
    }

    /**
     * @description  this uses service token authentication.
     * @param uri
     * @return
     *
     */
    // TODO: to implement authentication using serviceToken
    public RestClientService newInternalRestClient(String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(utils.getServiceToken());

        return this.newRestClient(uri, headers);
    }


}
