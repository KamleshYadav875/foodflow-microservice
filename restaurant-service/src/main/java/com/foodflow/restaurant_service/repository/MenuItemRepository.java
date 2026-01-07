package com.foodflow.restaurant_service.repository;

import com.foodflow.restaurant_service.entity.MenuItems;
import com.foodflow.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItems, Long> {


    List<MenuItems> findByRestaurant(Restaurant restaurant);

    List<MenuItems> findByRestaurantAndIsAvailableTrue(Restaurant restaurant);

    long countByRestaurant(Restaurant restaurant);

    long countByRestaurantAndIsAvailableTrue(Restaurant restaurant);
}
