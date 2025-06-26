package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubCategoryDto {
    private  String subCategoryId;
    private String subCategoryName;
    private String SubCategoryDiscription;
    private CommonStatus commonStatus;
    private CategoryDto categoryDto;

}
