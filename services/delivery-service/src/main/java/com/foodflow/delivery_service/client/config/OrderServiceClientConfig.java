package com.foodflow.delivery_service.client.config;

import com.foodflow.delivery_service.client.OrderServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OrderServiceClientConfig {

    @Bean
    public OrderServiceClient orderServiceClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder){
        RestClient restClient = builder
                .baseUrl("http://order-service")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OrderServiceClient.class);
    }
}
