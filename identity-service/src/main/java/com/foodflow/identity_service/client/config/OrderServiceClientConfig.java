package com.foodflow.identity_service.client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceClientConfig {

//    @Bean
//    public OrderServiceClient orderServiceRestClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder){
//        RestClient restClient = restClientBuilder.baseUrl("http://order-service").build();
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//
//        return factory.createClient(OrderServiceClient.class);
//    }
}
