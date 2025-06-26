package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.SystemFeaturesDto;
import Software.Portal.web.entity.SystemFeatures;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.repository.SystemFeaturesRepository;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.service.SystemFeaturesService;
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
public class SystemFeaturesServiceImpl implements SystemFeaturesService {

    private final SystemFeaturesRepository systemFeaturesRepository;

    private final SystemProfileService systemProfileService;

    private final SystemProfileRepository systemProfileRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemFeaturesServiceImpl.class);

@Autowired
    public SystemFeaturesServiceImpl(SystemFeaturesRepository systemFeaturesRepository, SystemProfileService systemProfileService, SystemProfileRepository systemProfileRepository) {
        this.systemFeaturesRepository = systemFeaturesRepository;
        this.systemProfileService = systemProfileService;
    this.systemProfileRepository = systemProfileRepository;
}

    @Override
    public CommonResponse save(SystemFeaturesDto systemFeaturesDto) {
    CommonResponse commonResponse = new CommonResponse();
        SystemFeatures systemFeatures;
        try{
            List<String> validationList = this.SystemProfileFeaturesValidation(systemFeaturesDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                commonResponse.setStatus(false);
                return commonResponse;
            }

            Optional<SystemProfile> systemProfileOpt = systemProfileRepository.findById(Long.valueOf(systemFeaturesDto.getSystemProfileDto().getSystemProfilesId()));
            if(systemProfileOpt.isPresent() ) {
                systemFeatures = SystemFeaturesDtoIntoSystemFeatures(systemFeaturesDto);
                systemFeaturesRepository.save(systemFeatures);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("add success");
                commonResponse.setPayload(Collections.singletonList(systemFeatures));
                return commonResponse;
            }else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("You are not fill the step 1 page");
                return commonResponse;
            }

        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in SystemFeaturesService -> save()" + e);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse getAllOneProfileFeatures(String systemProfilesId, String commonStatus) {
    CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        Predicate<SystemFeatures> filterOnStatus = systemFeatures -> systemFeatures.getCommonStatus() == commonStatus1;
        try {
            List<SystemFeatures> systemFeaturesList = systemFeaturesRepository.findBySystemProfileSystemProfilesId((Long.valueOf(systemProfilesId)));
            if (systemFeaturesList == null ) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Features Found");
            } else {

                List<SystemFeaturesDto> systemFeaturesDtoList = systemFeaturesList.stream().filter(filterOnStatus).map(this::castSystemProfileFeaturesIntoSystemProfileFeaturesDto).collect(Collectors.toList());
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(systemFeaturesDtoList));
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Features Found");

            LOGGER.error("/**************** Exception in SystemProfileFeaturesService -> getAll(ActiveSystemProfileFeatures)"+e);

        }
        return commonResponse;
    }

    @Override
    public CommonResponse getBySystemProfileFeatureOne(String systemProfilesId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<SystemFeatures> filterOnStatus = systemFeatures -> systemFeatures.getCommonStatus() != CommonStatus.INACTIVE;
            List<SystemFeatures> systemFeaturesList = systemFeaturesRepository.findBySystemProfileSystemProfilesId((Long.valueOf(systemProfilesId)));
            List<SystemFeaturesDto> systemFeaturesDtoList = systemFeaturesList.stream().filter(filterOnStatus).map(this::castSystemProfileFeaturesIntoSystemProfileFeaturesDto).collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(systemFeaturesDtoList));
            return commonResponse;
        }catch (Exception e){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Features Found");
            LOGGER.error("/**************** Exception in SystemProfileFeaturesService -> getByIdProfile(ActiveSystemProfileFeatures)"+e);

        }
        return commonResponse;
    }



    private SystemFeaturesDto castSystemProfileFeaturesIntoSystemProfileFeaturesDto (SystemFeatures systemFeatures){
       SystemFeaturesDto systemFeaturesDto = new SystemFeaturesDto();
       systemFeaturesDto.setCommonStatus(systemFeatures.getCommonStatus());
       systemFeaturesDto.setSystemFeatureId(String.valueOf(systemFeatures.getSystemFeatureId()));
       systemFeaturesDto.setSystemFeatureDiscripion(systemFeatures.getSystemFeatureDiscripion());
       systemFeaturesDto.setSystemProfileDto(systemProfileService.findDetails(systemFeatures.getSystemProfile().getSystemProfilesId()));

        return systemFeaturesDto;
    }

    private SystemFeatures SystemFeaturesDtoIntoSystemFeatures(SystemFeaturesDto systemFeaturesDto) {
        SystemFeatures systemFeatures = new SystemFeatures();
       systemFeatures.setSystemFeatureDiscripion(systemFeaturesDto.getSystemFeatureDiscripion());
       systemFeatures.setCommonStatus(CommonStatus.ACTIVE);
        systemFeatures.setSystemProfile(systemProfileService.findByProfileId(systemFeaturesDto.getSystemProfileDto().getSystemProfilesId()));
        return systemFeatures;
    }
    private List<String> SystemProfileFeaturesValidation(SystemFeaturesDto systemFeaturesDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(systemFeaturesDto.getSystemFeatureDiscripion()))
            validationList.add("Chip Name field is empty");
        return validationList;
    }
}
