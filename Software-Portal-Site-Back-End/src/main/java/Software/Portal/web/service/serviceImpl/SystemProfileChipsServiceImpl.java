package Software.Portal.web.service.serviceImpl;


import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.SystemProfileChipsDto;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.entity.SystemProfileChips;
import Software.Portal.web.repository.SystemProfileChipsRepository;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.service.SystemProfileChipsService;
import Software.Portal.web.service.SystemProfileService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SystemProfileChipsServiceImpl implements SystemProfileChipsService {

    private final SystemProfileChipsRepository systemProfileChipsRepository;

    private final SystemProfileService systemProfileService;

    private final SystemProfileRepository systemProfileRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemProfileChipsServiceImpl.class);

    @Autowired
    public SystemProfileChipsServiceImpl(SystemProfileChipsRepository systemProfileChipsRepository, SystemProfileService systemProfileService, SystemProfileRepository systemProfileRepository) {
        this.systemProfileChipsRepository = systemProfileChipsRepository;
        this.systemProfileService = systemProfileService;
        this.systemProfileRepository = systemProfileRepository;
    }

    @Override
    public CommonResponse saveChips(SystemProfileChipsDto systemProfileChipsDto) {
        CommonResponse commonResponse = new CommonResponse();
        SystemProfileChips systemProfileChips;
        try{
            List<String> validationList = this.SystemProfileChipsValidation(systemProfileChipsDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                commonResponse.setStatus(false);
                return commonResponse;
            }
            Optional<SystemProfile> systemProfileOpt = systemProfileRepository.findById(Long.valueOf(systemProfileChipsDto.getSystemProfileDto().getSystemProfilesId()));
if(systemProfileOpt.isPresent()){
    systemProfileChips = CategoryDtoIntoCategory(systemProfileChipsDto);
    systemProfileChipsRepository.save(systemProfileChips);
    commonResponse.setStatus(true);
    commonResponse.setCommonMessage("Add Success");
    commonResponse.setPayload(Collections.singletonList(systemProfileChips));
}else{
    commonResponse.setStatus(false);
    commonResponse.setCommonMessage("You are not fill the step 1 page");
    return commonResponse;
}




        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in SystemProfileChipsService -> save()" + e);
        }


        return commonResponse;
    }

    @Override
    public CommonResponse delete(String chipId) {
        CommonResponse commonResponse = new CommonResponse();
    try {
        SystemProfileChips systemProfileChips;
        systemProfileChips = systemProfileChipsRepository.findById(Long.valueOf(chipId)).get();
        systemProfileChipsRepository.delete(systemProfileChips);
        commonResponse.setStatus(true);
        commonResponse.setPayload(Collections.singletonList(systemProfileChips));
        return commonResponse;
    }catch (Exception e){
        commonResponse.setStatus(false);
        LOGGER.error("/***************** Exception in SystemProfileChipsService -> delete()" + e);
    }
    return commonResponse;
    }

    @Override
    public CommonResponse getAllProfilesChipsInOneProfile(String systemProfilesId,String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        Predicate<SystemProfileChips> filterOnStatus = systemProfileChips   -> systemProfileChips.getCommonStatus() == commonStatus1;
        try {
            List<SystemProfileChips> systemProfileChipsList = systemProfileChipsRepository.findBySystemProfileSystemProfilesId((Long.valueOf(systemProfilesId)));
            if (systemProfileChipsList == null ) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Chips Found");
            } else {

                List<SystemProfileChipsDto> systemProfileChipsDtoList = systemProfileChipsList.stream().filter(filterOnStatus).map(this::castSystemProfileChipsIntoSystemProfileChipsDto).collect(Collectors.toList());
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(systemProfileChipsDtoList));
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Chips Found");

            LOGGER.error("/**************** Exception in SystemProfileChipsService -> getAll(ActiveSystemProfileChips)"+e);

        }
        return commonResponse;
    }

    private SystemProfileChipsDto castSystemProfileChipsIntoSystemProfileChipsDto (SystemProfileChips systemProfileChips){
        SystemProfileChipsDto systemProfileChipsDto = new SystemProfileChipsDto();
        systemProfileChipsDto.setChipName(systemProfileChips.getChipName());
        systemProfileChipsDto.setSystemProfileChipId(String.valueOf(systemProfileChips.getSystemProfileChipId()));
        systemProfileChipsDto.setCommonStatus(systemProfileChips.getCommonStatus());
        systemProfileChipsDto.setSystemProfileDto(systemProfileService.findDetails(systemProfileChips.getSystemProfile().getSystemProfilesId()));
        return systemProfileChipsDto;
    }

    private SystemProfileChips CategoryDtoIntoCategory(SystemProfileChipsDto systemProfileChipsDto) {
        SystemProfileChips systemProfileChips = new SystemProfileChips();
        systemProfileChips.setChipName(systemProfileChipsDto.getChipName());
        systemProfileChips.setCommonStatus(CommonStatus.ACTIVE);
        systemProfileChips.setSystemProfile(systemProfileService.findByProfileId(systemProfileChipsDto.getSystemProfileDto().getSystemProfilesId()));
        return systemProfileChips;
    }
    private List<String> SystemProfileChipsValidation(SystemProfileChipsDto systemProfileChipsDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(systemProfileChipsDto.getChipName()))
            validationList.add("Chip Name field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(systemProfileChipsDto.getSystemProfileDto().getSystemProfilesId())))
            validationList.add("Firstly Create a Profile");
        return validationList;
    }
}
