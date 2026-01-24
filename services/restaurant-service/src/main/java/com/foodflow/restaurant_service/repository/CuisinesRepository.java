package com.foodflow.restaurant_service.repository;

import com.foodflow.restaurant_service.entity.Cuisines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuisinesRepository extends JpaRepository<Cuisines, Long> {
    Cuisines findByIdAndRestaurantId(Long cuisineId, Long id);

    List<Cuisines> findByRestaurantId(Long id);
}
