package Software.Portal.web.service;

import Software.Portal.web.dto.RollDto;
import Software.Portal.web.entity.Roll;
import Software.Portal.web.utill.CommonResponse;

public interface RollService {
    CommonResponse createRoll(RollDto rollDto);

    Roll findByRollID(String rollId);


    RollDto castSiteRollsDTOIntoSiteRolls(Long rollId);



}
