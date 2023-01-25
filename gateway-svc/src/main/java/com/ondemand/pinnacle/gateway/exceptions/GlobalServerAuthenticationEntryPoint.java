package com.ondemand.pinnacle.gateway.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.gateway.security.models.ApiErrorAttributes;
import lombok.Data;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@Data
public class GlobalServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private HttpStatus httpStatus;
    private int status;
    private String message;
    private String error;

    @Override
    public Mono<Void> commence(ServerWebExchange swe, AuthenticationException e) {
        ApiErrorAttributes attributes = null;

        ServerHttpRequest request = swe.getRequest();
        ServerHttpResponse response = swe.getResponse();

        attributes = ApiErrorAttributes.builder()
                .error(this.error == null ?
                        e.getLocalizedMessage() : this.error)
                .status(this.status != 0 ? this.status : response.getRawStatusCode())
                .message(this.message == null ? e.getMessage() : this.message)
                .path(request.getPath().toString())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(request.getId())
                .build();

        swe.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        swe.getResponse().setRawStatusCode(attributes.getStatus());

        ObjectMapper mapper = new ObjectMapper();
        byte[] body = null;
        try {
            body = mapper.writeValueAsBytes(attributes);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        DataBuffer buffer = swe.getResponse().bufferFactory().wrap(body);
        Mono<Void> rtn = swe.getResponse().writeWith(Flux.just(buffer));


        /* clear this singleton's state */
        this.clearState();

        return rtn;
    }

    /**
     * since the @Component is a singleton, this class will persist state,
     * hence the attributes needs to be null again for next call.
     */
    private void clearState() {
        this.status = 0;
        this.httpStatus = null;
        this.error = null;
        this.message = null;
    }
}

