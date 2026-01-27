package com.foodflow.restaurant_service.service.impl;

import com.foodflow.restaurant_service.dto.OfferRequest;
import com.foodflow.restaurant_service.dto.OfferResponse;
import com.foodflow.restaurant_service.entity.Offer;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.repository.OfferRepository;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public OfferResponse createOffer(Long userId, OfferRequest request) {

        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Offer offer = Offer.builder()
                .restaurantId(restaurant.getId())
                .code(request.getCode())
                .isActive(request.isActive())
                .discountType(request.getDiscountType())
                .title(request.getTitle())
                .description(request.getDescription())
                .discountValue(request.getDiscountValue())
                .maxDiscount(request.getMaxDiscount())
                .minOrderAmount(request.getMinOrderAmount())
                .validFrom(request.getValidFrom())
                .validTill(request.getValidTill())
                .build();

        Offer savedOffer = offerRepository.save(offer);
        return OfferResponse.builder()
                .id(savedOffer.getId())
                .title(savedOffer.getTitle())
                .code(savedOffer.getCode())
                .description(savedOffer.getDescription())
                .discountType(savedOffer.getDiscountType())
                .discountValue(savedOffer.getDiscountValue())
                .maxDiscount(savedOffer.getMaxDiscount())
                .minOrderAmount(savedOffer.getMinOrderAmount())
                .validFrom(savedOffer.getValidFrom())
                .validTill(savedOffer.getValidTill())
                .isActive(savedOffer.getIsActive())
                .build();
    }

    @Override
    public OfferResponse updateOffer(Long userId, Long offerId, OfferRequest request) {
        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Offer offer = offerRepository.findByIdAndRestaurantId(offerId, restaurant.getId());

        offer.setIsActive(request.isActive());
        offer.setDiscountType(request.getDiscountType());
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setDiscountValue(request.getDiscountValue());
        offer.setMaxDiscount(request.getMaxDiscount());
        offer.setMinOrderAmount(request.getMinOrderAmount());
        offer.setValidFrom(request.getValidFrom());
        offer.setValidTill(request.getValidTill());

        Offer savedOffer = offerRepository.save(offer);
        return OfferResponse.builder()
                .id(savedOffer.getId())
                .title(savedOffer.getTitle())
                .code(savedOffer.getCode())
                .description(savedOffer.getDescription())
                .discountType(savedOffer.getDiscountType())
                .discountValue(savedOffer.getDiscountValue())
                .maxDiscount(savedOffer.getMaxDiscount())
                .minOrderAmount(savedOffer.getMinOrderAmount())
                .validFrom(savedOffer.getValidFrom())
                .validTill(savedOffer.getValidTill())
                .isActive(savedOffer.getIsActive())
                .build();
    }

    @Override
    public void deleteOffer(Long userId, Long offerId) {
        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        Offer offer = offerRepository.findByIdAndRestaurantId(offerId, restaurant.getId());

        if(offer == null){
            throw new ResourceNotFoundException("No Valid Offer Found");
        }

        offer.setIsActive(false);
    }

    @Override
    public List<OfferResponse> getOffer(Long userId) {
        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<Offer> offers = offerRepository.findByRestaurantId(restaurant.getId());

        return offers.stream().map(
                offer -> OfferResponse.builder()
                        .id(offer.getId())
                        .code(offer.getCode())
                        .title(offer.getTitle())
                        .description(offer.getDescription())
                        .discountType(offer.getDiscountType())
                        .discountValue(offer.getDiscountValue())
                        .maxDiscount(offer.getMaxDiscount())
                        .minOrderAmount(offer.getMinOrderAmount())
                        .validFrom(offer.getValidFrom())
                        .validTill(offer.getValidTill())
                        .isActive(offer.getIsActive())
                        .build()
        ).toList();
    }
}
