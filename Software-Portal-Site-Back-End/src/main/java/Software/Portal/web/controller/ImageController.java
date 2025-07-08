package Software.Portal.web.controller;

import Software.Portal.web.service.serviceImpl.ImageService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/image")
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/save")
    public CommonResponse uploadImage(@RequestParam("image") MultipartFile file,@RequestParam ("employeeId") String employeeId) throws IOException {
      return imageService.uploadImage(file,employeeId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<?> downloadImage(@PathVariable String employeeId) throws IOException {
        byte[] imageData = imageService.downloadImage(employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(IMAGE_PNG_VALUE))
                .body(imageData);
    }

}