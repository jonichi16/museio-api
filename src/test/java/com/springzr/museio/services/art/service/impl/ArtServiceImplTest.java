package com.springzr.museio.services.art.service.impl;

import com.springzr.museio.services.art.model.Art;
import com.springzr.museio.services.art.model.response.ArtGetResponse;
import com.springzr.museio.services.art.repository.ArtRepository;
import com.springzr.museio.services.tag.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ArtServiceImplTest {

    @Mock
    private ArtRepository artRepository;

    @InjectMocks
    private ArtServiceImpl artService;

    @Test
    void getArtByCollectionId_shouldReturnAllArts_whenCollectionIdIsNull() {
        // given
        int page = 1, size = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Tag> tags = List.of(new Tag("drawing"),new Tag("color"));

        List<Art> arts = List.of(
                Art.builder().id(1L).title("Art 1").description("Description 1").tags(tags).build(),
                Art.builder().id(2L).title("Art 2").description("Description 2").tags(tags).build()
        );
        Page<Art> pageResult = new PageImpl<>(arts, pageRequest, arts.size());

        // when
        when(artRepository.findAll(pageRequest)).thenReturn(pageResult);
        ArtGetResponse response = artService.getArtByCollectionId(null, page, size);

        // then
        assertThat(response.arts()).hasSize(2);
        assertThat(response.arts().getFirst().title()).isEqualTo("Art 1");
        assertThat(response.arts().get(1).title()).isEqualTo("Art 2");
        verify(artRepository).findAll(pageRequest);
    }

    @Test
    void getArtByCollectionId_shouldCallFindByCollectionId_whenValidCollectionIdIsGiven() {
        // given
        int collectionId = 99;
        int page = 1, size = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Tag> tags = List.of(new Tag("drawing"),new Tag("color"));

        List<Art> arts = List.of(
                Art.builder().id(1L).title("Art 1").description("Description 1").tags(tags).build()
        );
        Page<Art> pageResult = new PageImpl<>(arts, pageRequest, arts.size());

        when(artRepository.findByCollectionId(collectionId, pageRequest)).thenReturn(pageResult);

        // when
        ArtGetResponse response = artService.getArtByCollectionId(collectionId, page, size);

        // then
        assertThat(response.arts()).hasSize(1);
        assertThat(response.arts().getFirst().title()).isEqualTo("Art 1");
        verify(artRepository).findByCollectionId(collectionId, pageRequest);
    }
}
