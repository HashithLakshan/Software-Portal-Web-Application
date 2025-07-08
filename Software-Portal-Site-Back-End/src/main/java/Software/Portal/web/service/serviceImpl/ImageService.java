package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.Employee;
import Software.Portal.web.entity.Image;
import Software.Portal.web.entity.User;
import Software.Portal.web.repository.EmployeeRepository;
import Software.Portal.web.repository.ImageRepository;
import Software.Portal.web.repository.UserRepository;

import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

   private final EmployeeRepository employeeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

@Autowired
    public ImageService(ImageRepository imageRepository, EmployeeRepository employeeRepository) {
        this.imageRepository = imageRepository;

    this.employeeRepository = employeeRepository;
}

    public CommonResponse uploadImage(MultipartFile imageFile, String employeeId) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        try {
            // 1️⃣ Validate Image File
            if (imageFile == null || imageFile.isEmpty()) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("Please provide an image file");
                return commonResponse;
            }

            // 2️⃣ Check if User Exists
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            if (optionalEmployee.isEmpty()) {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("User not found");
                return commonResponse;
            }
            Employee employee = optionalEmployee.get();

            // 3️⃣ Find Existing Image (if any)
            Optional<Image> existingImageOptional = imageRepository.findByEmployeeEmployeeId(employeeId);

            if (existingImageOptional.isPresent()) {
                Image existingImage = existingImageOptional.get();
                existingImage.setName(imageFile.getOriginalFilename());
                existingImage.setType(imageFile.getContentType());
                existingImage.setImageData(ImageUtils.compressImage(imageFile.getBytes()));

                imageRepository.save(existingImage);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Employee updated successfully");

            } else {
                // ✅ Save New Image
                Image newImage = Image.builder()
                        .name(imageFile.getOriginalFilename())
                        .type(imageFile.getContentType())
                        .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                        .employee(employee)
                        .commonStatus(CommonStatus.ACTIVE)
                        .build();

                imageRepository.save(newImage);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("Image uploaded successfully");
            }

        } catch (Exception e) {
            LOGGER.error("Exception in ImageService -> uploadImage: " + e);
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Error while uploading image");
        }

        return commonResponse;
    }

    public byte[] downloadImage(String employeeId) throws IOException {
        Optional<Image> dbImage = imageRepository.findByEmployeeEmployeeId(employeeId);
        return dbImage.map(image -> {
            try {
                return ImageUtils.decompressImage(image.getImageData());
            } catch (DataFormatException | IOException exception) {
                throw new ContextedRuntimeException("Error downloading an image", exception)
                        .addContextValue("Image ID",  image.getId())
                        .addContextValue("Image name", employeeId);
            }
        }).orElse(null);
    }


}
