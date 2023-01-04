package com.ondemand.pinnacle.analyzer.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chandu D - i861116
 * @created 03/01/2023 - 2:47 PM
 * @description Abstraction layer for Rest Template client services.
 */

public interface RestClientService {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RestClientService.class);

    <T> ResponseEntity<T> doGet(Class<T> clz);


    /**
     * POST call
     * @param clz - Class object of mapping entity for response body e.g. String.class
     * @param body - Serializable POJO or even a JSON String.
     * @return ResponseEntity<T> for clz Type provided
     */
    <T> ResponseEntity<T> doPost(Class<T> clz, T body);


    /**
     * POST call with different response type
     * @param clz
     * @param body
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> ResponseEntity<R> doPostResponse(Class<R> clz, T body);

    /**
     * PUT call
     * @param clz - Class object of mapping entity for response body e.g. String.class
     * @param body - Serializable POJO or even a JSON String.
     * @return ResponseEntity<T> for clz Type provided
     */
    <T> ResponseEntity<T> doPut(Class<T> clz, T body);

    /**
     * @description wrapper method for exchange calls;
     * TODO: need to implment exception handling.
     * @param restTemplate
     * @param uri
     * @param method
     * @param httpEntity
     * @param clz
     * @return
     * @param <T>
     */
    default  <T> ResponseEntity<T> exchange(RestTemplate restTemplate, String uri,
                                HttpMethod method, HttpEntity<?> httpEntity, Class<T> clz) {
        log.info("{} call to {}", method,clz);
        return restTemplate.exchange(uri,method,httpEntity,clz);
    }
}
