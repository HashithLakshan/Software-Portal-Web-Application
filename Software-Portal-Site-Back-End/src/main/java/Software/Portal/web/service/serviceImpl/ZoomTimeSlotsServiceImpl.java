package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.dto.ZoomTimeSlotsDto;
import Software.Portal.web.entity.ZoomTimeSlots;
import Software.Portal.web.repository.ZoomTimeSlotsRepository;
import Software.Portal.web.service.ZoomTimeSlotsService;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ZoomTimeSlotsServiceImpl implements ZoomTimeSlotsService {

    private final ZoomTimeSlotsRepository zoomTimeSlotsRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    public ZoomTimeSlotsServiceImpl(ZoomTimeSlotsRepository zoomTimeSlotsRepository) {
        this.zoomTimeSlotsRepository = zoomTimeSlotsRepository;
    }

    @Override
    public CommonResponse save(ZoomTimeSlotsDto zoomTimeSlotsDto) {
        CommonResponse commonResponse = new CommonResponse();
        ZoomTimeSlots zoomTimeSlots2 = new ZoomTimeSlots();
        List<String> validationList = this.zoomTimeSlotValidation(zoomTimeSlotsDto);
        if (!validationList.isEmpty()) {
            commonResponse.setErrorMessages(validationList);
            commonResponse.setStatus(false);
            return commonResponse;
        }

        if(CommonValidation.stringNullValidation(zoomTimeSlotsDto.getZoomTimeSlotId())){
            zoomTimeSlots2.setSlotOpenTime(zoomTimeSlotsDto.getSlotOpenTime());
            zoomTimeSlots2.setSlotCloseTime(zoomTimeSlotsDto.getSlotCloseTime());
            zoomTimeSlots2.setCommonStatus(zoomTimeSlotsDto.getCommonStatus());
            zoomTimeSlotsRepository.save(zoomTimeSlots2);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Successfully Add");
            return commonResponse;
        }
      Optional<ZoomTimeSlots> zoomTimeSlots = zoomTimeSlotsRepository.findById(Long.valueOf(zoomTimeSlotsDto.getZoomTimeSlotId()));
        ZoomTimeSlots zoomTimeSlots1 = zoomTimeSlotsRepository.findById(Long.valueOf(zoomTimeSlotsDto.getZoomTimeSlotId())).get();
        ZoomTimeSlots zoomTimeSlots3 = zoomTimeSlotsRepository.findBySlotOpenTimeAndSlotCloseTime(zoomTimeSlots1.
                getSlotOpenTime(), zoomTimeSlots1.getSlotCloseTime());

        if(zoomTimeSlots.isPresent()) {
            if(zoomTimeSlots3 !=null) {
                if (zoomTimeSlots1.getSlotOpenTime().equals(zoomTimeSlotsDto.getSlotOpenTime())
                        && zoomTimeSlots1.getSlotCloseTime().equals(zoomTimeSlotsDto.getSlotCloseTime())) {
                    commonResponse.setStatus(false);
                    commonResponse.setCommonMessage("You are not any Changes here");
                } else {
                    zoomTimeSlots1.setSlotOpenTime(zoomTimeSlotsDto.getSlotOpenTime());
                    zoomTimeSlots1.setSlotCloseTime(zoomTimeSlotsDto.getSlotCloseTime());
                    zoomTimeSlots1.setCommonStatus(zoomTimeSlotsDto.getCommonStatus());
                    zoomTimeSlotsRepository.save(zoomTimeSlots1);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Successfully Updated");
                }
            }else{
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("this time slot already taken");
                return commonResponse;
            }

        }else{
            zoomTimeSlots1.setSlotOpenTime(zoomTimeSlotsDto.getSlotOpenTime());
            zoomTimeSlots1.setSlotCloseTime(zoomTimeSlotsDto.getSlotCloseTime());
            zoomTimeSlots2.setCommonStatus(zoomTimeSlotsDto.getCommonStatus());
            zoomTimeSlotsRepository.save(zoomTimeSlots2);
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Successfully Add");
            return commonResponse;
        }
        return commonResponse;
    }

    @Override
    public ZoomTimeSlots findById(String zoomTimeSlotId) {
        return zoomTimeSlotsRepository.findById(Long.valueOf(zoomTimeSlotId)).get();
    }

    @Override
    public CommonResponse getAll(String commonStatus) {
       CommonResponse commonResponse = new CommonResponse();
        List<ZoomTimeSlotsDto> zoomTimeSlotsDtoList = new ArrayList<>();
        CommonStatus commonStatus1 = CommonStatus.valueOf(commonStatus);

        try {
            Predicate<ZoomTimeSlots> filterOnStatus = zoomTimeSlots -> zoomTimeSlots.getCommonStatus() == commonStatus1;
            zoomTimeSlotsDtoList= zoomTimeSlotsRepository.findAll().stream().filter(filterOnStatus).map(this::castZoomTimeSlotIntoZoomTimeSlotDto).collect(Collectors.toList());

            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(zoomTimeSlotsDtoList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in ZoomTimeSlotService -> getAll(ActiveTimeSlots)" + e);
        }
       return commonResponse;
    }

    @Override
    public ZoomTimeSlotsDto findDetails(Long zoomTimeSlotId) {
        ZoomTimeSlotsDto zoomTimeSlotsDto = new ZoomTimeSlotsDto();
        ZoomTimeSlots timeSlots = zoomTimeSlotsRepository.findById(zoomTimeSlotId).get();
        zoomTimeSlotsDto.setSlotOpenTime(timeSlots.getSlotOpenTime());
        zoomTimeSlotsDto.setSlotCloseTime(timeSlots.getSlotCloseTime());
        zoomTimeSlotsDto.setCommonStatus(timeSlots.getCommonStatus());
        zoomTimeSlotsDto.setZoomTimeSlotId(String.valueOf(timeSlots.getZoomTimeSlotId()));
        return zoomTimeSlotsDto;
    }

    @Override
    public CommonResponse getFilteredTimeSlots(String commonStatus, int page, int size) {
        CommonResponse commonResponse = new CommonResponse();
        Pageable pageable = PageRequest.of(page, size);
        List<ZoomTimeSlotsDto> zoomTimeSlotsDtoList = new ArrayList<>();
        Page<ZoomTimeSlots> zoomTimeSlotsPage;

        try {

            // Convert String to Enum
            CommonStatus commonStatusEnum = CommonStatus.valueOf(commonStatus);


            zoomTimeSlotsPage = zoomTimeSlotsRepository.findByCommonStatus(commonStatusEnum, pageable);


            // Convert Employee entities to DTOs
            zoomTimeSlotsDtoList = zoomTimeSlotsPage.getContent().stream()
                    .map(this::castZoomTimeSlotIntoZoomTimeSlotDto)
                    .collect(Collectors.toList());

            // Prepare pagination details
            Map<String, Object> paginationDetails = new HashMap<>();
            paginationDetails.put("currentPage", zoomTimeSlotsPage.getNumber());
            paginationDetails.put("totalItems", zoomTimeSlotsPage.getTotalElements());
            paginationDetails.put("totalPages", zoomTimeSlotsPage.getTotalPages());

            // Set response payload properly
            commonResponse.setStatus(true);
            commonResponse.setCommonMessage("Time slot fetched successfully.");
            commonResponse.setPayload(Collections.singletonList(zoomTimeSlotsDtoList)); // Employee list
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
    public CommonResponse updateStatus(String zoomTimeSlotId, String commonStatus) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            ZoomTimeSlots zoomTimeSlots = zoomTimeSlotsRepository.findById(Long.valueOf(zoomTimeSlotId)).get();
            if (commonStatus.equals("ACTIVE")) {
                zoomTimeSlots.setCommonStatus(CommonStatus.ACTIVE);
                zoomTimeSlotsRepository.save(zoomTimeSlots);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Recover");
                return commonResponse;

            } else if (commonStatus.equals("INACTIVE")) {
                zoomTimeSlots.setCommonStatus(CommonStatus.INACTIVE);
                zoomTimeSlotsRepository.save(zoomTimeSlots);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Deleted");
                return commonResponse;
            } else if (commonStatus.equals("DELETED")) {
                zoomTimeSlotsRepository.delete(zoomTimeSlots);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Successfully Permanent Deleted");
                return commonResponse;
            }
        }catch (Exception e){
            commonResponse.setStatus(false);
            LOGGER.error("/***************** Exception in TimeSlotService -> updateStatus()" + e);

        }

        return commonResponse;
    }

    private List<String> zoomTimeSlotValidation(ZoomTimeSlotsDto zoomTimeSlotsDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(zoomTimeSlotsDto.getSlotOpenTime()))
            validationList.add("Open timeSlot field is empty");
        if (CommonValidation.stringNullValidation(zoomTimeSlotsDto.getSlotCloseTime()))
            validationList.add("Close timeSlot field is empty");

        return validationList;
    }

 public ZoomTimeSlotsDto castZoomTimeSlotIntoZoomTimeSlotDto(ZoomTimeSlots zoomTimeSlots) {
        ZoomTimeSlotsDto zoomTimeSlotsDto = new ZoomTimeSlotsDto();
        zoomTimeSlotsDto.setZoomTimeSlotId(String.valueOf(zoomTimeSlots.getZoomTimeSlotId()));
        zoomTimeSlotsDto.setSlotOpenTime(zoomTimeSlots.getSlotOpenTime());
        zoomTimeSlotsDto.setSlotCloseTime(zoomTimeSlots.getSlotCloseTime());
        return zoomTimeSlotsDto;
 }

}