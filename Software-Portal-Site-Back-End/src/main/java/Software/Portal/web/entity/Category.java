package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "categorys")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_Id")
    private Long categoryId;

    @Column(name = "category_Name")
    private String categoryName;

    @Column(name = "save_Date")
    private LocalDate saveDate;


    @Column(name = "category_Discription",length = 2000)
    private String categoryDiscription;

    @Enumerated
    private CommonStatus commonStatus;

}
