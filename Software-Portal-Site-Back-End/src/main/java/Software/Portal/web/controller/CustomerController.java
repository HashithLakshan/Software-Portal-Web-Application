package Software.Portal.web.controller;

import Software.Portal.web.dto.CustomerDto;
import Software.Portal.web.service.CustomerService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer")
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

@Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/save/customer")
    CommonResponse saveUser (@RequestBody CustomerDto customerDto) {
        return customerService.saveUser(customerDto);
    }
}
