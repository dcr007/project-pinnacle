package com.ondemand.pinnacle.ingestion.kafka.producer;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 10:14 AM
 * @description
 */
import com.ondemand.pinnacle.ingestion.kafka.models.SplunkPayLoad;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.ondemand.pinnacle.ingestion.kafka.models.CallStack;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.jaas}")
    private String jaas;

    @Bean
    public ProducerFactory<String, CallStack> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //Set these if using SASL authentication or Confluent Cloud
        properties.put("security.protocol", "PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", jaas);
        properties.put("acks", "all");
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, CallStack> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, SplunkPayLoad> splunkPayLoadProducerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //Set these if using SASL authentication or Confluent Cloud
        properties.put("security.protocol", "PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", jaas);
        properties.put("acks", "all");
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, SplunkPayLoad> splunkPayLoadKafkaTemplate() {
        return new KafkaTemplate<>(splunkPayLoadProducerFactory());
    }
}
