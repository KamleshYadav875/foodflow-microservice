package com.foodflow.customer_service.client.config;

import com.foodflow.customer_service.client.RestaurantServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestaurantServiceClientConfig {

    @Bean
    public RestaurantServiceClient restaurantServiceRestClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder.baseUrl("http://restaurant-service").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(RestaurantServiceClient.class);
    }
}
