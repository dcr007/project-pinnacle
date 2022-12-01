package com.ondemand.tools.perflog.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ondemand.tools.perflog.convertors.SplunkPayLoadConvertor;
import com.ondemand.tools.perflog.convertors.StringToPerfLogConvertor;
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
@EnableMongoRepositories(basePackages = {"com.ondemand.tools.perflog.repository"})
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    @Override
    public @Bean MongoClient mongoClient() {

//        String CONNECTION_STRING =
//                "mongodb%3A//admin%3Apassword%40localhost:27017/perflogglobaldb?authSource=admin&retryWrites=true&w=majority\n";

        final ConnectionString connectionString = new ConnectionString("mongodb://admin:password@localhost:27017/perflogglobaldb?authSource=admin");
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
        converters.add(new SplunkPayLoadConvertor());
        log.info("Size of convertors: {}", converters.size());
        return  new MongoCustomConversions(converters);
    }


}
