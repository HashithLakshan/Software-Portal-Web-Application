package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.ZoomDto;
import Software.Portal.web.entity.PaymentResit;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.entity.Zoom;
import Software.Portal.web.entity.ZoomTimeSlots;
import Software.Portal.web.repository.PaymentResitRepository;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.repository.ZoomRepository;
import Software.Portal.web.service.SystemProfileService;
import Software.Portal.web.service.ZoomTimeSlotsService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ZoomService {

    private final ZoomRepository zoomRepository;

    private final SystemProfileService systemProfileService;

    private  final ZoomTimeSlotsService zoomTimeSlotsService;

    private  final SystemProfileRepository systemProfileRepository;

    private final PaymentResitRepository paymentResitRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZoomService.class);


    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String MyEmail;

    @Value("${zoom.client.id}")
    private String clientId;

    @Value("${zoom.client.secret}")
    private String clientSecret;

    @Value("${zoom.token.url}")
    private String tokenUrl;

    @Value("${zoom.redirect.uri}")
    private String redirectUri;

    private String accessToken;
    private String refreshToken;
    private long accessTokenExpirationTime; // Store expiration time
    private static final String GRANT_TYPE = "authorization_code"; // Fixed for OAuth
    private final String ZOOM_API_URL = "https://api.zoom.us/v2/users/me/meetings";
    private static final String ZOOM_API_Delete = "https://api.zoom.us/v2/meetings/";


    private final ObjectMapper objectMapper = new ObjectMapper(); // Initialize ObjectMapper

    @Autowired
    public ZoomService(ZoomRepository zoomRepository, SystemProfileService systemProfileService, ZoomTimeSlotsService zoomTimeSlotsService, SystemProfileRepository systemProfileRepository, PaymentResitRepository paymentResitRepository) {
        this.zoomRepository = zoomRepository;
        this.zoomTimeSlotsService = zoomTimeSlotsService;
        this.systemProfileService = systemProfileService;
        this.systemProfileRepository = systemProfileRepository;
        this.paymentResitRepository = paymentResitRepository;
    }


    // Create Zoom Meeting String customerAddress;
    public CommonResponse createMeeting(String perchaseId,String zoomTimeSlotId,String topic,String startTime, String duration,String systemProfilesId,String rollName,String customerNumber,String customerAddress,String customerEmail,String customerName, String customerType,String companyRegNo) {
        CommonResponse commonResponse = new CommonResponse();
        boolean y = CommonValidation.isValidNumber(customerNumber);
        boolean x = CommonValidation.isNumberInRange(Integer.parseInt(duration));

        if(accessToken == null || refreshToken == null) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Firstly Authorise the zoom !!");
            return commonResponse;
        }


        List<String> validationList = this.zoomValidation(topic, startTime, duration, customerNumber, customerAddress, customerEmail, customerName, customerType, companyRegNo);
        if (!validationList.isEmpty()) {
            commonResponse.setErrorMessages(validationList);
            commonResponse.setStatus(false);
            return commonResponse;
        }



        if(x != true){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Select the 10 to 40 minutes number");
            return commonResponse;
        }
