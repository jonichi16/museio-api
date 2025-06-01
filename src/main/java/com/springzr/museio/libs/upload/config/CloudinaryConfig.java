package com.springzr.museio.libs.upload.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures applications cloudinary service.
 *
 */
@Configuration
public class CloudinaryConfig {

    @Value("${app.cloudinary-url}")
    private String cloudinaryUrl;

    /**
     * Instantiate a new {@link Cloudinary} object.
     *
     * @return a {@link Cloudinary} object used to upload images
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(cloudinaryUrl);
    }

}
