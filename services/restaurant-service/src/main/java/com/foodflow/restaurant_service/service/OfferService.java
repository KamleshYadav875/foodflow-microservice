package com.foodflow.restaurant_service.service;

import com.foodflow.restaurant_service.dto.OfferRequest;
import com.foodflow.restaurant_service.dto.OfferResponse;

import java.util.List;

public interface OfferService {
    OfferResponse createOffer(Long userId, OfferRequest request);

    OfferResponse updateOffer(Long userId, Long offerId, OfferRequest request);

    void deleteOffer(Long userId, Long offerId);

    List<OfferResponse> getOffer(Long userId);
}
