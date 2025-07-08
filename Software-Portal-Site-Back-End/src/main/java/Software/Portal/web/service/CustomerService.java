package Software.Portal.web.service;

import Software.Portal.web.dto.CustomerDto;
import Software.Portal.web.entity.Customer;
import Software.Portal.web.utill.CommonResponse;

public interface CustomerService {

    CommonResponse saveUser(CustomerDto customerDto);

    Customer findById(String customerId);

    CustomerDto findByCustomerDetails(String customerId);
}
