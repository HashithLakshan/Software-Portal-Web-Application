package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemProfileChipsDto {
    private  String systemProfileChipId;
    private String chipName;
    private CommonStatus commonStatus;
    private SystemProfileDto systemProfileDto;
}
