package com.foodflow.customer_service.client;

import com.foodflow.customer_service.dto.RestaurantCardDto;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface RestaurantServiceClient {

    @GetExchange("/restaurant/internal")
    List<RestaurantCardDto> getRestaurantByCity(@RequestParam("city") String city);
}
