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
@Table(name = "subCategorys")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subCategory_Id")
    private  Long subCategoryId;

    @Column(name = "subCategory_Name")
    private String subCategoryName;

    @Column(name = "SubCategory_Discription",length = 2000)
    private String SubCategoryDiscription;

    @Enumerated
    private CommonStatus commonStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_Id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;
}
