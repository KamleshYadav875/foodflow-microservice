package com.foodflow.restaurant_service.client.config;

import com.foodflow.restaurant_service.client.MediaServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class MediaServiceClientConfig {

    @Bean
    public MediaServiceClient mediaServiceClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder.baseUrl("http://media-service").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(MediaServiceClient.class);
    }
}
