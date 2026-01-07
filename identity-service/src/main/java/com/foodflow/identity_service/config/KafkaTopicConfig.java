package com.foodflow.identity_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic deliveryRoleAssignedTopic(){
        return TopicBuilder.name("delivery-role-assigned")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
