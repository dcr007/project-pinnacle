package com.ondemand.pinnacle.gateway;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import com.ondemand.pinnacle.gateway.security.models.AppSecurityConstants;

@Configuration
@Log4j2
public class InitBeans {
    @Autowired
    private Environment env;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AppSecurityConstants getAppSecurityConstants(){
        return new AppSecurityConstants(
                env.getProperty("constants.security.jwt.secret"),
                Integer.parseInt(
                        env.getProperty("constants.security.jwt.expiration-time-seconds"))*1000,
                env.getProperty("constants.security.jwt.token-type"),
                env.getProperty("constants.security.apis.login"),
                env.getProperty("constants.security.apis.token"),
                env.getProperty("constants.security.apis.signup"),
                env.getProperty("constants.security.apis.actuator")

        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }

}
