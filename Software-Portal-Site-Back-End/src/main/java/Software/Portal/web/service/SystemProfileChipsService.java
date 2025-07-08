package Software.Portal.web.service;

import Software.Portal.web.dto.SystemProfileChipsDto;
import Software.Portal.web.utill.CommonResponse;

public interface SystemProfileChipsService {
    CommonResponse saveChips(SystemProfileChipsDto systemProfileChipsDto);

    CommonResponse delete(String chipId);

    CommonResponse getAllProfilesChipsInOneProfile(String systemProfilesId,String commonStatus);
}
