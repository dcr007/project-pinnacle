package com.ondemand.pinnacle.analyzer.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ondemand.pinnacle.analyzer.convertors.StringToPerfLogConvertor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 21/11/2022 - 3:21 PM
 * @description
 */
@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = {"com.ondemand.pinnacle.ingestion.repository"})
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    @Override
    public @Bean
    @NotNull MongoClient mongoClient() {

        final String CONNECTION_STRING =
                "mongodb://admin:password@localhost:27017/perflogglobaldb?authSource=admin";

        final ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
//        TODO: get the DBname from app.prop
        return "perflogglobaldb";
    }
    @Override
    public @NotNull MongoCustomConversions customConversions(){
        converters.add(new StringToPerfLogConvertor());
        log.debug("Size of convertors: {}", converters.size());
        return  new MongoCustomConversions(converters);
    }

}
