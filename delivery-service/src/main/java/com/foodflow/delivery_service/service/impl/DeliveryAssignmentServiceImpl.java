package com.foodflow.delivery_service.service.impl;


import com.foodflow.delivery_service.entity.DeliveryPartner;
import com.foodflow.delivery_service.entity.OrderDeliveryAssignment;
import com.foodflow.delivery_service.enums.DeliveryAssignmentStatus;
import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery_service.exceptions.BadRequestException;
import com.foodflow.delivery_service.exceptions.ResourceNotFoundException;
import com.foodflow.delivery_service.producer.DeliveryEventProducer;
import com.foodflow.delivery_service.repository.DeliveryPartnerRepository;
import com.foodflow.delivery_service.repository.OrderDeliveryAssignmentRepository;
import com.foodflow.delivery_service.service.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderDeliveryAssignmentRepository assignmentRepository;
    private final DeliveryEventProducer deliveryEventProducer;
    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long userId) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));

        OrderDeliveryAssignment assignment =
                assignmentRepository
                        .findByOrderIdAndDeliveryPartnerId(orderId, partner.getId())
                        .orElseThrow(() -> new BadRequestException("Offer expired"));

        if (assignment.getStatus() != DeliveryAssignmentStatus.PENDING) {
            throw new BadRequestException("Offer already processed");
        }

        assignment.setStatus(DeliveryAssignmentStatus.ACCEPTED);
        assignmentRepository.rejectOtherAssignments(orderId, partner.getId());

        partner.setAvailability(DeliveryPartnerAvailability.BUSY);

        deliveryEventProducer.publishDeliveryPartnerAssigned(orderId, partner.getId());
    }
}
