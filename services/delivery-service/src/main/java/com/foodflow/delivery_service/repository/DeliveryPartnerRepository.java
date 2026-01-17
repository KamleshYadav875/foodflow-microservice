package com.foodflow.delivery_service.repository;

import com.foodflow.delivery_service.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    boolean existsByUserId(Long user);

    List<DeliveryPartner> findOnlineByCity(String city);

   Optional<DeliveryPartner> findByUserId(Long user);

}
