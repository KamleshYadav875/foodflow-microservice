package com.foodflow.delivery_service.client;

import com.foodflow.delivery_service.dto.PartnerOrderDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface OrderServiceClient {

    @GetExchange("/internal/partners/{partnerId}/orders/history")
    List<PartnerOrderDetail> getActiveOrdersForPartner(@PathVariable Long partnerId);

    @GetExchange("/internal/partners/{partnerId}/orders/history")
    List<PartnerOrderDetail> getOrdersHistoryForPartner(@PathVariable Long partnerId);
}
