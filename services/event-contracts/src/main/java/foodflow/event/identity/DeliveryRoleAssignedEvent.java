package foodflow.event.identity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRoleAssignedEvent {

    private Long userId;
    private String userName;
}
