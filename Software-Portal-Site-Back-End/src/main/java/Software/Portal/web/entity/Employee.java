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
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "employee_Id")
    private String employeeId;

    @Column(name = "company_Email")
    private String companyEmail;


    @Column(name = "employee_Number")
    private String employeeNumber;

//    @Column(name = "gender")
//    private String gender;

    @Column(name = "company_RegNo")
    private String companyRgNo;

    @Column(name = "company_Name")
    private String companyName;

    @Column(name = "employee_NIC")
    private String employeeNIC;

  @Enumerated
  private RequestStatus requestStatus;

    @Enumerated
    private CommonStatus commonStatus;



}
