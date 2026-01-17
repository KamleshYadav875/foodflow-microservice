package com.foodflow.delivery_service.strategy.impl;

import com.foodflow.delivery_service.entity.DeliveryPartner;
import com.foodflow.delivery_service.entity.OrderDeliveryAssignment;
import com.foodflow.delivery_service.enums.DeliveryAssignmentStatus;
import com.foodflow.delivery_service.repository.DeliveryPartnerRepository;
import com.foodflow.delivery_service.repository.OrderDeliveryAssignmentRepository;
import com.foodflow.delivery_service.strategy.DeliveryAssignmentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityBasedBroadcastStrategy implements DeliveryAssignmentStrategy {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderDeliveryAssignmentRepository deliveryAssignmentRepository;
    @Override
    public void broadcast(Long orderId) {

        List<DeliveryPartner> partners = deliveryPartnerRepository.findOnlineByCity("Gurugram");

        for (DeliveryPartner partner : partners) {

            OrderDeliveryAssignment assignment =
                    OrderDeliveryAssignment.builder()
                            .orderId(orderId)
                            .deliveryPartner(partner)
                            .status(DeliveryAssignmentStatus.PENDING)
                            .build();

            deliveryAssignmentRepository.save(assignment);

            log.info("Broadcast order {} to partner {}", orderId, partner.getId());
        }
    }
}
