package com.springzr.museio.services.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record TokenRequest(
        @NotBlank(message = "id is required")
        String id
) {
}
