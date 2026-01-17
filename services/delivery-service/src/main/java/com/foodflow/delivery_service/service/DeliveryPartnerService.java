package com.foodflow.delivery_service.service;



import com.foodflow.delivery_service.dto.DeliveryPartnerResponseDto;
import com.foodflow.delivery_service.dto.PartnerOrderDetail;
import com.foodflow.delivery_service.dto.PartnerProfileResponseDto;
import com.foodflow.delivery_service.dto.RegisterDeliveryPartnerRequest;
import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import foodflow.event.identity.DeliveryRoleAssignedEvent;

import java.util.List;

public interface DeliveryPartnerService {

    DeliveryPartnerResponseDto register(Long userId, RegisterDeliveryPartnerRequest request);

    DeliveryPartnerResponseDto updateAvailability(Long partnerId, DeliveryPartnerAvailability availability);

    PartnerProfileResponseDto getPartnerProfile(Long userId);

    List<PartnerOrderDetail> getActiveOrdersForPartner(Long userId);

    List<PartnerOrderDetail> getOrdersHistoryForPartner(Long userId);

    void updateStatus(Long userId, Long orderId, String status);

    void activateDeliveryPartner(DeliveryRoleAssignedEvent event);
}
