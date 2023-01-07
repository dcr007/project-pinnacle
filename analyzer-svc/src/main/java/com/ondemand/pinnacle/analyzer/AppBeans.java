package com.ondemand.pinnacle.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chandu D - i861116
 * @created 06/01/2023 - 4:31 PM
 * @description
 */
@Component
@Slf4j
public class AppBeans {
    @Autowired
    private Environment env;

    @Bean("internalRestTemplate")
    public RestTemplate getInternalRestTemplate() {
        return new RestTemplate();
    }

    @Bean("externalRestTemplate")
    public RestTemplate getExternalRestTemplate(){
        return  new RestTemplate();
    }
}
