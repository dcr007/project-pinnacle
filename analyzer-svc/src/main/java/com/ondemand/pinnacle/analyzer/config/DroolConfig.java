package com.ondemand.pinnacle.analyzer.config;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Chandu D - i861116
 * @created 26/01/2023 - 2:42 PM
 * @description
 */
@Configuration
@Slf4j
public class DroolConfig {

    private final KieServices kieServices = KieServices.Factory.get();

    private KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/metrics.drl"));
        return kieFileSystem;

    }

    @Bean
    public KieContainer getKieContainer() throws IOException {

        getKieRepository();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        log.info("KieContainer with ReleaseId {} created.",kieContainer.getReleaseId().toString());
        return kieContainer;

    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }

    @Bean
    public KieSession getKieSession() throws IOException {

        KieSession kieSession = getKieContainer().newKieSession();
        log.info("KieSession session with Id: {} created.",kieSession.getIdentifier());
        return kieSession;

    }

}
