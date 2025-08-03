package com.springzr.museio.services.tag.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.tag.model.response.TagCountGetResponse;
import com.springzr.museio.services.tag.model.response.TagCountResponse;
import com.springzr.museio.services.tag.service.TagService;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @Test
    void getTagsByKeyword_shouldCallServiceOnceAndReturnSuccessResponse() {
        // given
        String keyword = "art";
        int page = 1;
        int size = 10;

        List<TagCountResponse> tags = List.of(
                new TagCountResponse(1L, "My Art", 5L),
                new TagCountResponse(2L, "Your Art", 3L)
        );

        TagCountGetResponse.Pagination pagination = new TagCountGetResponse.Pagination(size, page, 2L, 1);
        TagCountGetResponse responseData = new TagCountGetResponse(tags, pagination);

        when(tagService.getTagsByKeyword(keyword, page, size)).thenReturn(responseData);

        // when
        ResponseEntity<MSResponse<?>> responseEntity = tagController.getTagsByKeyWord(keyword, page, size);

        // then
        verify(tagService, times(1)).getTagsByKeyword(keyword, page, size);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);

        MSResponse<?> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Tags retrieved successfully");

        TagCountGetResponse data = (TagCountGetResponse) Objects.requireNonNull(response.getData());
        assertThat(data.pagination().page()).isEqualTo(1);
        assertThat(data.pagination().totalElements()).isEqualTo(2);
    }

    @Test
    void getTagsByKeyword_shouldReturnErrorResponseOnException() {
        // given
        String keyword = "error";
        int page = 1;
        int size = 10;

        when(tagService.getTagsByKeyword(keyword, page, size))
                .thenThrow(new RuntimeException("Database error"));

        // when
        ResponseEntity<MSResponse<?>> responseEntity = tagController.getTagsByKeyWord(keyword, page, size);

        // then
        verify(tagService, times(1)).getTagsByKeyword(keyword, page, size);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);

        MSResponse<?> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(500);
        assertThat(response.getMessage()).isEqualTo("Failed to retrieve tags");
    }

    @Test
    void shouldCreatePaginationFromPage() {
        // given
        List<TagCountResponse> items = List.of(
                new TagCountResponse(1L, "My Art", 5L),
                new TagCountResponse(2L, "Your Art", 3L)
        );
        Page<TagCountResponse> page = new PageImpl<>(items, PageRequest.of(0, 10), 20);

        // when
        TagCountGetResponse.Pagination pagination = new TagCountGetResponse.Pagination(page);

        // then
        assertThat(pagination.size()).isEqualTo(10);
        assertThat(pagination.page()).isEqualTo(1); // 1-based index
        assertThat(pagination.totalElements()).isEqualTo(20);
        assertThat(pagination.totalPages()).isEqualTo(2);
    }

    @Test
    void shouldCreateTagCountGetResponseCorrectly() {
        // given
        List<TagCountResponse> tags = List.of(
                new TagCountResponse(1L, "Art", 5L)
        );
        TagCountGetResponse.Pagination pagination = new TagCountGetResponse.Pagination(10, 1, 1, 1);

        // when
        TagCountGetResponse response = new TagCountGetResponse(tags, pagination);

        // then
        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags().getFirst().name()).isEqualTo("Art");
        assertThat(response.pagination().size()).isEqualTo(10);
        assertThat(response.pagination().page()).isEqualTo(1);
    }
}
