package com.foodflow.restaurant_service.repository;

import com.foodflow.restaurant_service.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByRestaurantId(Long id);

    Offer findByIdAndRestaurantId(Long offerId, Long id);
}
