package com.ondemand.pinnacle.gateway.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class AppSecurityConstants {
    private String secret;
    private long expirationTime;
    private String tokenPrefix;
    private String login;
    private String token;
    private String signup;
    private String actuator;

}
