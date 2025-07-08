package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.SystemProfile;
import Software.Portal.web.entity.SystemProfileVideo;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.repository.SystemProfileVideoRepository;
import Software.Portal.web.service.SystemProfileVideoService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Optional;

@Service
public class SystemProfileVideoServiceImpl implements SystemProfileVideoService {

    private final SystemProfileVideoRepository systemProfileVideoRepository;

    private final SystemProfileRepository systemProfileRepository;

    @Autowired
    public SystemProfileVideoServiceImpl(SystemProfileVideoRepository systemProfileVideoRepository, SystemProfileRepository systemProfileRepository) {
        this.systemProfileVideoRepository = systemProfileVideoRepository;
        this.systemProfileRepository = systemProfileRepository;
    }

    @Override
    public CommonResponse saveVideo(MultipartFile file, String systemProfileId) throws IOException  {
        CommonResponse commonResponse = new CommonResponse();
        if (file == null || file.isEmpty()) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Please provide an video file");
            return commonResponse;
        }
        Optional<SystemProfile> systemProfileOpt = systemProfileRepository.findById(Long.valueOf(systemProfileId));

        if (systemProfileOpt.isPresent()) {
            long imageCount = systemProfileVideoRepository.countBySystemProfileSystemProfilesId(Long.valueOf(systemProfileId));
            if (imageCount < 2) {
              Optional <SystemProfileVideo> systemProfileVideo = systemProfileVideoRepository.findBySystemProfileSystemProfilesId(Long.valueOf(systemProfileId));
                if(systemProfileVideo.isPresent()) {
                    SystemProfileVideo existingVideo = systemProfileVideo.get();
                    existingVideo.setVideo(file.getBytes());
                    existingVideo.setType(file.getContentType());
                    existingVideo.setName(file.getOriginalFilename());
                    systemProfileVideoRepository.save(existingVideo);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Successfully updated video");
                    return commonResponse;
                }else {

                    SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfileId))
                            .orElseThrow(() -> new IllegalArgumentException("SystemProfile not found with ID: " + systemProfileId));
                    SystemProfileVideo video = new SystemProfileVideo();
                    video.setName(file.getOriginalFilename());
                    video.setType(file.getContentType());
                    video.setVideo(file.getBytes());
                    video.setSystemProfile(systemProfile);
                    video.setCommonStatus(CommonStatus.ACTIVE);
                    systemProfileVideoRepository.save(video);
                    commonResponse.setStatus(true);
                    commonResponse.setCommonMessage("Successfully saved video");
                    return commonResponse;
                }
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Maximum upload video count is 1");
                return commonResponse;
            }

        }else {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("You are not fill the Profile Information's");
            return commonResponse;
        }

    }

    @Override
    public Optional<SystemProfileVideo> getVideo(String systemProfileId) {
        return systemProfileVideoRepository.findBySystemProfileSystemProfilesId(Long.valueOf(systemProfileId));
    }
}
