package foodflow.event.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReadyEvent {

    private String orderId;
    private Long userId;
    private Long restaurantId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}