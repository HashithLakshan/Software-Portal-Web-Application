package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RollDto {

    private String rollId;
    private String rollName;
    private CommonStatus commonStatus;


}
