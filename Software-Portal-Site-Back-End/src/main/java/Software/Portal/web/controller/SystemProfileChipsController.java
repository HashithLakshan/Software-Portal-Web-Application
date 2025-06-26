package Software.Portal.web.controller;

import Software.Portal.web.dto.SystemProfileChipsDto;
import Software.Portal.web.entity.SystemProfileChips;
import Software.Portal.web.repository.SystemProfileChipsRepository;
import Software.Portal.web.service.SystemProfileChipsService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/systemProfileChips")
@CrossOrigin
public class SystemProfileChipsController {

    private final SystemProfileChipsService systemProfileChipsService;

    private  final SystemProfileChipsRepository  systemProfileChipsRepository;

    @Autowired
    public SystemProfileChipsController(SystemProfileChipsService systemProfileChipsService, SystemProfileChipsRepository systemProfileChipsRepository) {
        this.systemProfileChipsService = systemProfileChipsService;
        this.systemProfileChipsRepository = systemProfileChipsRepository;
    }

    @PostMapping("/save")
    CommonResponse SystemProfileChipSave (@RequestBody SystemProfileChipsDto systemProfileChipsDto) {
        return systemProfileChipsService.saveChips(systemProfileChipsDto);
    }

    @DeleteMapping("/delete")
    public CommonResponse deleteChip(@RequestParam  String systemProfileChipId) {
        CommonResponse commonResponse = new CommonResponse();
        SystemProfileChips systemProfileChips = systemProfileChipsRepository.findById(Long.valueOf(systemProfileChipId)).get();
        systemProfileChipsRepository.delete(systemProfileChips);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Deleted");
        return  commonResponse;
    }
    @PutMapping("/deleteChips/{chipId}")
    CommonResponse ChipDelete (@PathVariable String chipId) {
        return systemProfileChipsService.delete(chipId);
    }
    @GetMapping("/getAllOneProfileChips/{systemProfilesId}/{commonStatus}")
    CommonResponse SystemProfileChips (@PathVariable String systemProfilesId, @PathVariable String commonStatus) {
        return systemProfileChipsService.getAllProfilesChipsInOneProfile(systemProfilesId,commonStatus);
    }

}
