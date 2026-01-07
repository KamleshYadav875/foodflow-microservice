package com.foodflow.delivery_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderPickedUpTopic(){
        return TopicBuilder.name("order-picked-up")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderDeliveredTopic(){
        return TopicBuilder.name("order-delivered")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deliveryPartnerRegisteredTopic(){
        return TopicBuilder.name("delivery-partner-registered")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deliveryPartnerAssigned(){
        return TopicBuilder.name("delivery-partner-assigned")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderPickedUp(){
        return TopicBuilder.name("order-picked-up")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderDelivered(){
        return TopicBuilder.name("order-delivered")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
