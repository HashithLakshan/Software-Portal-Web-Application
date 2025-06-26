package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.PaymentResitDto;
import Software.Portal.web.entity.*;
import Software.Portal.web.repository.PaymentResitRepository;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.repository.ZoomRepository;
import Software.Portal.web.service.SystemProfileService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentResitService {

    private final PaymentResitRepository paymentResitRepository;

    private final SystemProfileRepository systemProfileRepository;

    private final ZoomRepository zoomRepository;

    private final ZoomService zoomService;

    private final SystemProfileService systemProfileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);


    @Autowired
    public PaymentResitService(PaymentResitRepository paymentResitRepository, SystemProfileRepository systemProfileRepository, ZoomRepository zoomRepository, ZoomService zoomService, SystemProfileService systemProfileService) {
        this.paymentResitRepository = paymentResitRepository;
        this.systemProfileRepository = systemProfileRepository;
        this.zoomRepository = zoomRepository;

        this.zoomService = zoomService;
        this.systemProfileService = systemProfileService;
    }


    public CommonResponse uploadPdf(MultipartFile file, String systemProfileId, String perchaseId, String requestStatus) {
        CommonResponse commonResponse = new CommonResponse();
        boolean x = CommonValidation.isNumeric(perchaseId);

        if(!x){
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Using Only Numbers !!");
            return commonResponse;
        }
        try {
            if (file == null || file.isEmpty()) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("File is empty or missing!");
                return commonResponse;
            } else if (!"application/pdf".equals(file.getContentType())) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Only PDF files are allowed!");
                return commonResponse;
            } else if (CommonValidation.stringNullValidation(perchaseId)) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Customer ID is Empty!");
                return commonResponse;
            } else if (!perchaseId.isEmpty()) {
                Optional<Zoom> zoom1 = zoomRepository.findById(Long.valueOf(perchaseId));
                PaymentResit paymentResit = new PaymentResit();
                Optional<PaymentResit> paymentResitOptional = paymentResitRepository.findById(Long.valueOf(systemProfileId));
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                LocalDate today = LocalDate.now();
                if(paymentResitOptional.isEmpty()) {
                    SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfileId)).get();
                    if (zoom1.isPresent()) {
                        Zoom zoom = zoomRepository.findById(Long.valueOf(perchaseId)).get();
                        paymentResit.setFileName(file.getOriginalFilename());
                        paymentResit.setFileType(file.getContentType());
                        paymentResit.setData(file.getBytes());
                        paymentResit.setSystemProfile(systemProfile);
                        paymentResit.setZoom(zoom);
                        paymentResit.setCommonStatus(CommonStatus.ACTIVE);
                        paymentResit.setRequestStatus(requestStatus1);
                        paymentResit.setSaveDate(today);
                        paymentResitRepository.save(paymentResit);
                        commonResponse.setStatus(true);
                        commonResponse.setCommonMessage("PDF uploaded successfully!");
                        return commonResponse;
                    } else {

                        commonResponse.setStatus(false);
                        commonResponse.setCommonMessage("Check Your Perchase ID!");
                        return commonResponse;
                    }
                }else{
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("Check Your SystemProfile ID!");
                    return commonResponse;
                }
            }
        } catch (IOException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Failed to upload PDF:" + e.getMessage());
            return commonResponse;
        }
        return commonResponse;
    }


    // Download PDF
    public Optional<PaymentResit> getPdf(Long id) {
        return paymentResitRepository.findById(id);
    }

    public CommonResponse getFilteredPayments(String commonStatus, String requestStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResit> paymentResits;
        List<PaymentResitDto> paymentResitDtoList;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
            RequestStatus requestStatusEnum = null;

            if (requestStatus != null && !requestStatus.isEmpty()) {
                requestStatusEnum = RequestStatus.valueOf(requestStatus);
            }

            if (commonStatus.equals("INACTIVE")) {
                paymentResits = paymentResitRepository.findByCommonStatus(commonStatusEnum, pageable);
            } else {
                paymentResits = paymentResitRepository.findByCommonStatusAndRequestStatus(commonStatusEnum, requestStatusEnum, pageable);
            }


            paymentResitDtoList = paymentResits.getContent().stream()
                    .map(this::PayementResitIntoPayement)
                    .collect(Collectors.toList());
            paymentResitDtoList.sort(Comparator.comparing(dto -> LocalDate.parse(dto.getSaveDate())));


            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", paymentResits.getNumber());
            paginationDetails.put("totalItems", paymentResits.getTotalElements());
            paginationDetails.put("totalPages", paymentResits.getTotalPages());

            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("payment  fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(paymentResitDtoList)); // Employee list
            commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details

        } catch (IllegalArgumentException e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Invalid request status or common status.");
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching payment.");
        }

        return commonResponse;
    }

    public PaymentResitDto PayementResitIntoPayement(PaymentResit paymentResit) {
        PaymentResitDto paymentResitDto = new PaymentResitDto();
        paymentResitDto.setZoomDto(zoomService.findById(String.valueOf(paymentResit.getZoom().getPerchaseId())));
        paymentResitDto.setSystemProfileDto(systemProfileService.findDetails(paymentResit.getSystemProfile().getSystemProfilesId()));
        paymentResitDto.setData(paymentResit.getData());
        paymentResitDto.setRequestStatus(paymentResit.getRequestStatus());
        paymentResitDto.setSaveDate(String.valueOf(paymentResit.getSaveDate()));
        paymentResitDto.setCommonStatus(paymentResit.getCommonStatus());
        paymentResitDto.setId(String.valueOf(paymentResit.getId()));
        return paymentResitDto;

    }

    public CommonResponse updateStatus(String id, String actionDo) {
        CommonResponse commonResponse = new CommonResponse();

        if (id.isEmpty()) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Please enter a id");
            return commonResponse;
        } else {
            PaymentResit paymentResit1 = paymentResitRepository.findById(Long.valueOf(id)).get();

            if (actionDo.equals("Completed")) {
                paymentResit1.setRequestStatus(RequestStatus.COMPLETED);
                paymentResitRepository.save(paymentResit1);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Successfully  complete business");
            } else if (actionDo.equals("Inactive")) {
                paymentResit1.setCommonStatus(CommonStatus.INACTIVE);
                paymentResitRepository.save(paymentResit1);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Successfully deleted");
                return commonResponse;
            } else if (actionDo.equals("deleteDatabase")) {
                paymentResitRepository.delete(paymentResit1);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Successfully deleted for database");
                return commonResponse;
            } else if (actionDo.equals("Active")) {
                paymentResit1.setCommonStatus(CommonStatus.ACTIVE);
                paymentResitRepository.save(paymentResit1);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage(" Successfully recovered");
                return commonResponse;

            }
        }
        return commonResponse;
    }

    public CommonResponse getDetailsInId(String commonStatus, String requestStatus, String id) {
        CommonResponse commonResponse = new CommonResponse();
        PaymentResit paymentResit;
        CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatusEnum = null;
        try {
            if (id != null && !id.isEmpty()) {
                if (requestStatus != null && !requestStatus.isEmpty()) {
                    requestStatusEnum = RequestStatus.valueOf(requestStatus);
                }

                if (commonStatus.equals("INACTIVE")) {
                    paymentResit = paymentResitRepository.findById(Long.valueOf(id)).get();
                } else {
                    paymentResit = paymentResitRepository.findByCommonStatusAndRequestStatusAndId(commonStatusEnum, requestStatusEnum, Long.valueOf(id));
                }

                if (paymentResit == null) {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No payment resit found with the given  Id.");
                    return commonResponse;
                }

                PaymentResitDto paymentResitDto = PayementResitIntoPayement(paymentResit);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Payment resit fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(paymentResitDto));
                return commonResponse;
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Employee found with the given PaymentResit ID.");
                return commonResponse;
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching the PaymentResit.");
            LOGGER.error("Exception in PaymentResitService -> getIdActivePaymentResitPendingAndApproved: " + e);

        }
        return commonResponse;
    }

    public CommonResponse getDetailsInPerchaseId(String commonStatus, String requestStatus, String perchaseId) {
        CommonResponse commonResponse = new CommonResponse();
        PaymentResit paymentResit;
        CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);
        RequestStatus requestStatusEnum = null;
        try {
            if (perchaseId != null && !perchaseId.isEmpty()) {
                if (requestStatus != null && !requestStatus.isEmpty()) {
                    requestStatusEnum = RequestStatus.valueOf(requestStatus);
                }

                if (commonStatus.equals("INACTIVE")) {
                    paymentResit = paymentResitRepository.findByZoomPerchaseId(Long.valueOf(perchaseId));
                } else {
                    paymentResit = paymentResitRepository.findByCommonStatusAndRequestStatusAndZoomPerchaseId(commonStatusEnum, requestStatusEnum, Long.valueOf(perchaseId));
                }

                if (paymentResit == null) {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No payment resit found with the given Perchase Id.");
                    return commonResponse;
                }

                PaymentResitDto paymentResitDto = PayementResitIntoPayement(paymentResit);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Payment resit fetched successfully.");
                commonResponse.setPayload(Collections.singletonList(paymentResitDto));
                return commonResponse;
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("No Payment found with the given perchase ID.");
                return commonResponse;
            }
        } catch (Exception e) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while fetching the PaymentResit.");
            LOGGER.error("Exception in PaymentResitService -> getIActivePaymentResitPendingAndApproved: " + e);

        }
        return commonResponse;
    }

    public CommonResponse getDetailsAllDateFilter(String fromDate, String toDate, String commonStatus, String requestStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        List<PaymentResitDto> paymentResitDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        if (CommonValidation.stringNullValidation(fromDate) || CommonValidation.stringNullValidation(toDate)) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Check your date pickers");
            return commonResponse;
        } else {
            if (commonStatus.equals("INACTIVE")) {
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<PaymentResit> paymentResitPage = paymentResitRepository.findBySaveDateBetweenAndCommonStatus(fromDateD, toDateD, commonStatus1, pageable);
                if (!paymentResitPage.isEmpty()) {
                    paymentResitDtoList = paymentResitPage.stream().map(this::PayementResitIntoPayement).collect(Collectors.toList());

                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", paymentResitPage.getNumber());
                    paginationDetails.put("totalItems", paymentResitPage.getTotalElements());
                    paginationDetails.put("totalPages", paymentResitPage.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("PaymentResit fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(paymentResitDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No Payment Resit Found");
                    return commonResponse;
                }
            } else {
                RequestStatus requestStatus1 = RequestStatus.valueOf(requestStatus);
                LocalDate fromDateD = LocalDate.parse(fromDate);
                LocalDate toDateD = LocalDate.parse(toDate);
                Page<PaymentResit> paymentResits = paymentResitRepository.findBySaveDateBetweenAndCommonStatusAndRequestStatus(fromDateD, toDateD, commonStatus1, requestStatus1, pageable);
                if (!paymentResits.isEmpty()) {
                    paymentResitDtoList = paymentResits.stream().map(this::PayementResitIntoPayement).collect(Collectors.toList());
                    Map<String, Object> paginationDetails = new HashMap<>();
                    paginationDetails.put("currentPage", paymentResits.getNumber());
                    paginationDetails.put("totalItems", paymentResits.getTotalElements());
                    paginationDetails.put("totalPages", paymentResits.getTotalPages());

                    // Set response payload properly
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("ZoomDetails fetched successfully.");
                    commonResponse.setPayload(Collections.singletonList(paymentResitDtoList));
                    commonResponse.setPages(Collections.singletonList(paginationDetails));// Pagination details
                    return commonResponse;
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("No payment Resit Found");
                    return commonResponse;
                }

            }
        }
    }
}