package com.foodflow.delivery_service.service.impl;

import com.foodflow.delivery_service.client.OrderServiceClient;
import com.foodflow.delivery_service.dto.DeliveryPartnerResponseDto;
import com.foodflow.delivery_service.dto.PartnerOrderDetail;
import com.foodflow.delivery_service.dto.PartnerProfileResponseDto;
import com.foodflow.delivery_service.dto.RegisterDeliveryPartnerRequest;
import com.foodflow.delivery_service.entity.DeliveryPartner;
import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery_service.exceptions.BadRequestException;
import com.foodflow.delivery_service.exceptions.ResourceNotFoundException;
import com.foodflow.delivery_service.producer.DeliveryEventProducer;
import com.foodflow.delivery_service.repository.DeliveryPartnerRepository;
import com.foodflow.delivery_service.service.DeliveryPartnerService;
import foodflow.event.delivery.DeliveryPartnerRegisteredEvent;
import foodflow.event.identity.DeliveryRoleAssignedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final DeliveryEventProducer deliveryEventProducer;
    private final OrderServiceClient orderServiceClient;

    @Override
    public DeliveryPartnerResponseDto register(Long userId, RegisterDeliveryPartnerRequest request) {

        if(deliveryPartnerRepository.existsByUserId(userId))
        {
            throw new BadRequestException("User is already registered as delivery partner");
        }

        DeliveryPartner deliveryPartner = DeliveryPartner.builder()
                .userId(userId)
                .vehicleType(request.getVehicleType())
                .availability(DeliveryPartnerAvailability.OFFLINE)
                .city(request.getCity())
                .rating(0.0)
                .totalDeliveries(0)
                .isActive(false)
                .build();

        DeliveryPartner savedPartner = deliveryPartnerRepository.save(deliveryPartner);

        DeliveryPartnerRegisteredEvent event = new DeliveryPartnerRegisteredEvent(
                userId,
                savedPartner.getId(),
                savedPartner.getCity(),
                savedPartner.getVehicleType().name(),
                LocalDateTime.now());
        deliveryEventProducer.publishRegistered(event);

        return DeliveryPartnerResponseDto.builder()
                .deliveryPartnerId(savedPartner.getId())
                .availability(savedPartner.getAvailability())
                .city(savedPartner.getCity())
                .name(savedPartner.getUserName())
                .userId(userId)
                .vehicleType(savedPartner.getVehicleType())
                .build();
    }

    @Override
    public DeliveryPartnerResponseDto updateAvailability(Long partnerId, DeliveryPartnerAvailability availability) {

        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));


        if(availability == DeliveryPartnerAvailability.BUSY){
            throw new BadRequestException("BUSY status is system-controlled");
        }

        partner.setAvailability(availability);
        DeliveryPartner updated = deliveryPartnerRepository.save(partner);

        return DeliveryPartnerResponseDto.builder()
                .deliveryPartnerId(updated.getId())
                .userId(updated.getUserId())
                .city(updated.getCity())
                .name(updated.getUserName())
                .vehicleType(updated.getVehicleType())
                .availability(updated.getAvailability())
                .build();
    }

    @Override
    public PartnerProfileResponseDto getPartnerProfile(Long userId) {

        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not a partner"));

        return PartnerProfileResponseDto.builder()
                .partnerId(partner.getId())
                .name(partner.getUserName())
                .vehicleType(partner.getVehicleType())
                .rating(partner.getRating())
                .city(partner.getCity())
                .joinedAt(partner.getCreatedAt())
                .totalDeliveries(Long.valueOf(partner.getTotalDeliveries()))
                .availability(partner.getAvailability())
                .isActive(partner.getIsActive())
                .build();
    }

    @Override
    public List<PartnerOrderDetail> getActiveOrdersForPartner(Long userId) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user is not a partner"));

        return orderServiceClient.getActiveOrdersForPartner(partner.getId());
    }

    @Override
    public List<PartnerOrderDetail> getOrdersHistoryForPartner(Long userId) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user is not a partner"));

        return orderServiceClient.getOrdersHistoryForPartner(partner.getId());
    }

    @Override
    public void updateStatus(Long userId, Long orderId, String status) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user is not partner"));

        if("DELIVERED".equals(status)){
            partner.setAvailability(DeliveryPartnerAvailability.ONLINE);
            partner.setTotalDeliveries(partner.getTotalDeliveries() + 1);
            deliveryPartnerRepository.save(partner);
            deliveryEventProducer.publishOrderDelivered(orderId, partner.getId());
        }
        else if ("PICKED_UP".equals(status)) {
            deliveryEventProducer.publishOrderPickedUp(orderId, partner.getId());
        }

    }

    @Override
    public void activateDeliveryPartner(DeliveryRoleAssignedEvent event) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(event.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No partner found"));

        partner.setIsActive(true);
        partner.setUserName(event.getUserName());

        deliveryPartnerRepository.save(partner);
    }

}
