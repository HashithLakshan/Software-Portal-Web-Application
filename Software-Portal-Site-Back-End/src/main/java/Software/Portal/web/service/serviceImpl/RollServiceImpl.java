package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.constant.Roles;
import Software.Portal.web.dto.RollDto;
import Software.Portal.web.entity.Roll;
import Software.Portal.web.entity.User;
import Software.Portal.web.repository.RollRepository;
import Software.Portal.web.repository.UserRepository;
import Software.Portal.web.service.RollService;
import Software.Portal.web.utill.CommonResponse;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;


@Service
public class RollServiceImpl implements RollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollServiceImpl.class);

    private final RollRepository rollRepository;
    private final UserRepository userRepository;


    @Autowired
    public RollServiceImpl(RollRepository rollRepository, UserRepository userRepository) {
        this.rollRepository = rollRepository;
        this.userRepository = userRepository;
    }





    @Override
    public CommonResponse createRoll(RollDto rollDto) {
    CommonResponse commonResponse = new CommonResponse();
        Roll roll;
        try {
            roll = castSiteRollsDTOIntoSiteRolls(rollDto);
            roll = rollRepository.save(roll);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(roll));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> save()", e);
        }
        return commonResponse;
    }

    @Override
    public Roll findByRollID(String rollId) {
        return   rollRepository.getByRollId(Long.valueOf(rollId));
    }

    @Override
    public RollDto castSiteRollsDTOIntoSiteRolls(Long rollId) {
        Roll roll;
        RollDto rollDto = new RollDto();
        roll = rollRepository.getByRollId(rollId);
        rollDto.setRollId(String.valueOf(roll.getRollId()));
        rollDto.setRollName(String.valueOf(roll.getRollName()));
        rollDto.setCommonStatus(roll.getCommonStatus());
        return rollDto;    }




    private Roll castSiteRollsDTOIntoSiteRolls(RollDto rollDto) {
        Roll roll = new Roll();
        roll.setRollName(Roles.valueOf(rollDto.getRollName()));
        roll.setCommonStatus(rollDto.getCommonStatus());
        return roll;
    }
}
