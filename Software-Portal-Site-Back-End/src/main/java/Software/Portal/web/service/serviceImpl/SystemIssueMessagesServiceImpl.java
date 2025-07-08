package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.MessageStatus;
import Software.Portal.web.constant.ReplyMessageStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.SystemIssueMessagesDto;
import Software.Portal.web.dto.SystemIssuesAnswerDto;
import Software.Portal.web.dto.ZoomDto;
import Software.Portal.web.entity.SystemIssueMessages;
import Software.Portal.web.entity.SystemIssuesAnswer;
import Software.Portal.web.entity.Zoom;
import Software.Portal.web.repository.SystemIssueMessagesRepository;
import Software.Portal.web.repository.SystemIssuesAnswerRepository;
import Software.Portal.web.repository.ZoomRepository;
import Software.Portal.web.service.SystemIssueMessagesService;
import Software.Portal.web.service.SystemProfileService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemIssueMessagesServiceImpl implements SystemIssueMessagesService {

    private final SystemIssueMessagesRepository systemIssueMessagesRepository;

    private final ZoomService zoomService;

    private final ZoomRepository zoomRepository;

    private final JavaMailSender mailSender;

    private final SystemIssuesAnswerRepository systemIssuesAnswerRepository;

    @Autowired
    public SystemIssueMessagesServiceImpl(SystemIssueMessagesRepository systemIssueMessagesRepository, ZoomService zoomService, ZoomRepository zoomRepository, JavaMailSender mailSender, SystemIssuesAnswerRepository systemIssuesAnswerRepository) {
        this.systemIssueMessagesRepository = systemIssueMessagesRepository;
        this.zoomService = zoomService;
        this.zoomRepository = zoomRepository;
        this.mailSender = mailSender;
        this.systemIssuesAnswerRepository = systemIssuesAnswerRepository;
    }



    @Value("${spring.mail.username}")
    private String MyEmail;

    @Override
    public CommonResponse getFilteredMessages(String commonStatus, String replyMessageStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<SystemIssueMessagesDto> systemIssueMessagesDtoList = new ArrayList<>();
        Page<SystemIssueMessages> systemIssueMessagesPage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            ReplyMessageStatus messageStatus = null;

            if (replyMessageStatus != null && !replyMessageStatus.isEmpty()) {
                messageStatus = ReplyMessageStatus.valueOf(replyMessageStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                systemIssueMessagesPage = systemIssueMessagesRepository.findByCommonStatus(commonStatusEnum, pageable);
            } else {
                systemIssueMessagesPage = systemIssueMessagesRepository.findByCommonStatusAndReplyMessageStatus(commonStatusEnum, messageStatus, pageable);
            }


            // Convert Employee entities to DTOs
            systemIssueMessagesDtoList = systemIssueMessagesPage.getContent().stream()
                    .map(this::SystemIssueMessagesIntoSystemIssueMessagesDto)
                    .collect(Collectors.toList());


            systemIssueMessagesDtoList.sort(Comparator.comparing(dto -> LocalDate.parse(dto.getReceivedDate())));


            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", systemIssueMessagesPage.getNumber());
            paginationDetails.put("totalItems", systemIssueMessagesPage.getTotalElements());
            paginationDetails.put("totalPages", systemIssueMessagesPage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("SystemIssuesMessages fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDtoList)); // Employee list
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
    public CommonResponse save(SystemIssueMessagesDto systemIssueMessagesDto) {
        CommonResponse  commonResponse = new CommonResponse();
        SystemIssueMessages systemIssueMessages = new SystemIssueMessages();
        boolean x = CommonValidation.isNumeric(systemIssueMessagesDto.getZoomDto().getPerchaseId());
        List<String> validationList = this.SystemIssueMessagesValidation(systemIssueMessagesDto);
        if (!validationList.isEmpty()) {
            commonResponse.setErrorMessages(validationList);
            commonResponse.setStatus(false);
            return commonResponse;
        }
        if(!x) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Using Only Numbers");
            return commonResponse;

        }
        Zoom zoom = zoomRepository.findByCommonStatusAndRequestStatusAndPerchaseId(CommonStatus.ACTIVE, RequestStatus
                .COMPLETED,Long.valueOf(systemIssueMessagesDto.getZoomDto().getPerchaseId()));
        if(zoom != null) {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            systemIssueMessages.setZoom(zoom);
            systemIssueMessages.setSubject(systemIssueMessagesDto.getSubject());
            systemIssueMessages.setBody(systemIssueMessagesDto.getBody());
            systemIssueMessages.setCommonStatus(CommonStatus.ACTIVE);
            systemIssueMessages.setReplyMessageStatus(ReplyMessageStatus.NO);
            systemIssueMessages.setReceivedDate(currentDate);
            systemIssueMessages.setReceivedTime(currentTime);
            systemIssueMessagesRepository.save(systemIssueMessages);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("We reply you soon !!");
            commonResponse.setPayload(Collections.singletonList(systemIssueMessages));
            return commonResponse;

        }else{
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Your perchase Id wrong Please check the Id");
            return commonResponse;
        }

    }

    @Override
    public CommonResponse updateStatus(String status,String issueId,String subject,String body) {
        CommonResponse commonResponse = new CommonResponse();
        SystemIssueMessages  systemIssueMessages = new SystemIssueMessages();
        SimpleMailMessage message = new SimpleMailMessage();
        SystemIssuesAnswer  systemIssuesAnswer = new SystemIssuesAnswer();
        List<String> validationList = this.SystemIssueMessagesValidationSending(subject,body);

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        systemIssueMessages = systemIssueMessagesRepository.findById(Long.valueOf(issueId)).get();
        SystemIssuesAnswer systemIssuesAnswer1 = systemIssuesAnswerRepository.findBySystemIssueMessagesIssueId(systemIssueMessages.getIssueId());

        if(status.equals("YES")){
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                commonResponse.setStatus(false);
                return commonResponse;
            }
        systemIssueMessages.setReplyMessageStatus(ReplyMessageStatus.YES);
            message.setTo(systemIssueMessages.getZoom().getCustomerEmail());
            message.setSubject(systemIssueMessages.getSubject());
            message.setText("hello world" + systemIssueMessages.getBody());
            message.setFrom(MyEmail);
            mailSender.send(message);
            systemIssuesAnswer.setAnswerSubject(systemIssueMessages.getSubject());
            systemIssuesAnswer.setAnswerBody(systemIssueMessages.getBody());
            systemIssuesAnswer.setSendDate(String.valueOf(currentDate));
            systemIssuesAnswer.setSendTime(String.valueOf(currentTime));
            systemIssuesAnswer.setSystemIssueMessages(systemIssueMessages);
            systemIssuesAnswer.setCommonStatus(CommonStatus.ACTIVE);
            systemIssuesAnswerRepository.save(systemIssuesAnswer);
        systemIssueMessagesRepository.save(systemIssueMessages);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Your Email is delivered");
        } else if (status.equals("DELETE")) {
            systemIssueMessages.setCommonStatus(CommonStatus.INACTIVE);
            systemIssueMessagesRepository.save(systemIssueMessages);
            if(systemIssuesAnswer1 != null) {
                systemIssuesAnswer1.setCommonStatus(CommonStatus.INACTIVE);
                systemIssuesAnswerRepository.save(systemIssuesAnswer1);
            }
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Message deleted");
        } else if (status.equals("deleteDatabase")) {
            systemIssueMessagesRepository.deleteById(Long.valueOf(issueId));
            if(systemIssuesAnswer1 != null) {
                systemIssuesAnswerRepository.delete(systemIssuesAnswer1);
            }
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Message deleted by Database");

        } else if (status.equals("ACTIVE")) {
            systemIssueMessages.setCommonStatus(CommonStatus.ACTIVE);
            systemIssueMessagesRepository.save(systemIssueMessages);
            if(systemIssuesAnswer1 != null) {
                systemIssuesAnswer1.setCommonStatus(CommonStatus.ACTIVE);
                systemIssuesAnswerRepository.save(systemIssuesAnswer1);
            }
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Message recovered");
        }
        return commonResponse;
    }

    @Override
    public CommonResponse getDetailsAllDateFilter(String fromDate, String toDate, String commonStatus, String replyMessageStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        List<SystemIssueMessagesDto> systemIssueMessagesDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        if(CommonValidation.stringNullValidation(fromDate) || CommonValidation.stringNullValidation(toDate)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Check your date pickers");
            return commonResponse;
        }else {
            if(commonStatus.equals("INACTIVE")){
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<SystemIssueMessages> systemIssueMessagesPage = systemIssueMessagesRepository.findByReceivedDateBetweenAndCommonStatus(fromDateD,toDateD,commonStatus1,pageable);
                if(!systemIssueMessagesPage.isEmpty()){
                    systemIssueMessagesDtoList = systemIssueMessagesPage.stream().map(this::SystemIssueMessagesIntoSystemIssueMessagesDto).collect(Collectors.toList());

                    systemIssueMessagesDtoList.sort(Comparator.comparing(dto -> LocalDate.parse(dto.getReceivedDate())));

                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", systemIssueMessagesPage.getNumber());
                    paginationDetails.put("totalItems", systemIssueMessagesPage.getTotalElements());
                    paginationDetails.put("totalPages", systemIssueMessagesPage.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Message fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Message Found");
                    return commonResponse;
                }
            }else{
                ReplyMessageStatus replyMessageStatus1 = ReplyMessageStatus.valueOf(replyMessageStatus);
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<SystemIssueMessages> systemIssueMessagesPage = systemIssueMessagesRepository.findByReceivedDateBetweenAndCommonStatusAndReplyMessageStatus(fromDateD,toDateD,commonStatus1,replyMessageStatus1,pageable);
                if(!systemIssueMessagesPage.isEmpty()){
                    systemIssueMessagesDtoList = systemIssueMessagesPage.stream().map(this::SystemIssueMessagesIntoSystemIssueMessagesDto).collect(Collectors.toList());
                    systemIssueMessagesDtoList.sort(Comparator.comparing(dto -> LocalDate.parse(dto.getReceivedDate())));

                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", systemIssueMessagesPage.getNumber());
                    paginationDetails.put("totalItems", systemIssueMessagesPage.getTotalElements());
                    paginationDetails.put("totalPages", systemIssueMessagesPage.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Message fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Message Found");
                    return commonResponse;
                }

            }
        }



    }

    @Override
    public SystemIssueMessagesDto findll(Long issueId) {
        SystemIssueMessagesDto systemIssueMessagesDto = new SystemIssueMessagesDto();
        SystemIssueMessages systemIssueMessages = systemIssueMessagesRepository.findById(issueId).get();
        systemIssueMessagesDto = SystemIssueMessagesIntoSystemIssueMessagesDto(systemIssueMessages);
        return systemIssueMessagesDto;
    }

    @Override
    public CommonResponse detailsPerchaseId(String perchaseId,String status) {
        CommonResponse commonResponse =new CommonResponse();
        SystemIssueMessages systemIssueMessages = new SystemIssueMessages();
        SystemIssuesAnswer systemIssuesAnswer = new SystemIssuesAnswer();
        ReplyMessageStatus replyMessageStatus = ReplyMessageStatus.valueOf(status);
        if(CommonValidation.stringNullValidation(perchaseId)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Check your Search field");
            return commonResponse;
        }
        if(status.equals("YES") || status.equals("NO")) {
         systemIssueMessages = systemIssueMessagesRepository.findByZoomPerchaseIdAndReplyMessageStatusAndCommonStatus(Long.valueOf(perchaseId),replyMessageStatus,CommonStatus.ACTIVE);
         if(systemIssueMessages != null){
             SystemIssueMessagesDto systemIssueMessagesDto = SystemIssueMessagesIntoSystemIssueMessagesDto(systemIssueMessages);
             commonResponse.setStatus(true);
             commonResponse.setCommonMessage("Message fetched successfully.");
             commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDto));
         }else{
             commonResponse.setStatus(false);
             commonResponse.setCommonMessage("No Message Found");
         }

        } else {
            systemIssueMessages = systemIssueMessagesRepository.findByZoomPerchaseIdAndCommonStatus(Long.valueOf(perchaseId),CommonStatus.INACTIVE);
            if(systemIssueMessages != null){
                SystemIssueMessagesDto systemIssueMessagesDto = SystemIssueMessagesIntoSystemIssueMessagesDto(systemIssueMessages);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Message fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDto));

            }else{
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Message Found");
            }

        }
        return commonResponse;
    }

    @Override
    public CommonResponse DetailsIssueId(String issueId, String status) {
        CommonResponse commonResponse =new CommonResponse();
        SystemIssueMessages systemIssueMessages = new SystemIssueMessages();
        SystemIssuesAnswer systemIssuesAnswer = new SystemIssuesAnswer();
        ReplyMessageStatus replyMessageStatus = ReplyMessageStatus.valueOf(status);
        if(CommonValidation.stringNullValidation(issueId)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Check your Search field");
            return commonResponse;
        }
        if(status.equals("Yes") || status.equals("NO")) {
            systemIssueMessages = systemIssueMessagesRepository.findByReplyMessageStatusAndCommonStatusAndIssueId(replyMessageStatus,CommonStatus.ACTIVE,Long.valueOf(issueId));
            if(systemIssueMessages != null){
                SystemIssueMessagesDto systemIssueMessagesDto = SystemIssueMessagesIntoSystemIssueMessagesDto(systemIssueMessages);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Message fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDto));
            }else{
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Message Found");
            }
            } else {
            systemIssueMessages = systemIssueMessagesRepository.findByCommonStatusAndIssueId(CommonStatus.INACTIVE,Long.valueOf(issueId));
            if(systemIssueMessages != null){
                SystemIssueMessagesDto systemIssueMessagesDto = SystemIssueMessagesIntoSystemIssueMessagesDto(systemIssueMessages);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Message fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(systemIssueMessagesDto));
            }else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Message Found");
            }

            }

        return commonResponse;
    }


    private List<String> SystemIssueMessagesValidation(SystemIssueMessagesDto systemIssueMessagesDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(systemIssueMessagesDto.getSubject()))
            validationList.add("Message subject field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(systemIssueMessagesDto.getBody())))
            validationList.add("Message body field are empty !");
        if (CommonValidation.stringNullValidation(String.valueOf(systemIssueMessagesDto.getZoomDto().getPerchaseId())))
            validationList.add("Perchase Id field are empty !");
        return validationList;
    }

    private List<String> SystemIssueMessagesValidationSending( String subject,String body) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(subject))
            validationList.add("Message subject field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(body)))
            validationList.add("Message body field are empty !");

        return validationList;
    }
    public SystemIssueMessagesDto SystemIssueMessagesIntoSystemIssueMessagesDto(SystemIssueMessages systemIssueMessages) {
        SystemIssueMessagesDto systemIssueMessagesDto = new SystemIssueMessagesDto();
        systemIssueMessagesDto.setIssueId(String.valueOf(systemIssueMessages.getIssueId()));
        systemIssueMessagesDto.setSubject(systemIssueMessages.getSubject());
        systemIssueMessagesDto.setBody(systemIssueMessages.getBody());
        systemIssueMessagesDto.setReceivedDate(String.valueOf(systemIssueMessages.getReceivedDate()));
        systemIssueMessagesDto.setReceivedTime( String.valueOf(systemIssueMessages.getReceivedTime()));
        systemIssueMessagesDto.setReplyMessageStatus(systemIssueMessages.getReplyMessageStatus());
        systemIssueMessagesDto.setCommonStatus(systemIssueMessages.getCommonStatus());
        systemIssueMessagesDto.setZoomDto(zoomService.findById(String.valueOf(systemIssueMessages.getZoom().getPerchaseId())));
        return systemIssueMessagesDto;
    }
}
