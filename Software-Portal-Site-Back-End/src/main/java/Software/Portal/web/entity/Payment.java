package Software.Portal.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
}
