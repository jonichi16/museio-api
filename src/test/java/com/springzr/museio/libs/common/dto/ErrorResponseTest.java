package com.springzr.museio.libs.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ErrorResponseTest {

    @Test
    void builder_shouldCreateTheCorrectObject() {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        MSResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        // then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getError()).isEqualTo(error);
        assertThat(response.getErrorCode()).isEqualTo(errorCode);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void builder_shouldHaveNullValueForData() {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        MSResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        // then
        assertThat(response.getData()).isNull();
    }

    @Test
    void builder_shouldNotHaveDataField() throws Exception {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        MSResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("error");
        assertThat(json).contains("errorCode");
        assertThat(json).doesNotContain("data");
    }

    @Test
    void testErrorResponseToString() {
        ErrorResponse<String> response = new ErrorResponse<>(
                400,
                "Bad Request",
                "ERR400",
                "Invalid ID"
        );

        String toString = response.toString();

        assertThat(toString)
                .startsWith("ErrorResponse{")
                .contains("success=false")
                .contains("code=400")
                .contains("message=\"Bad Request\"")
                .contains("errorCode=\"ERR400\"")
                .contains("error=Invalid ID");
    }

}
