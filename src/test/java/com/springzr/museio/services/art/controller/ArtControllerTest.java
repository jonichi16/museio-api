package com.springzr.museio.services.art.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.art.model.Art;
import com.springzr.museio.services.art.model.response.ArtGetResponse;
import com.springzr.museio.services.art.model.response.ArtGetResponse.Pagination;
import com.springzr.museio.services.art.model.response.ArtResponse;
import com.springzr.museio.services.art.service.ArtService;
import com.springzr.museio.services.tag.model.Tag;
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

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ArtControllerTest {

    @Mock
    private ArtService artService;

    @InjectMocks
    private ArtController artController;

    @Test
    void getArtByCollectionId_shouldCallServiceOnceAndReturnSuccessResponse() {
        // given
        int collectionId = 1;
        int page = 1;
        int size = 10;

        List<Tag> tags = List.of(new Tag("drawing"));

        List<Art> mockArts = List.of(
                Art.builder().id(1L).title("Art 1").description("Desc 1").imageUrl("url1").tags(tags).build(),
                Art.builder().id(2L).title("Art 2").description("Desc 2").imageUrl("url2").tags(tags).build()
        );

        List<ArtResponse> mockArtResponses = mockArts.stream()
                .map(ArtResponse::fromEntity)
                .toList();

        Pagination pagination = new Pagination(size, page, 2L, 1);
        ArtGetResponse mockResponse = new ArtGetResponse(mockArtResponses, pagination);

        when(artService.getArtByCollectionId(collectionId, page, size)).thenReturn(mockResponse);

        // when
        ResponseEntity<MSResponse<ArtGetResponse>> responseEntity =
                artController.getArtByCollectionId(collectionId, page, size);

        // then
        verify(artService, times(1)).getArtByCollectionId(collectionId, page, size);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);

        MSResponse<?> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Arts retrieved successfully");

        ArtGetResponse data = (ArtGetResponse) Objects.requireNonNull(response.getData());
        assertThat(data.arts()).hasSize(2);
        assertThat(data.pagination().page()).isEqualTo(1);
    }

    @Test
    void shouldCreatePaginationFromPage() {
        // given
        List<Art> items = List.of(
                Art.builder().id(1L).title("Art 1").description("Desc 1").build(),
                Art.builder().id(2L).title("Art 2").description("Desc 2").build()
        );
        Page<Art> page = new PageImpl<>(items, PageRequest.of(0, 10), 20);

        // when
        Pagination pagination = new Pagination(page);

        // then
        assertThat(pagination.size()).isEqualTo(10);
        assertThat(pagination.page()).isEqualTo(1); // 1-based
        assertThat(pagination.totalElements()).isEqualTo(20);
        assertThat(pagination.totalPages()).isEqualTo(2);
    }

    @Test
    void shouldCreateArtGetResponseCorrectly() {
        // given
        List<ArtResponse> arts = List.of(
                ArtResponse.fromEntity(Art.builder().id(1L).title("Test Art").tags(List.of()).build())
        );
        Pagination pagination = new Pagination(10, 1, 1, 1);

        // when
        ArtGetResponse response = new ArtGetResponse(arts, pagination);

        // then
        assertThat(response.arts()).hasSize(1);
        assertThat(response.arts().get(0).title()).isEqualTo("Test Art");
        assertThat(response.pagination().size()).isEqualTo(10);
    }
}
