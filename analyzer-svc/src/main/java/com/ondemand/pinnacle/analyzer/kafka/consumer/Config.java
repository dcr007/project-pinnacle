package com.ondemand.pinnacle.analyzer.kafka.consumer;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 2:43 PM
 * @description
 */
import com.ondemand.pinnacle.analyzer.models.SplunkPayLoad;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.ondemand.pinnacle.analyzer.models.CallStack;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class Config {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.groupId}")
    private String groupId;

    @Value(value = "${kafka.jaas}")
    private String jaas;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //Set these if using SASL authentication or Confluent Cloud
        properties.put("security.protocol", "PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", jaas);
        return properties;
    }
    @Bean
    public ConsumerFactory<String, CallStack> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>( consumerConfig(), new StringDeserializer()
                , new JsonDeserializer(CallStack.class));
    }

    @Bean
    public ConsumerFactory<String, SplunkPayLoad> splunkPayLoadConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>( consumerConfig(), new StringDeserializer()
                , new JsonDeserializer(SplunkPayLoad.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CallStack> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CallStack> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SplunkPayLoad>
    kafkaSplunkPayLoadListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SplunkPayLoad> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(splunkPayLoadConsumerFactory());
        factory.setConcurrency(3);
        return factory;
    }
}
