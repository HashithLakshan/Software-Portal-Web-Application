package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
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
@Table(name = "System_Profiles")
public class SystemProfile {
    @Id
    @Column(name = "System_Profile_Id")
    private  Long systemProfilesId;

    @Column(name = "System_Profile_Name")
    private String systemProfilesName;

    @Column(name = "System_Profile_Price")
    private int systemProfilesPrice;

    @Column(name = "system_Profile_Discription",length = 8000)
    private String systemProfilesDiscription;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_Id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_Id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "subCategory_Id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SubCategory subCategory;


    @Enumerated
    private CommonStatus commonStatus;

    @Enumerated
    private RequestStatus requestStatus;

}
