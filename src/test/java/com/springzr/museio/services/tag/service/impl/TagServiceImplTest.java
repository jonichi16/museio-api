package com.springzr.museio.services.tag.service.impl;

import com.springzr.museio.services.tag.model.response.TagCountGetResponse;
import com.springzr.museio.services.tag.model.response.TagCountResponse;
import com.springzr.museio.services.tag.repository.TagRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getTagsByKeyword_shouldReturnPaginatedTags() {
        // given
        String keyword = "art";
        int page = 1;
        int size = 10;

        List<TagCountResponse> tagList = List.of(
                new TagCountResponse(1L, "Art", 5L),
                new TagCountResponse(2L, "Artist", 3L)
        );

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<TagCountResponse> tagPage = new PageImpl<>(tagList, pageRequest, tagList.size());

        when(tagRepository.searchTagsWithCountFuzzy(keyword, pageRequest)).thenReturn(tagPage);

        // when
        TagCountGetResponse response = tagService.getTagsByKeyword(keyword, page, size);

        // then
        verify(tagRepository, times(1)).searchTagsWithCountFuzzy(keyword, pageRequest);

        assertThat(response.tags()).hasSize(2);
        assertThat(response.tags().get(0).name()).isEqualTo("Art");
        assertThat(response.tags().get(1).name()).isEqualTo("Artist");

        TagCountGetResponse.Pagination pagination = response.pagination();
        assertThat(pagination.page()).isEqualTo(1);
        assertThat(pagination.size()).isEqualTo(10);
        assertThat(pagination.totalElements()).isEqualTo(2);
        assertThat(pagination.totalPages()).isEqualTo(1);
    }
}
