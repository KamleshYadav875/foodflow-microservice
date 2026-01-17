package foodflow.event.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSuccessEvent {

    private Long orderId;
    private Long paymentId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
}
