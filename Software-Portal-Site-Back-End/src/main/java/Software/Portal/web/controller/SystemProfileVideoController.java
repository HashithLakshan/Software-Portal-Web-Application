package Software.Portal.web.controller;

import Software.Portal.web.service.SystemProfileVideoService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/systemProfileVideo")
@CrossOrigin
public class SystemProfileVideoController {

    private final SystemProfileVideoService systemProfileVideoService;

    @Autowired
    public SystemProfileVideoController(SystemProfileVideoService systemProfileVideoService) {
        this.systemProfileVideoService = systemProfileVideoService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public CommonResponse uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("systemProfileId") String systemProfileId) throws IOException {
        return systemProfileVideoService.saveVideo(file, systemProfileId);
    }


    @GetMapping("/{systemProfileId}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String systemProfileId) {
        return systemProfileVideoService.getVideo(systemProfileId)
                .map(video -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + video.getName() + "\"")
                        .contentType(org.springframework.http.MediaType.valueOf(video.getType()))
                        .body(video.getVideo()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
