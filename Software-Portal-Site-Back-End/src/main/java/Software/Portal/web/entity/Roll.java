package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roll")
public class Roll {
    @Id
    @Column(name = "roll_Id")
    private Long rollId;

    @Enumerated(EnumType.STRING)
    private Roles rollName;

    @Enumerated
    private CommonStatus commonStatus;

}
