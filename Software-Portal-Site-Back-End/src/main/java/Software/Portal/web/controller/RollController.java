package Software.Portal.web.controller;

import Software.Portal.web.dto.RollDto;
import Software.Portal.web.service.RollService;

import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/rolls")
@CrossOrigin
public class RollController {

  private final RollService rollService;

    @Autowired
    public RollController(RollService rollService) {
        this.rollService = rollService;
    }
    @PostMapping("/saveSiteRolls")
    public CommonResponse saveSystemRolls(@RequestBody RollDto rollDto) {
        return rollService.createRoll(rollDto);
    }



}
