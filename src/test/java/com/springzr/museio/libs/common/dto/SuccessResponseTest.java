package com.springzr.museio.libs.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class SuccessResponseTest {
    @Test
    void builder_shouldCreateTheCorrectObject() {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        MSResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        // then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void builder_shouldHaveNullErrorAndErrorCode() {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        MSResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        // then
        assertThat(response.getError()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    void builder_shouldNotHaveErrorAndErrorCodeFields() throws Exception {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        MSResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("data");
        assertThat(json).doesNotContain("error");
        assertThat(json).doesNotContain("errorCode");
    }

    @Test
    void testSuccessResponseToString() {
        SuccessResponse<String> response = new SuccessResponse<>(
                200,
                "Operation successful",
                "Hello, World!"
        );

        String toString = response.toString();

        assertThat(toString)
                .startsWith("SuccessResponse{")
                .contains("success=true")
                .contains("code=200")
                .contains("message=\"Operation successful\"")
                .contains("data=Hello, World!");
    }
}
