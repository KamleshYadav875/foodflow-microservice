package foodflow.event.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerAssignedEvent {
    private Long orderId;
    private Long partnerId;
}
