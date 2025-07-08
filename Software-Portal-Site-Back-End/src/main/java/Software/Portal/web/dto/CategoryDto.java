package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDto {
     private String categoryId;
    private String categoryName;
    private String categoryDiscription;
    private String saveDate;
    private CommonStatus commonStatus;


}
