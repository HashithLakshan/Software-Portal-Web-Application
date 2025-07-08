package Software.Portal.web.controller;

import Software.Portal.web.entity.SystemProfileImage;
import Software.Portal.web.repository.SystemProfileImageRepository;
import Software.Portal.web.service.serviceImpl.SystemProfileImageService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/SystemProfileImages")
@CrossOrigin
public class SystemProfileImageController {


    private  final SystemProfileImageService systemProfileImageService;

    private final SystemProfileImageRepository systemProfileImageRepository;

    @Autowired
    public SystemProfileImageController(SystemProfileImageService systemProfileImageService, SystemProfileImageRepository systemProfileImageRepository) {
        this.systemProfileImageService = systemProfileImageService;
        this.systemProfileImageRepository = systemProfileImageRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public CommonResponse uploadImage(@RequestParam("image") MultipartFile file, @RequestParam ("systemProfileId") String systemProfileId) throws IOException {
         return systemProfileImageService.uploadImage(file,systemProfileId);

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public CommonResponse updateImage(@RequestParam("image") MultipartFile file,
                                      @RequestParam ("id") Long id
    ) throws IOException {
        return systemProfileImageService.updateImage(file,id);

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public CommonResponse deleteImage(@RequestParam ("id") String id) {
        CommonResponse commonResponse = new CommonResponse();
        SystemProfileImage systemProfileImage = systemProfileImageRepository.findById(Long.valueOf(id)).get();
        systemProfileImageRepository.delete(systemProfileImage);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Successfully deleted system profile image");
        return  commonResponse;
    }

    @GetMapping("/{systemProfilesId}")
    public ResponseEntity<?> downloadAllImagesAsJson(@PathVariable String systemProfilesId) {
        List<Map<String, Object>> images = systemProfileImageService.downloadImages2(systemProfilesId);

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No images found for SystemProfile ID: " + systemProfilesId);
        }

        // Add placeNumber to each image object
        for (int i = 0; i < images.size(); i++) {
            images.get(i).put("placeNumber", i + 1); // Assigning place number
            images.get(i).put("systemProfilesId", systemProfilesId);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(images);
    }



    @GetMapping("/ll/{systemProfilesId}")
    public ResponseEntity<?> downloadAllImagesAsJsona(@PathVariable String systemProfilesId) {
        List<byte[]> imageDataList = systemProfileImageService.downloadImages(systemProfilesId);

        if (imageDataList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No images found for SystemProfile ID: " + systemProfilesId);
        }

        List<String> base64Images = imageDataList.stream()
                .map(imageData -> Base64.getEncoder().encodeToString(imageData))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(base64Images);
    }



}
