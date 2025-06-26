package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "System_Profile_Chips")
public class SystemProfileChips {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "system_Profile_Chip_Id")
    private  Long systemProfileChipId;

    @Column(name = "chip_Name")
    private String chipName;

    @Enumerated
    private CommonStatus commonStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "system_Profile_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SystemProfile systemProfile;
}
