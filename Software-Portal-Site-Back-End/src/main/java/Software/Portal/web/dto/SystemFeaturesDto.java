package Software.Portal.web.dto;

import Software.Portal.web.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemFeaturesDto {
    private  String systemFeatureId;
    private String systemFeatureDiscripion;
    private CommonStatus commonStatus;
    private SystemProfileDto systemProfileDto;
}
