package Software.Portal.web.controller;

import Software.Portal.web.dto.SystemFeaturesDto;
import Software.Portal.web.entity.SystemFeatures;
import Software.Portal.web.repository.SystemFeaturesRepository;
import Software.Portal.web.service.SystemFeaturesService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/systemFeatures")
@CrossOrigin
public class SystemFeaturesController {

    private  final SystemFeaturesRepository systemFeaturesRepository;

    private final SystemFeaturesService systemFeaturesService;

    @Autowired
    public SystemFeaturesController(SystemFeaturesRepository systemFeaturesRepository, SystemFeaturesService systemFeaturesService) {
        this.systemFeaturesRepository = systemFeaturesRepository;
        this.systemFeaturesService = systemFeaturesService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    CommonResponse saveFeatures (@RequestBody SystemFeaturesDto systemFeaturesDto){
        return systemFeaturesService.save(systemFeaturesDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public CommonResponse deleteFeature(@RequestParam  String systemFeatureId) {
        CommonResponse commonResponse = new CommonResponse();
        SystemFeatures systemFeatures = systemFeaturesRepository.findById(Long.valueOf(systemFeatureId)).get();
        systemFeaturesRepository.delete(systemFeatures);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Deleted");
        return  commonResponse;
    }

    @GetMapping("/getAllFeaturesinOneProfile/{systemProfilesId}/{commonStatus}")
    CommonResponse getAllFeaturesInOneProfile (@PathVariable String systemProfilesId,@PathVariable String commonStatus){
        return systemFeaturesService.getAllOneProfileFeatures(systemProfilesId,commonStatus);
    }

    @GetMapping("/getBySystemProfileFeatureOne/{systemProfilesId}")
    CommonResponse getByProfileIdFeatures(@PathVariable String systemProfilesId){
        return  systemFeaturesService.getBySystemProfileFeatureOne(systemProfilesId);
    }
}
