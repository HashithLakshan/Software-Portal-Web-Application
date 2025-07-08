package Software.Portal.web.service;

import Software.Portal.web.dto.SystemFeaturesDto;
import Software.Portal.web.utill.CommonResponse;

public interface SystemFeaturesService {
    CommonResponse save(SystemFeaturesDto systemFeaturesDto);

    CommonResponse getAllOneProfileFeatures(String systemProfilesId, String commonStatus);

    CommonResponse getBySystemProfileFeatureOne(String systemProfilesId);


}
