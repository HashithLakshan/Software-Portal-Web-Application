package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.EmployeeDto;
import Software.Portal.web.entity.Employee;
import Software.Portal.web.entity.User;
import Software.Portal.web.repository.EmployeeRepository;
import Software.Portal.web.repository.UserRepository;
import Software.Portal.web.service.EmployeeService;
import Software.Portal.web.service.UserService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private  final EmployeeRepository employeeRepository;

    private  final UserService userService;

    private final UserRepository userRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Override
    public CommonResponse

    saveUser(String rollName,EmployeeDto employeeDto) {

        CommonResponse commonResponse = new CommonResponse();
        Employee employee;
        boolean x = CommonValidation.isValidInput(employeeDto.getEmployeeNIC());
        boolean y = CommonValidation.isValidNumber(employeeDto.getEmployeeNumber());
        try{
            List<String> validationList = this.CustomerValidation(employeeDto);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            if(x == false){
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("You Enter NIC Number is not valid");
                return commonResponse;
            }
            if(y == false){
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("You Enter Contact Number is not valid");
                return commonResponse;
            }

          Optional < Employee> employee1 =  employeeRepository.findById(employeeDto.getEmployeeId());

          if  (employee1.isPresent()){
                Employee employee2 = employeeRepository.findById(employeeDto.getEmployeeId()).get();

                
             if(employeeDto.getEmployeeNIC().equals(employee2.getEmployeeNIC()) && employeeDto.getEmployeeNumber().equals(employee2.getEmployeeNumber())
             && employeeDto.getCompanyRgNo().equals(employee2.getCompanyRgNo())&& employeeDto.getCompanyName().equals(employee2.getCompanyName())
              && employeeDto.getCompanyEmail().equals(employee2.getCompanyEmail())){
                 commonResponse.setStatus(false);
                 commonResponse.setCommonMessage("you are not any changes to update ");
                 return commonResponse;

             }else{

                 employee2.setEmployeeNIC(employeeDto.getEmployeeNIC());
                 employee2.setEmployeeNumber(employeeDto.getEmployeeNumber());
                 employee2.setCompanyRgNo(employeeDto.getCompanyRgNo());
                 employee2.setCompanyName(employeeDto.getCompanyName());
                 employee2.setCompanyEmail(employeeDto.getCompanyEmail());
                 employeeRepository.save(employee2);
                 commonResponse.setStatus(true);
                 commonResponse.setCommonMessage("Employee Id " +employeeDto.getEmployeeId()+  " Updated");
                 return commonResponse;
             }

            } else  {



                if (rollName.equals("superAdmin")) {
                    Optional <Employee> employee2 = employeeRepository.findByCompanyEmailAndCommonStatusAndRequestStatus(employeeDto.getCompanyEmail(),CommonStatus.ACTIVE,RequestStatus.APPROVED);
                    Optional <Employee> employee3 = employeeRepository.findByCompanyEmailAndCommonStatusAndRequestStatus(employeeDto.getCompanyEmail(),CommonStatus.ACTIVE,RequestStatus.PENDING);
                    if (employee2.isPresent() || employee3.isPresent()) {
                        commonResponse.setStatus(false);
                        commonResponse.setCommonMessage("this email already taken someOne");
                        return commonResponse;
                    }else {
                    employee = EmployeeDtoIntoEmployee(employeeDto);
                    employee.setRequestStatus(RequestStatus.APPROVED);
                    employeeRepository.save(employee);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Successfully registered approved Employee");
                    commonResponse.setPayload(Collections.singletonList(employee));
                    return commonResponse;
                    }
                } else {
                    Optional <Employee> employee2 = employeeRepository.findByCompanyEmailAndCommonStatusAndRequestStatus(employeeDto.getCompanyEmail(),CommonStatus.ACTIVE,RequestStatus.APPROVED);
                    Optional <Employee> employee3 = employeeRepository.findByCompanyEmailAndCommonStatusAndRequestStatus(employeeDto.getCompanyEmail(),CommonStatus.ACTIVE,RequestStatus.PENDING);
                    if (employee2.isPresent() || employee3.isPresent()) {
                        commonResponse.setStatus(false);
                        commonResponse.setCommonMessage("this email already taken someOne");
                        return commonResponse;
                    }else {
                        employee = EmployeeDtoIntoEmployee(employeeDto);
                        employee.setRequestStatus(RequestStatus.PENDING);
                        employeeRepository.save(employee);
                        commonResponse.setStatus(true);
                        commonResponse.setCommonMessage("We contact you soon !!");
                        commonResponse.setPayload(Collections.singletonList(employee));
                        return commonResponse;
                    }
                }
            }


        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;

    }



    @Override
    public CommonResponse getAllFilter(String commonStatus, String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        try {
            // Convert string to enum values
            RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
            CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);



            // Query the repository with filters
            List<Employee> employeeList = employeeRepository
                    .findByCommonStatusAndRequestStatus(commonStatus1,requestStatus1);

            // Convert SystemProfile to SystemProfileDto
            employeeDtoList = employeeList.stream()
                    .map(this::EmployeeDtoIntoEmployeeDto)
                    .collect(Collectors.toList());

            // Check if the list is empty and set the response accordingly
            if(employeeDtoList.isEmpty()) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Employee found with the given filters.");
            } else {
                commonResponse.setStatus(true);
                commonResponse.setPayload(Collections.singletonList(employeeDtoList));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid status  provided: " + e);
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            LOGGER.error("Exception in EmployeeService -> getAllActiveEmployeePendingAndApprovedCategory: " + e);
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching the employee.");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse getFilteredEmployees(String commonStatus, String requestStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        Page<Employee> employeePage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                employeePage = employeeRepository.findByCommonStatus(commonStatusEnum, pageable);
            } else {
                employeePage = employeeRepository.findByCommonStatusAndRequestStatus(commonStatusEnum, requestStatusEnum, pageable);
            }
            

            // Convert Employee entities to DTOs
            employeeDtoList = employeePage.getContent().stream()
                    .map(this::EmployeeDtoIntoEmployeeDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", employeePage.getNumber());
            paginationDetails.put("totalItems", employeePage.getTotalElements());
            paginationDetails.put("totalPages", employeePage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Employees fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(employeeDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching employees.");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse getDetailsInId(String commonStatus, String requestStatus, String employeeId) {
        CommonResponse commonResponse = new CommonResponse();
        Employee employee;
        CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatusEnum = null;


try {
    if (employeeId != null && !employeeId.isEmpty()) {
        if (requestStatus != null && !requestStatus.isEmpty()) {
            requestStatusEnum = RequestStatus.valueOf(requestStatus);
        }

        if (commonStatus.equals("INACTIVE")) {
            employee = employeeRepository.findByCommonStatusAndEmployeeId(commonStatusEnum,employeeId);
        } else {
            employee = employeeRepository.findByCommonStatusAndRequestStatusAndEmployeeId(commonStatusEnum, requestStatusEnum, employeeId);
        }

        if (employee == null) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Employee found with the given Employee Id.");
            return commonResponse;
        }

        EmployeeDto employeeDto = EmployeeDtoIntoEmployeeDto(employee);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Employees fetched successfully.");
        commonResponse.setPayload(Collections.singletonList(employeeDto));
        return commonResponse;
    } else {
        commonResponse.setStatus(false);
        commonResponse.setCommonMessage("No Employee found with the given Employee ID.");
        return commonResponse;
    }
} catch (Exception e) {
    commonResponse.setStatus(false);
    commonResponse.setCommonMessage("An error occurred while fetching the employee.");
    LOGGER.error("Exception in EmployeeService -> getIdActiveEmployeePendingAndApproved: " + e);

}
return commonResponse;
    }

    @Override
    public CommonResponse getDetailsInUserName(String commonStatus, String requestStatus, String companyName) {
        CommonResponse commonResponse = new CommonResponse();
        Employee employee;
        CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatusEnum = null;


        try {
            if (companyName != null && !companyName.isEmpty()) {
                if (requestStatus != null && !requestStatus.isEmpty()) {
                    requestStatusEnum = RequestStatus.valueOf(requestStatus);
                }

                if (commonStatus.equals("INACTIVE")) {
                    employee = employeeRepository.findByCommonStatusAndCompanyName(commonStatusEnum,companyName);
                } else {
                    employee = employeeRepository.findByCommonStatusAndRequestStatusAndCompanyName(commonStatusEnum, requestStatusEnum, companyName);
                }

                if (employee == null) {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Employee found with the given Company Name.");
                    return commonResponse;
                }

                EmployeeDto employeeDto = EmployeeDtoIntoEmployeeDto(employee);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Employees fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(employeeDto));
                return commonResponse;
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Employee found with the given Company Name.");
                return commonResponse;
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching the employee.");
            LOGGER.error("Exception in EmployeeService -> getUserNameActiveEmployeePendingAndApprovedCategory: " + e);

        }
        return commonResponse;
    }

    @Override
    public CommonResponse updateStatus(String employeeId,String requestStatus,String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        Employee employee;

        if (employeeId != null && !employeeId.isEmpty()) {
            employee = employeeRepository.findById(employeeId).get();
            if(commonStatus.equals("ACTIVE")) {
                if (employee != null) {
                 employee = employeeRepository.findById(employeeId).get();
                 employee.setCommonStatus(CommonStatus.ACTIVE);
                    employeeRepository.save(employee);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Employees successfully recovered.");
                    commonResponse.setPayload(Collections.singletonList(employee));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Employee found with the given Employee Id.");
                    return commonResponse;
                }
            } else if (commonStatus.equals("INACTIVE")) {
                if (employee != null) {
                    employee = employeeRepository.findById(employeeId).get();
                    employee.setCommonStatus(CommonStatus.INACTIVE);
                    employeeRepository.save(employee);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Employees successfully deleted.");
                    commonResponse.setPayload(Collections.singletonList(employee));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Employee found with the given Employee Id.");
                    return commonResponse;
                }
            } else if (requestStatus.equals("APPROVED")) {
                if (employee != null) {
                    employee = employeeRepository.findById(employeeId).get();
                    employee.setRequestStatus(RequestStatus.APPROVED);
                    employeeRepository.save(employee);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Employees approved now.");
                    commonResponse.setPayload(Collections.singletonList(employee));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Employee found with the given Employee Id.");
                    return commonResponse;
                }
            }
            else if (commonStatus.equals("DELETED")) {
                if (employee != null) {
                    employee = employeeRepository.findById(employeeId).get();
                    employeeRepository.delete(employee);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Employees permanent deleted now.");
                    commonResponse.setPayload(Collections.singletonList(employee));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Employee found with the given Employee Id.");
                    return commonResponse;
                }
            }
        }else{
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage(" Employee ID Field empty");
            return commonResponse;
        }
        return commonResponse;
    }

    @Override
    public Employee findByemployeeId(String employeeId) {

        return  employeeRepository.findById(employeeId).get();
    }

    @Override
    public EmployeeDto findById(String employeeId) {
        Employee  employee = employeeRepository.findById(employeeId).get();
        EmployeeDto employeeDto;
        employeeDto = EmployeeDtoIntoEmployeeDto(employee);
        return employeeDto;
    }


    private Employee EmployeeDtoIntoEmployee(EmployeeDto EmployeeDto) {
        Employee employee = new Employee();
       employee.setEmployeeId(EmployeeDto.getEmployeeId());
       employee.setEmployeeNIC(EmployeeDto.getEmployeeNIC());
       employee.setEmployeeNumber(EmployeeDto.getEmployeeNumber());
        employee.setCompanyRgNo(EmployeeDto.getCompanyRgNo());
       employee.setCompanyName(EmployeeDto.getCompanyName());
       employee.setCommonStatus(EmployeeDto.getCommonStatus());
       employee.setCompanyEmail(EmployeeDto.getCompanyEmail());
        return employee;
    }
private EmployeeDto EmployeeDtoIntoEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employee.getEmployeeId());
        employeeDto.setEmployeeNIC(employee.getEmployeeNIC());
        employeeDto.setEmployeeNumber(employee.getEmployeeNumber());
        employeeDto.setCompanyRgNo(employee.getCompanyRgNo());
        employeeDto.setCompanyName(employee.getCompanyName());
        employeeDto.setCommonStatus(employee.getCommonStatus());
        employeeDto.setRequestStatus(employee.getRequestStatus());
        employeeDto.setCompanyEmail(employee.getCompanyEmail());
        return employeeDto;
}



    private List<String> CustomerValidation(EmployeeDto employeeDto) {
        List<String> validationList = new ArrayList<>();
//        if (CommonValidation.stringNullValidation(employeeDto.getGender()))
//            validationList.add("gender selection is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(employeeDto.getEmployeeNumber())))
            validationList.add("Contact Number field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(employeeDto.getEmployeeNIC())))
            validationList.add("Date of Birth field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(employeeDto.getEmployeeNIC())))
            validationList.add("NIc field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(employeeDto.getCompanyRgNo())))
            validationList.add("CompanyReg No field is empty");
        return validationList;
    }
}



