package com.cloudinary_service.aspect;

import com.cloudinary_service.dto.ErrorResponse;
import com.cloudinary_service.exception.CloudinaryException;
import com.cloudinary_service.exception.KafkaProducerException;
import com.cloudinary_service.exception.KafkaConsumerException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;

@Aspect
@Component
public class ExceptionLoggerAspect {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLoggerAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.cloudinary_service..*(..))",
            throwing = "exception"
    )
    public void logException(Exception exception) {
        ErrorResponse errorResponse;

        if (exception instanceof CloudinaryException cloudinaryException) {
            errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    "Cloudinary Service Error",
                    cloudinaryException.getMessage(),
                    cloudinaryException.getDetails()
            );
        } else if (exception instanceof KafkaProducerException kafkaProducerException) {
            errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    "Kafka Producer Error",
                    kafkaProducerException.getMessage(),
                    kafkaProducerException.getDetails()
            );
        } else if (exception instanceof KafkaConsumerException kafkaConsumerException) {
            errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    "Kafka Consumer Error",
                    kafkaConsumerException.getMessage(),
                    kafkaConsumerException.getDetails()
            );
        } else {
            errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    "Cloudinary Service Error",
                    "UNKNOWN_ERROR",
                    Collections.singletonList("No additional details available")
            );
        }

        log.error("Captured exception: {}", errorResponse);
    }
}
