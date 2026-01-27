package com.foodflow.restaurant_service.repository;


import com.foodflow.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCity(String city);

    boolean existsByUserId(Long ownerId);

    Optional<Restaurant> findByUserId(Long userId);

    boolean existsByPhone(String phone);
}
