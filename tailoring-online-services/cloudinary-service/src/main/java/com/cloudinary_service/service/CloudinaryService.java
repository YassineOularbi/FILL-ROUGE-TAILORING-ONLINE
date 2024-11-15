package com.cloudinary_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary_service.messaging.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during profile picture upload", e);
        }
    }

    public String uploadFile(byte[] file) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }
}