package Software.Portal.web.service;

import Software.Portal.web.entity.SystemProfileVideo;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface SystemProfileVideoService {

    CommonResponse saveVideo(MultipartFile file, String systemProfileId) throws IOException;

    Optional<SystemProfileVideo> getVideo(String systemProfileId);
}

