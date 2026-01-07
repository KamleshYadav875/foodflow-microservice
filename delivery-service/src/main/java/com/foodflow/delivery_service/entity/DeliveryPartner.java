package com.foodflow.delivery_service.entity;


import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery_service.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_deliveryPartner_availability", columnList = "availability")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName;

    private String city;

    @Enumerated(EnumType.STRING)
    private DeliveryPartnerAvailability availability = DeliveryPartnerAvailability.OFFLINE;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private Integer totalDeliveries;
    private Double rating = 0.0;
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
