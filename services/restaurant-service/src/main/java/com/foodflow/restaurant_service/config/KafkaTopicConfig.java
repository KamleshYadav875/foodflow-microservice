package com.foodflow.restaurant_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderConfirmedTopic(){
        return TopicBuilder.name("order-confirmed")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean NewTopic orderPreparingTopic(){
        return TopicBuilder.name("order-preparing")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean NewTopic orderReadyTopic(){
        return TopicBuilder.name("order-ready")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
