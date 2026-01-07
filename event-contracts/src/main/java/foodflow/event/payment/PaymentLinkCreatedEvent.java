package foodflow.event.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkCreatedEvent {
    private Long orderId;
    private String paymentId;
    private String paymentLink;
}
