package com.foodflow.identity_service.client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MediaServiceClientConfig {

//    @Bean
//    public MediaServiceClient mediaServiceClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder){
//        RestClient restClient = restClientBuilder.baseUrl("http://media-service").build();
//
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//
//        return factory.createClient(MediaServiceClient.class);
//    }
}
