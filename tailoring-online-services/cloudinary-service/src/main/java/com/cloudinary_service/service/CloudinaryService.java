package com.cloudinary_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary_service.exception.CloudinaryException;
import com.cloudinary_service.messaging.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final KafkaProducer kafkaProducer;

    public void uploadProfilePicture(byte[] imageBytes, String pictureId) throws IOException {
        try {
            String imageUrl = uploadFile(imageBytes);
            kafkaProducer.sendProfilePictureUrl(imageUrl, pictureId);
        } catch (IOException e) {
            throw new CloudinaryException(
                    "Failed to upload profile picture due to IO error.",
                    Collections.singletonList("Picture ID: " + pictureId)
            );
        } catch (Exception e) {
            throw new CloudinaryException(
                    "Unexpected error during profile picture upload",
                    Collections.singletonList("Picture ID: " + pictureId + ", Error: " + e.getMessage())
            );
        }
    }

    public String uploadFile(byte[] file) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new CloudinaryException(
                    "Failed to upload image to Cloudinary due to IO error.",
                    Collections.singletonList("Error: " + e.getMessage())
            );
        } catch (Exception e) {
            throw new CloudinaryException(
                    "Unexpected error during file upload to Cloudinary.",
                    Collections.singletonList("Error: " + e.getMessage())
            );
        }
    }
}
