package com.ondemand.pinnacle.gateway.security;


import com.ondemand.pinnacle.gateway.exceptions.GlobalServerAuthenticationEntryPoint;
import com.ondemand.pinnacle.gateway.security.models.AppSecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private AppSecurityConstants asc;

    @Autowired
    private GlobalServerAuthenticationEntryPoint globalServerAuthenticationEntryPoint;

    private final ServerAccessDeniedHandler accessDeniedHandler =
            (ServerWebExchange exchange, AccessDeniedException exception) ->
            {
                return Mono.fromRunnable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                });
            };
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                /*
                 * Injecting authentication manager. And context repo to hold auth data while processing.
                 */
//                .securityContextRepository(securityContextRepository)
//                .authenticationManager(authenticationManager)


                /*
                 * Disabling any other security.
                 */
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();

                /*
                 * Handler in case of authentication failure or unauthenticated access:
                 * In case of an authentication failure or unauthenticated access, by default Spring Security
                 * redirects the user to a login page where the user gets a chance to login.
                 * But in REST APIs, we'd instead like to return a 401/403 response.
                 * To configure so, we can add the following to our SecurityWebFilterChain
                 */
//                .exceptionHandling()
//                .authenticationEntryPoint(globalServerAuthenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler);


        /*
         * Path specs for authorization and role based permissions.
         */
        http.authorizeExchange()
                /*
                 * GATEWAY SERVICE auth guards.
                 */
				.pathMatchers(asc.getLogin()).permitAll()
                .pathMatchers(asc.getToken()).permitAll()
                .pathMatchers(asc.getActuator()).permitAll()

                /*
                 * Exposing dataIngestion service
                 */
                .pathMatchers("/svc/ingestion/**").permitAll()

                /*
                 * Visionone cp service auth guards.
                 */
//				.pathMatchers("/api/cp/alt/**").permitAll()
//				.pathMatchers("/api/**").hasRole("Project - Developers")
//				.pathMatchers("/api/**").hasRole("Project - Administrators")



                /*
                 * USER MANAGEMENT SERVICE auth guards.
                 */
//				.pathMatchers(asc.getSignup()).permitAll()
//                .pathMatchers("/api/usermgnt/rest/v1/admin/**").hasAnyRole(
//                        CdmUserRole.APP_ADMIN.toString(),
//                        CdmUserRole.API_ADMIN.toString())


                /*
                 * CORE SERVICE auth guards.
                 */
//				.pathMatchers("/api/core/rest/v1/internal/**").denyAll()


                /*
                 * DSAF SERVICE auth guards.
                 */


                /*
                 * EMAIL SERVICE  auth guards.
                 */


                .anyExchange().authenticated();

        return http.build();
    }
}
