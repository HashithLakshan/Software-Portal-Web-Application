package Software.Portal.web.controller;

import Software.Portal.web.dto.EmployeeDto;
import Software.Portal.web.service.EmployeeService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/employee")
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;


    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @PostMapping("/save/employee")
    CommonResponse saveUser(@RequestParam String rollName, @RequestBody EmployeeDto employeeDto) {
        return employeeService.saveUser(rollName,employeeDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllFilterStatus/{commonStatus}/{requestStatus}")
    CommonResponse getAllFilterStatus(@PathVariable String commonStatus, @PathVariable String requestStatus) {
        return employeeService.getAllFilter(commonStatus, requestStatus);
    }

   @GetMapping("/filtered")
    public CommonResponse getFilteredEmployees(
            @RequestParam(required = false) String commonStatus,
            @RequestParam(required = false) String requestStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

       return employeeService.getFilteredEmployees(commonStatus, requestStatus, page, size);
   }


   @PreAuthorize("hasRole('ROLE_ADMIN')")
   @GetMapping("/getDetailEmployeeId")
    public CommonResponse getDetailsInId(@RequestParam String commonStatus, @RequestParam String requestStatus,
                                         @RequestParam String employeeId) {
        return employeeService.getDetailsInId(commonStatus,requestStatus,employeeId);
   }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailEmployeeUserName")
    public CommonResponse getDetailsInUserName(@RequestParam String commonStatus, @RequestParam String requestStatus,
                                               @RequestParam String companyName) {
        return employeeService.getDetailsInUserName(commonStatus,requestStatus,companyName);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStatus")
    public CommonResponse updateEmployeeStatus(@RequestParam String employeeId,@RequestParam String requestStatus,@RequestParam String commonStatus) {
        return employeeService.updateStatus(employeeId,requestStatus,commonStatus);
    }
}

