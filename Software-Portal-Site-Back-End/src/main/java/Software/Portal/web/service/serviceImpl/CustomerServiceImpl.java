package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.CustomerDto;
import Software.Portal.web.entity.Customer;
import Software.Portal.web.repository.CustomerRepository;
import Software.Portal.web.service.CustomerService;
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

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private  final SystemProfileService systemProfileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

@Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, SystemProfileService systemProfileService) {
        this.customerRepository = customerRepository;
    this.systemProfileService = systemProfileService;
}

    @Override
    public CommonResponse saveUser(CustomerDto customerDto) {
        CommonResponse commonResponse = new CommonResponse();
        Customer customer;
        try{
            List<String> validationList = this.CustomerValidation(customerDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                commonResponse.setStatus(false);
                return commonResponse;
            }
            customer = CustomerDtoIntoCustomer(customerDto);
           customerRepository.save(customer);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Successfully registered Customer");
            commonResponse.setPayload(Collections.singletonList(customer));


        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;

    }

    @Override
    public Customer findById(String customerId) {
        return customerRepository.findById(customerId).get();
    }

    @Override
    public CustomerDto findByCustomerDetails(String customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        CustomerDto customerDto;
        customerDto = CustomerIntoCustomerDto(customer);
        return customerDto;

    }
    private CustomerDto CustomerIntoCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setCustomerAddress(customer.getCustomerAddress());
        customerDto.setCompanyRegNo(customer.getCompanyRegNo());
        customerDto.setCustomerEmail(customer.getCustomerEmail());
        customerDto.setCustomerNumber(customer.getCustomerNumber());
        customerDto.setCustomerType(customer.getCustomerType());
        customerDto.setCustomerName(customer.getCustomerName());
        return customerDto;
    }

    private Customer CustomerDtoIntoCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
       customer.setCustomerId(customerDto.getCustomerId());
       customer.setCustomerAddress(customerDto.getCustomerAddress());
       customer.setCompanyRegNo(customerDto.getCompanyRegNo());
       customer.setCustomerEmail(customerDto.getCustomerEmail());
       customer.setCustomerNumber(customerDto.getCustomerNumber());
       customer.setCustomerType(customerDto.getCustomerType());
       customer.setCommonStatus(CommonStatus.ACTIVE);
       customer.setCustomerName(customerDto.getCustomerName());
        customer.setSystemProfile(systemProfileService.findByProfileId(customerDto.getSystemProfileDto().getSystemProfilesId()));
        return customer;
    }

    private List<String> CustomerValidation(CustomerDto customerDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(customerDto.getCustomerName()))
            validationList.add("name field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(customerDto.getCustomerNumber())))
            validationList.add("Contact Number field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(customerDto.getCustomerAddress())))
            validationList.add("Address field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(customerDto.getCustomerType())))
            validationList.add("You are not Select Type is empty");
        return validationList;
    }
}
