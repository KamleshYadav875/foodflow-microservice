package foodflow.event.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerRegisteredEvent {

    private Long userId;
    private Long deliveryPartnerId;
    private String city;
    private String vehicleType;
    private LocalDateTime createdAt;

}
