package com.ondemand.pinnacle.analyzer.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

/**
 * @author Chandu D - i861116
 * @created 03/01/2023 - 2:45 PM
 * @description
 */
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class RestClient implements RestClientService{

    private RestTemplate restTemplate;

    private HttpHeaders headers;

    private String uri;


    @Override
    public <T> ResponseEntity<T> doGet(Class<T> clz) {
        HttpEntity<T> httpEntity = new HttpEntity<T>(this.headers);
        return this.exchange(this.restTemplate,this.uri, HttpMethod.GET,httpEntity,clz);
    }

    @Override
    public <T, R> ResponseEntity<R> doPostResponse(Class<R> clz, T body) {
        HttpEntity<T> httpEntity = new HttpEntity<T>(body, this.headers);
        return this.exchange(this.restTemplate,this.uri, HttpMethod.POST, httpEntity, clz);
    }

    @Override
    public <T> ResponseEntity<T> doPut(Class<T> clz, T body) {
        HttpEntity<T> httpEntity = new HttpEntity<T>(body, this.headers);
        return this.exchange(this.restTemplate,this.uri, HttpMethod.PUT, httpEntity, clz);
    }

    @Override
    @Deprecated
    /**
     * Replaced with doPostResponse
     */
    public <T> ResponseEntity<T> doPost(Class<T> clz, T body) {
        HttpEntity<T> httpEntity = new HttpEntity<T>(body, this.headers);
        return this.exchange(this.restTemplate,this.uri, HttpMethod.POST, httpEntity, clz);
    }
}
