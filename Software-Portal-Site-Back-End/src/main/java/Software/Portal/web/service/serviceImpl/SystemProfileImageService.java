package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.*;
import Software.Portal.web.repository.SystemProfileImageRepository;
import Software.Portal.web.repository.SystemProfileRepository;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

@Service
public class SystemProfileImageService {

    private final SystemProfileImageRepository systemProfileImageRepository;

    private final SystemProfileRepository systemProfileRepository;


    @Autowired
    public SystemProfileImageService(SystemProfileImageRepository systemProfileImageRepository, SystemProfileRepository systemProfileRepository) {
        this.systemProfileImageRepository = systemProfileImageRepository;
        this.systemProfileRepository = systemProfileRepository;
    }

    public CommonResponse uploadImage(MultipartFile imageFile, String systemProfileId) throws IOException {
    // Validate the file
       CommonResponse commonResponse = new CommonResponse();
       if (imageFile == null || imageFile.isEmpty()) {
           commonResponse.setStatus(false);
           commonResponse.setCommonMessage("Please provide an image file");
           return commonResponse;
       }
        Optional<SystemProfile> systemProfileOpt = systemProfileRepository.findById(Long.valueOf(systemProfileId));
        if (systemProfileOpt.isPresent()) {
        long imageCount = systemProfileImageRepository.countBySystemProfileSystemProfilesId(Long.valueOf(systemProfileId));

    if (imageCount < 6) {
        SystemProfile systemProfile = systemProfileRepository.findById(Long.valueOf(systemProfileId)).get();
        var imageToSave = SystemProfileImage.builder()
                .placeImage(imageCount + 1)
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .systemProfile(systemProfile)
                .commonStatus(CommonStatus.ACTIVE)
                .build();
        systemProfileImageRepository.save(imageToSave);
        commonResponse.setStatus(true);
        commonResponse.setCommonMessage("Image uploaded successfully");
        return commonResponse;
    } else {
        commonResponse.setStatus(false);
        commonResponse.setCommonMessage("Maximum upload Photo count is 6");
        return commonResponse;
    }
}else{
    commonResponse.setStatus(false);
    commonResponse.setCommonMessage("You are not fill the step 1 page");
    return commonResponse;
}


    }


    public List<Map<String, Object>> downloadImages2(String systemProfilesId) {
        // Retrieve all images for the given SystemProfile ID
        List<SystemProfileImage> dbImages = systemProfileImageRepository.findAllBySystemProfileSystemProfilesId(Long.valueOf(systemProfilesId));

        if (dbImages.isEmpty()) {
            throw new IllegalArgumentException("No images found for SystemProfile ID: " + systemProfilesId);
        }

        // Sort images by 'placeImage' in ascending order
        dbImages.sort(Comparator.comparingLong(SystemProfileImage::getPlaceImage));

        // Create a list of maps to store image ID and Base64 image
        List<Map<String, Object>> imageList = new ArrayList<>();

        for (SystemProfileImage image : dbImages) {
            try {
                byte[] decompressedImage = ImageUtils.decompressImageList(image.getImageData());
                String base64Image = Base64.getEncoder().encodeToString(decompressedImage);

                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("imageId", image.getId()); // Include image ID
                imageMap.put("base64Image", base64Image); // Include Base64 image data

                imageList.add(imageMap);
            } catch (DataFormatException | IOException exception) {
                throw new RuntimeException("Error decompressing image for ID: " + image.getId(), exception);
            }
        }

        return imageList;
    }

    public List<byte[]> downloadImages(String systemProfilesId) {
        // Retrieve all images for the given SystemProfile ID
        List<SystemProfileImage> dbImages = systemProfileImageRepository.findAllBySystemProfileSystemProfilesId(Long.valueOf(systemProfilesId));

        if (dbImages.isEmpty()) {
            throw new IllegalArgumentException("No images found for SystemProfile ID: " + systemProfilesId);
        }

        // Sort the images by 'placeImage' in ascending order (from 1 to 6)
        dbImages.sort(Comparator.comparingLong(SystemProfileImage::getPlaceImage));

        // Decompress each image and return the list of byte arrays
        return dbImages.stream().map(systemProfileImage -> {
            try {
                return ImageUtils.decompressImageList(systemProfileImage.getImageData());
            } catch (DataFormatException | IOException exception) {
                throw new RuntimeException("Error decompressing image for ID: " + systemProfileImage.getId(), exception);
            }
        }).toList();
    }









    public CommonResponse updateImage(MultipartFile file, Long id) throws IOException {
CommonResponse commonResponse = new CommonResponse();
    if (file.isEmpty()) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Please provide an image file");
            return commonResponse;
        }

        // Fetch the image entity by placeImage ID from the repository
        SystemProfileImage existingImage = systemProfileImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " ));

        // Convert the MultipartFile to byte array
        ImageUtils.compressImage(file.getBytes());
        // Update the image bytes in the existing image object
        existingImage.setImageData( ImageUtils.compressImage(file.getBytes()));
        // Assuming your entity has a setImage() method

        // Save the updated image object back to the database
        systemProfileImageRepository.save(existingImage);

commonResponse.setStatus(true);
commonResponse.setCommonMessage("Image updated successfully");
return commonResponse;
    }




}
