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

import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "customer_Id")
    private String customerId;

    @Column(name = "customer_Address")
    private String customerAddress;

    @Column(name = "customer_number")
    private String customerNumber;

    @Column(name = "customer_Email")
    private String customerEmail;

    @Column(name = "customer_Name")
    private String customerName;

    @Column(name = "company_RegNo")
    private String companyRegNo;


    @Column(name = "customer_Type")
    private String customerType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "systemProfile_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SystemProfile systemProfile;

    @Enumerated
    private CommonStatus commonStatus;



}