if(y != true){
    commonResponse.setStatus(false);
    commonResponse.setCommonMessage("Invalid your contact number");
    return commonResponse;
}
        LocalDateTime inputDateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Get current time
        LocalDateTime now = LocalDateTime.now();
        if (inputDateTime.toLocalDate().isEqual(now.toLocalDate())) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("We can not arrange your meeting today ");
            return commonResponse;
        }

         if (inputDateTime.isBefore(now)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("You Select the wrong Date Check the date");
            return commonResponse;
        } else {


        LocalDateTime dateTime1 = LocalDateTime.parse(startTime);
        LocalTime time1 = dateTime1.toLocalTime();
        LocalDate startDate = dateTime1.toLocalDate();
        Optional<Zoom> zoom1 = zoomRepository.findByZoomTimeSlotsZoomTimeSlotIdAndStartDateAndRequestStatusInAndCommonStatus(
                Long.valueOf(zoomTimeSlotId),
                startDate,
                Arrays.asList("APPROVED", "PENDING"),
                CommonStatus.ACTIVE
        );
        if (zoom1.isPresent()) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("this time slot already exists");
            return commonResponse;
        } else {

            if (rollName.equals("Super Admin")) {
                Optional<SystemProfile> systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfilesId));
                if (systemProfile.isPresent()) {



                    int minutes = Integer.parseInt(duration.replaceAll("\\D+", ""));

                    // Convert to LocalTime (starting from 00:00 and adding minutes)
                    LocalTime meetingEndTime = time1.plusMinutes(minutes);



                    ZoomTimeSlots zoomTimeSlots = zoomTimeSlotsService.findById(zoomTimeSlotId);

            if (!LocalTime.parse(zoomTimeSlots.getSlotOpenTime()).isBefore(time1)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Your select time not matches in slot open Time");
                return commonResponse;
            }
                    if (!LocalTime.parse(zoomTimeSlots.getSlotCloseTime()).isAfter(time1)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Your select time not matches in slot close Time");
                return commonResponse;
            }

                    if (!LocalTime.parse(zoomTimeSlots.getSlotCloseTime()).isAfter(meetingEndTime)) {
                        commonResponse.setStatus(false);
                        commonResponse.setCommonMessage("Your selected time extends beyond the slot closing time");
                        return commonResponse;
                    }

                    if (isAccessTokenExpired()) {
                        refreshAccessToken();
                    }

                    String url = ZOOM_API_URL;

                    // Headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(accessToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Meeting details payload
                    Map<String, Object> meetingDetails = new HashMap<>();

                    ZonedDateTime localDateTime = ZonedDateTime.of(java.time.LocalDateTime.parse(startTime,
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME), ZoneId.of("Asia/Colombo"));
                    ZonedDateTime utcDateTime = localDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                    String startTimeUTC = utcDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);


                    meetingDetails.put("topic", topic);
                    meetingDetails.put("type", 2);  // Scheduled Meeting
                    meetingDetails.put("start_time", startTimeUTC);  // ISO 8601 format
                    meetingDetails.put("duration", duration); // Duration in minutes
                    meetingDetails.put("timezone", "Asia/Colombo"); // Sri Lanka timezone
                    meetingDetails.put("agenda", "Meeting Agenda");

                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(meetingDetails, headers);

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                    if (response.getStatusCode() == HttpStatus.CREATED) {
                        // Parse the response to extract required details
                        String responseBody = response.getBody();
                        JsonNode responseJson = null;
                        try {
                            responseJson = objectMapper.readTree(responseBody);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        String joinUrl = responseJson.get("join_url").asText();
                        String meetingPassword = responseJson.get("password").asText();
                        String meetingTopic = responseJson.get("topic").asText();
                        String meetingStartTime = responseJson.get("start_time").asText();
                        String meetingDuration = String.valueOf(responseJson.get("duration").asInt());
                        String ZMeetingId = responseJson.get("id").asText(); // If you want it as a String


//
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(meetingStartTime);

                        // Convert to Sri Lanka Time (SLT)
                        ZonedDateTime sriLankaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Colombo"));

                        // Extract date and time separately in Sri Lanka Time
                        LocalDate date = sriLankaTime.toLocalDate();
                        LocalTime time = sriLankaTime.toLocalTime();

                        // Format date and time if needed
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                        String formattedDate = date.format(dateFormatter);
                        String formattedTime = time.format(timeFormatter);

                        Zoom zoom = new Zoom();
                        SimpleMailMessage message = new SimpleMailMessage();
                        zoom.setZoomLink(joinUrl);
                        zoom.setSheduleTopic(meetingTopic);
                        zoom.setStartDate(LocalDate.parse(formattedDate));
                        zoom.setStartTime(LocalTime.parse(formattedTime));
                        zoom.setMeetingPassword(meetingPassword);
                        zoom.setMeeting_Duration(meetingDuration);
                        zoom.setCommonStatus(CommonStatus.ACTIVE);
                        zoom.setCustomerNumber(customerNumber);
                        zoom.setCustomerEmail(customerEmail);
                        zoom.setCustomerName(customerName);
                        zoom.setCustomerType(customerType);
                        zoom.setCompanyRegNo(companyRegNo);
                        zoom.setCustomerAddress(customerAddress);
                        zoom.setZMeetingId(ZMeetingId);
                        zoom.setSystemProfile(systemProfileService.findById(systemProfilesId));
                        zoom.setZoomTimeSlots(zoomTimeSlotsService.findById(zoomTimeSlotId));
                        zoom.setRequestStatus(RequestStatus.APPROVED);
                        zoomRepository.save(zoom);
                        message.setTo(zoom.getCustomerEmail());
                        message.setSubject(zoom.getSheduleTopic());
                        message.setText( "zoom Start date "+zoom.getStartDate()+"\n"+ "zoom Start time"+
                                zoom.getStartTime() +"\n"+"zoom Start duration"+ zoom.getMeeting_Duration() +"\n"

                                + "zoom password"+zoom.getMeetingPassword()+"\n" +" zoom link" + zoom.getZoomLink()
                                +"\n"+"zoom meeting Id"+
                                zoom.getZMeetingId());
                        message.setFrom(MyEmail);
                        mailSender.send(message);
                        commonResponse.setStatus(true);
                        commonResponse.setCommonMessage("Success Create Meeting");
                        return commonResponse;
                    } else {
                        commonResponse.setStatus(false);
                        commonResponse.setCommonMessage("This SystemId does not match any records");
                        return commonResponse;

                    }

                }
            }
        }
    }
        return commonResponse;
    }

    // Fetch OAuth Access Token
    public CommonResponse getAccessToken(String code) {
        CommonResponse commonResponse =  new CommonResponse();

        if(CommonValidation.stringNullValidation(code)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Input field is Empty !!");
            return commonResponse;
        }

            if(accessToken != null || refreshToken != null) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Early generated Zoom Refresh Token and Access Token ");
                return commonResponse;
            }


        String url = tokenUrl + "?grant_type=" + GRANT_TYPE +
                "&code=" + code +
                "&redirect_uri=" + redirectUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                accessToken = responseJson.get("access_token").asText();
                refreshToken = responseJson.get("refresh_token").asText();
                int expiresIn = responseJson.get("expires_in").asInt(); // Get expiration time in seconds

                // Store the expiration time (current time + expires_in)
                accessTokenExpirationTime = System.currentTimeMillis() + (expiresIn);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Activate Zoom");
                return commonResponse;
            } else {
                throw new RuntimeException("Failed to fetch access token: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching tokens: " + e.getMessage(), e);
        }
    }


    // Check if the access token has expired
    private boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > accessTokenExpirationTime;
    }

    // Refresh the access token using the refresh token
    private void refreshAccessToken() {
        String url = tokenUrl + "?grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                accessToken = responseJson.get("access_token").asText();
                refreshToken = responseJson.get("refresh_token").asText();
                int expiresIn = responseJson.get("expires_in").asInt(); // Get expiration time in seconds

                // Store the expiration time (current time + expires_in)
                accessTokenExpirationTime = System.currentTimeMillis() + (expiresIn);

                System.out.println("Access token refreshed successfully.");
            } else {
                throw new RuntimeException("Failed to refresh access token: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing tokens: " + e.getMessage(), e);
        }
    }


    private ZoomDto castZoomIntoZoomDto(Zoom zoom) {
        ZoomDto zoomDto = new ZoomDto();
        zoomDto.setPerchaseId(String.valueOf(zoom.getPerchaseId()));
        zoomDto.setSheduleTopic(zoom.getSheduleTopic());
        zoomDto.setZoomLink(zoom.getZoomLink());
        zoomDto.setMeetingPassword(zoom.getMeetingPassword());
        zoomDto.setStartDate(String.valueOf(zoom.getStartDate()));
        zoomDto.setStartTime(String.valueOf(zoom.getStartTime()));
        zoomDto.setMeeting_Duration(String.valueOf(zoom.getMeeting_Duration()));
        zoomDto.setCommonStatus(zoom.getCommonStatus());
        zoomDto.setRequestStatus(zoom.getRequestStatus());
        zoomDto.setCustomerNumber(zoom.getCustomerNumber());
        zoomDto.setCustomerEmail(zoom.getCustomerEmail());
        zoomDto.setCustomerName(zoom.getCustomerName());
        zoomDto.setCustomerType(zoom.getCustomerType());
        zoomDto.setCompanyRegNo(zoom.getCompanyRegNo());
        zoomDto.setCustomerAddress(zoom.getCustomerAddress());
        zoomDto.setZMeetingId(zoom.getZMeetingId());
        zoomDto.setSystemProfileDto(systemProfileService.findDetails(zoom.getSystemProfile().getSystemProfilesId()));
        zoomDto.setZoomTimeSlotsDto(zoomTimeSlotsService.findDetails(zoom.getZoomTimeSlots().getZoomTimeSlotId()));
        return zoomDto;
    }

    public CommonResponse updateStatus(String changeStatus, String perchaseId) {
        CommonResponse commonResponse = new CommonResponse();
        Zoom zoom = new Zoom();
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            if (changeStatus.equals("Rejected")) {
                zoom = zoomRepository.findById(Long.valueOf(perchaseId)).get();
                zoom.setCommonStatus(CommonStatus.INACTIVE);
                zoomRepository.save(zoom);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Rejected Meeting");
                return commonResponse;
            }  else if (changeStatus.equals("DeleteDatabase")) {
                zoom = zoomRepository.findById(Long.valueOf(perchaseId)).get();
                zoomRepository.delete(zoom);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Deleted Meeting for Database");
                return commonResponse;
            } else if (changeStatus.equals("Completed")) {
                zoom = zoomRepository.findById(Long.valueOf(perchaseId)).get();
                zoom.setRequestStatus(RequestStatus.COMPLETED);
                zoomRepository.save(zoom);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Move to Complete table");
                return commonResponse;
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Error");
                return commonResponse;
            }
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in ZoomService -> Approve" + e);
        }
        return commonResponse;
    }

    public CommonResponse deleteMeeting(String meetingId) {
        CommonResponse commonResponse = new CommonResponse();
        String deleteUrl = ZOOM_API_Delete + meetingId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Void> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, Void.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) { // 204 No Content (successful delete)
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Meeting deleted successfully");
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Failed to delete meeting: " + response.getStatusCode());
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Error deleting meeting: " + e.getMessage());
        }

        return commonResponse;
    }


    private List<String> zoomValidation(String topic, String startTime, String duration, String customerNumber
                                        ,String customerAddress ,String customerEmail,String customerName,String customerType,String companyRegNo) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(topic))
            validationList.add("Meeting Reasons field is empty");
        if (CommonValidation.stringNullValidation(String.valueOf(startTime)))
            validationList.add("You aren't choose  start Date & Time");
        if (CommonValidation.stringNullValidation(String.valueOf(duration)))
            validationList.add("You aren't choose  Time Duration");
        if (CommonValidation.stringNullValidation(String.valueOf(customerNumber)))
            validationList.add("Contact number field are empty !");
        if (CommonValidation.stringNullValidation(String.valueOf(customerEmail)))
            validationList.add("Email field are empty !");
        if (CommonValidation.stringNullValidation(String.valueOf(customerName)))
            validationList.add("Name field are empty!");
        if (CommonValidation.stringNullValidation(String.valueOf(customerType)))
            validationList.add("You aren't choose  your Type");
        if (CommonValidation.stringNullValidation(String.valueOf(companyRegNo)))
            validationList.add("Company Reg No field are empty!");
        if (CommonValidation.stringNullValidation(String.valueOf(customerAddress)))
            validationList.add("Address No field are empty!");
        return validationList;
    }
    private List<String> zoomPendingValidation(String startDate, String customerNumber,String customerAddress ,String customerEmail,String customerName,String customerType,String companyRegNo) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(String.valueOf(startDate)))
            validationList.add("You aren't choose  start Date");
        if (CommonValidation.stringNullValidation(String.valueOf(customerNumber)))
            validationList.add("Contact number field are empty !");
        if (CommonValidation.stringNullValidation(String.valueOf(customerEmail)))
            validationList.add("Email field are empty !");
        if (CommonValidation.stringNullValidation(String.valueOf(customerName)))
            validationList.add("Name field are empty!");
        if (CommonValidation.stringNullValidation(String.valueOf(customerType)))
            validationList.add("You aren't choose  your Type");
        if (CommonValidation.stringNullValidation(String.valueOf(companyRegNo)))
            validationList.add("Company Reg No field are empty!");
        if (CommonValidation.stringNullValidation(String.valueOf(customerAddress)))
            validationList.add("Address No field are empty!");
        return validationList;
    }

    public CommonResponse getAll(String commonStatus, String requestStatus, int page, int size) {

        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<ZoomDto> zoomDtoList = new ArrayList<>();
        Page<Zoom> zoomPage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                zoomPage = zoomRepository.findByCommonStatus(commonStatusEnum, pageable);
            } else {
                zoomPage = zoomRepository.findByCommonStatusAndRequestStatus(commonStatusEnum, requestStatusEnum, pageable);
            }


            // Convert Employee entities to DTOs
            // Convert Employee entities to DTOs
            zoomDtoList = zoomPage.stream()
                    .map(this::castZoomIntoZoomDto)
                    .sorted(Comparator.comparing(ZoomDto::getStartDate)
                            .thenComparing(ZoomDto::getStartTime)) // Sort by date, then by time
                    .collect(Collectors.toList());








            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", zoomPage.getNumber());
            paginationDetails.put("totalItems", zoomPage.getTotalElements());
            paginationDetails.put("totalPages", zoomPage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("ZoomDetails fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(zoomDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching ZoomDetails.");
        }

        return commonResponse;
    }

    public CommonResponse savePending(String zoomTimeSlotId,String systemProfilesId ,String startDate, String topic,
                                      String customerNumber, String customerAddress, String customerEmail, String customerName,
                                      String customerType, String companyRegNo) {
        CommonResponse commonResponse  = new CommonResponse();
        Zoom zoom = new Zoom();
        List<String> validationList = this.zoomPendingValidation(startDate,customerNumber,customerAddress,customerEmail,customerName,customerType,companyRegNo);
        if (!validationList.isEmpty()) {
            commonResponse.setErrorMessages(validationList);
            commonResponse.setStatus(false);
            return commonResponse;
        }
        LocalDate inputDate = LocalDate.parse(startDate);
        LocalDate today = LocalDate.now();

        // Check if the date is past, today, or future
        if (inputDate.isEqual(today)) {
          commonResponse.setStatus(false);
          commonResponse.setCommonMessage("We can not arrange your meeting today ");
          return commonResponse;
        } else if (inputDate.isBefore(today)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("You Select the wrong Date Check the date");
            return commonResponse;
        } else {


            Optional<Zoom> zoom1 = zoomRepository.findByZoomTimeSlotsZoomTimeSlotIdAndStartDateAndRequestStatusInAndCommonStatus(
                    Long.valueOf(zoomTimeSlotId),
                    LocalDate.parse(startDate),
                    Arrays.asList("APPROVED", "PENDING"),
                    CommonStatus.ACTIVE
            );

            if (zoom1.isPresent()) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("This time slot already taken someone");
                return commonResponse;
            } else {
                zoom.setStartDate(LocalDate.parse(startDate));
                zoom.setCommonStatus(CommonStatus.ACTIVE);
                zoom.setRequestStatus(RequestStatus.PENDING);
                zoom.setCustomerNumber(customerNumber);
                zoom.setCustomerEmail(customerEmail);
                zoom.setCustomerName(customerName);
                zoom.setCustomerType(customerType);
                zoom.setCompanyRegNo(companyRegNo);
                zoom.setCustomerAddress(customerAddress);
                zoom.setSheduleTopic(topic);
                zoom.setSystemProfile(systemProfileService.findById(systemProfilesId));
                zoom.setZoomTimeSlots(zoomTimeSlotsService.findById(zoomTimeSlotId));
                zoomRepository.save(zoom);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("We are send You a Zoom link your mails.Focus Of that,Thank You !!");
                return commonResponse;
            }

        }
    }

    public CommonResponse createMeetingApproving(String perchaseId, String zoomTimeSlotId, String topic, String startTime, String duration, String systemProfilesId, String rollName, String customerNumber, String customerAddress, String customerEmail, String customerName, String customerType, String companyRegNo) {
        CommonResponse commonResponse  = new CommonResponse();
        LocalDateTime inputDateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if(accessToken == null || refreshToken == null) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Firstly Authorise the zoom !!");
            return commonResponse;
        }
        // Get current time
        LocalDateTime now = LocalDateTime.now();
        if (inputDateTime.toLocalDate().isEqual(now.toLocalDate())) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("We can not arrange your meeting today ");
            return commonResponse;
        }

        if (inputDateTime.isBefore(now)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("You Select the wrong Date Check the date");
            return commonResponse;
        }

            LocalDateTime dateTime1 = LocalDateTime.parse(startTime);
            LocalTime time1 = dateTime1.toLocalTime();


        Optional<Zoom> zoom = zoomRepository.findById(Long.valueOf(perchaseId));
        if (zoom.isPresent()) {


            int minutes = Integer.parseInt(duration.replaceAll("\\D+", "")); // Removes non-numeric characters
            LocalTime meetingEndTime = time1.plusMinutes(minutes);
            LocalTime slotOpenTime = LocalTime.parse(zoom.get().getZoomTimeSlots().getSlotOpenTime());
            LocalTime slotCloseTime = LocalTime.parse(zoom.get().getZoomTimeSlots().getSlotCloseTime());


            if (!slotOpenTime.isBefore(time1)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Your select time not matches in slot open Time");
                return commonResponse;
            }
            if (!slotCloseTime.isAfter(time1)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Your select time not matches in slot close Time");
                return commonResponse;
            }

            if (!slotCloseTime.isAfter(meetingEndTime)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Your selected time extends beyond the slot closing time");
                return commonResponse;
            }

                if (isAccessTokenExpired()) {
                    refreshAccessToken();
                }

                String url = ZOOM_API_URL;

                // Headers
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Meeting details payload
                Map<String, Object> meetingDetails = new HashMap<>();

                ZonedDateTime localDateTime = ZonedDateTime.of(java.time.LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME), ZoneId.of("Asia/Colombo"));
                ZonedDateTime utcDateTime = localDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                String startTimeUTC = utcDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);


                meetingDetails.put("topic", topic);
                meetingDetails.put("type", 2);  // Scheduled Meeting
                meetingDetails.put("start_time", startTimeUTC);  // ISO 8601 format
                meetingDetails.put("duration", duration); // Duration in minutes
                meetingDetails.put("timezone", "Asia/Colombo"); // Sri Lanka timezone
                meetingDetails.put("agenda", "Meeting Agenda");

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(meetingDetails, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                if (response.getStatusCode() == HttpStatus.CREATED) {
                    // Parse the response to extract required details
                    String responseBody = response.getBody();
                    JsonNode responseJson = null;
                    try {
                        responseJson = objectMapper.readTree(responseBody);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    String joinUrl = responseJson.get("join_url").asText();
                    String meetingPassword = responseJson.get("password").asText();
                    String meetingTopic = responseJson.get("topic").asText();
                    String meetingStartTime = responseJson.get("start_time").asText();
                    String meetingDuration = String.valueOf(responseJson.get("duration").asInt());
                    String ZMeetingId = responseJson.get("id").asText(); // If you want it as a String


//            Map<String, Object> meetingInfo = new HashMap<>();
//            meetingInfo.put("join_url", joinUrl);
//            meetingInfo.put("password", meetingPassword);
//            meetingInfo.put("topic", meetingTopic);
//            meetingInfo.put("start_time", meetingStartTime);
//            meetingInfo.put("duration", meetingDuration);
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(meetingStartTime);

                    // Convert to Sri Lanka Time (SLT)
                    ZonedDateTime sriLankaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Colombo"));

                    // Extract date and time separately in Sri Lanka Time
                    LocalDate date = sriLankaTime.toLocalDate();
                    LocalTime time = sriLankaTime.toLocalTime();

                    // Format date and time if needed
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                    String formattedDate = date.format(dateFormatter);
                    String formattedTime = time.format(timeFormatter);

//            if(rollName.equals("customer")) {
//
//                Zoom zoom = new Zoom();
//                zoom.setZoomLink(joinUrl);
//                zoom.setSheduleTopic(meetingTopic);
//                zoom.setStartDate(LocalDate.parse(formattedDate));
//                zoom.setStartTime(LocalTime.parse(formattedTime));
//                zoom.setMeetingPassword(meetingPassword);
//                zoom.setMeeting_Duration(meetingDuration);
//                zoom.setCommonStatus(CommonStatus.ACTIVE);
//                zoom.setRequestStatus(RequestStatus.PENDING);
//                zoom.setCustomerNumber(customerNumber);
//                zoom.setCustomerEmail(customerEmail);
//                zoom.setCustomerName(customerName);
//                zoom.setCustomerType(customerType);
//                zoom.setCompanyRegNo(companyRegNo);
//                zoom.setCustomerAddress(customerAddress);
//                zoom.setZMeetingId(ZMeetingId);
//                zoom.setSystemProfile(systemProfileService.findById(systemProfilesId));
//                zoomRepository.save(zoom);
//                commonResponse.setStatus(true);
//                commonResponse.setCommonMessage("We are send You a Zoom link your mails.Focus Of that,Thank You !!");
//                return commonResponse;
//            }


                    SimpleMailMessage message = new SimpleMailMessage();
                    zoom.get().setZoomLink(joinUrl);
                    zoom.get().setSheduleTopic(meetingTopic);
                    zoom.get().setStartDate(LocalDate.parse(formattedDate));
                    zoom.get().setStartTime(LocalTime.parse(formattedTime));
                    zoom.get().setMeetingPassword(meetingPassword);
                    zoom.get().setMeeting_Duration(meetingDuration);
                    zoom.get().setCommonStatus(CommonStatus.ACTIVE);
                    zoom.get().setCustomerNumber(customerNumber);
                    zoom.get().setCustomerEmail(customerEmail);
                    zoom.get().setCustomerName(customerName);
                    zoom.get().setCustomerType(customerType);
                    zoom.get().setCompanyRegNo(companyRegNo);
                    zoom.get().setCustomerAddress(customerAddress);
                    zoom.get().setZMeetingId(ZMeetingId);
                    zoom.get().setSystemProfile(systemProfileService.findById(systemProfilesId));
                    zoom.get().setZoomTimeSlots(zoomTimeSlotsService.findById(zoomTimeSlotId));
                    zoom.get().setRequestStatus(RequestStatus.APPROVED);
                    zoomRepository.save(zoom.get());
                    message.setTo(zoom.get().getCustomerEmail());
                    message.setSubject(zoom.get().getSheduleTopic());
                    message.setText("hello world" + zoom.get().getStartDate() +
                            zoom.get().getStartTime() + zoom.get().getMeeting_Duration()
                            + zoom.get().getMeetingPassword() + zoom.get().getZoomLink() + zoom.get().getZMeetingId());
                    message.setFrom(MyEmail);
                    mailSender.send(message);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("You Approved Meeting");
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("This SystemId does not match any records");
                    return commonResponse;

                }

            }

            return commonResponse;
        }


    public CommonResponse getPerchaseDetails(String perchaseId,String commonStatus,String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        if (CommonValidation.stringNullValidation(perchaseId)){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Input Field are empty");
            return commonResponse;
        }else {
            if (requestStatus.equals("INACTIVE")) {
                Zoom zoom = zoomRepository.findByCommonStatusAndPerchaseId(commonStatus1, Long.valueOf(perchaseId));
                if(zoom != null){
                    ZoomDto zoomDto = castZoomIntoZoomDto(zoom);
                    commonResponse.setStatus(true);
                    commonResponse.setPayload(Collections.singletonList(zoomDto));
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }

            }else {
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                Zoom zoom = zoomRepository.findByCommonStatusAndRequestStatusAndPerchaseId( commonStatus1, requestStatus1, Long.valueOf(perchaseId));
                if (zoom != null) {
                    ZoomDto zoomDto = castZoomIntoZoomDto(zoom);
                    commonResponse.setStatus(true);
                    commonResponse.setPayload(Collections.singletonList(zoomDto));
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }

            }

        }

    }

    public CommonResponse getZoomIdDetails(String zMeetingId, String commonStatus, String requestStatus) {

        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        if(CommonValidation.stringNullValidation(zMeetingId)){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Input Field are empty");
            return commonResponse;
        }else{

            if(requestStatus.equals("INACTIVE")){
                Zoom zoom = zoomRepository.findByzMeetingIdAndCommonStatus(zMeetingId,commonStatus1);
                if(zoom != null){
                    ZoomDto zoomDto = castZoomIntoZoomDto(zoom);
                    commonResponse.setStatus(true);
                    commonResponse.setPayload(Collections.singletonList(zoomDto));
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }
            }else{
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                Zoom zoom = zoomRepository.findByzMeetingIdAndCommonStatusAndRequestStatus(zMeetingId,commonStatus1,requestStatus1);
                if(zoom != null){
                    ZoomDto zoomDto = castZoomIntoZoomDto(zoom);
                    commonResponse.setStatus(true);
                    commonResponse.setPayload(Collections.singletonList(zoomDto));
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }
            }
        }
    }

    public CommonResponse getDetailsAllDateFilter(String fromDate, String toDate, String commonStatus, String requestStatus,int page,int size) {
        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        List<ZoomDto> zoomDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        if(CommonValidation.stringNullValidation(fromDate) || CommonValidation.stringNullValidation(toDate)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Check your date pickers");
            return commonResponse;
        }else {
            if(commonStatus.equals("INACTIVE")){
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<Zoom> zooms = zoomRepository.findByStartDateBetweenAndCommonStatus(fromDateD,toDateD,commonStatus1,pageable);
                if(!zooms.isEmpty()){
                    zoomDtoList = zooms.stream()
                            .map(this::castZoomIntoZoomDto)
                            .sorted(Comparator.comparing(ZoomDto::getStartDate)
                                    .thenComparing(ZoomDto::getStartTime)) // Sort by date, then by time
                            .collect(Collectors.toList());


                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", zooms.getNumber());
                    paginationDetails.put("totalItems", zooms.getTotalElements());
                    paginationDetails.put("totalPages", zooms.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("ZoomDetails fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(zoomDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }
            }else{
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<Zoom> zooms = zoomRepository.findByStartDateBetweenAndCommonStatusAndRequestStatus(fromDateD,toDateD,commonStatus1,requestStatus1,pageable);
                if(!zooms.isEmpty()){
                    zoomDtoList = zooms.stream()
                            .map(this::castZoomIntoZoomDto)
                            .sorted(Comparator.comparing(ZoomDto::getStartDate)
                                    .thenComparing(ZoomDto::getStartTime)) // Sort by date, then by time
                            .collect(Collectors.toList());


                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", zooms.getNumber());
                    paginationDetails.put("totalItems", zooms.getTotalElements());
                    paginationDetails.put("totalPages", zooms.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("ZoomDetails fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(zoomDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Meeting Found");
                    return commonResponse;
                }

            }
        }



    }


    public ZoomDto findById(String perchaseId) {
        ZoomDto zoomDto;
        Zoom zoom = zoomRepository.findById(Long.valueOf(perchaseId)).get();
        zoomDto = castZoomIntoZoomDto(zoom);
        return zoomDto;
    }

    public CommonResponse reminderZoom() {
        CommonResponse commonResponse = new CommonResponse();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0).plusMinutes(30);
        LocalTime now1 = LocalTime.now().withSecond(0).withNano(0).plusMinutes(10);

        Zoom zoom = zoomRepository.findByStartDateAndStartTimeAndCommonStatusAndRequestStatus(today,now,CommonStatus.ACTIVE,RequestStatus.APPROVED);
        if(zoom != null){
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("You Have meeting in 30 minutes Perchase ID :" + zoom.getPerchaseId() +
                    "And Customer Name is" + zoom.getCustomerName());
        }else{
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Meeting Found");
        }
        Zoom zoom1 = zoomRepository.findByStartDateAndStartTimeAndCommonStatusAndRequestStatus(today,now1,CommonStatus.ACTIVE,RequestStatus.APPROVED);
        if(zoom1 != null){
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("You Have meeting in 10 minutes Perchase ID :" + zoom.getPerchaseId() +
                    "And Customer Name is" + zoom.getCustomerName());
        }else{
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("No Meeting Found");
        }


        return commonResponse;
    }


    public CommonResponse getAllToday(String action,String commonStatus,String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        List<ZoomDto> zoomDtoList = new ArrayList<>();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
        LocalDate currentDate = LocalDate.now();
        if(action.equals("today")) {
            List<Zoom> zoom = zoomRepository.findByCommonStatusAndRequestStatusAndStartDate(commonStatus1, requestStatus1, currentDate);

//                zoomDtoList = zoom.stream()
//                        .map(this::castZoomIntoZoomDto)
//                        .sorted(Comparator.comparing(ZoomDto::getStartDate)
//                                .thenComparing(ZoomDto::getStartTime)) // First by date, then by time
//                        .collect(Collectors.toList());

                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("zoom today fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(zoom)); // Employee list

            return commonResponse;
        } else if (action.equals("payment")) {
            List<PaymentResit> paymentResits = paymentResitRepository.findByCommonStatusAndRequestStatus(commonStatus1, requestStatus1);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Payment pending fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(paymentResits));

        } else if (action.equals("pending")){
            List<Zoom> zoom = zoomRepository.findByCommonStatusAndRequestStatus(commonStatus1, requestStatus1);
//            if (zoom.isEmpty()) {
//                commonResponse.setStatus(false);
//                commonResponse.setCommonMessage("No pending zoom found");
//
//            } else {
//                zoomDtoList = zoom.stream()
//                        .map(this::castZoomIntoZoomDto)
//                        .collect(Collectors.toList());
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("zoom pending fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(zoom)); // Employee list
//            }
            return commonResponse;
        }
return commonResponse;
    }

    public CommonResponse getAllTodayPage(String commonStatus,String requestStatus,int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<ZoomDto> zoomDtoList = new ArrayList<>();
        Page<Zoom> zoomPage;
        LocalDate currentDate = LocalDate.now();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
        try {




                zoomPage = zoomRepository.findByCommonStatusAndRequestStatusAndStartDate(commonStatus1, requestStatus1,currentDate, pageable);



         zoomDtoList = zoomPage.stream()
                    .map(this::castZoomIntoZoomDto)
                    .sorted(Comparator.comparing(ZoomDto::getStartTime)) // Sort by startTime only
                    .collect(Collectors.toList());









            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", zoomPage.getNumber());
            paginationDetails.put("totalItems", zoomPage.getTotalElements());
            paginationDetails.put("totalPages", zoomPage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("ZoomDetails fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(zoomDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching ZoomDetails.");
        }

        return commonResponse;
    }
}

