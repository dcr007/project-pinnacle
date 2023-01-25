package com.ondemand.pinnacle.gateway.security.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApiErrorAttributes {
    private String timestamp;
    private String path;
    private int status;
    private String error;
    private String message;
    private String requestId;

    @Builder.Default
    private final Object and = "NO ADDITIONAL DETAILS.";
}
