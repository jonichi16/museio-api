package com.springzr.museio.services.art.service.impl;

import com.springzr.museio.services.art.model.Art;
import com.springzr.museio.services.art.model.response.ArtGetResponse;
import com.springzr.museio.services.art.model.response.ArtResponse;
import com.springzr.museio.services.art.repository.ArtRepository;
import com.springzr.museio.services.art.service.ArtService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



/**
 * Service class that implements {@link ArtService}.
 */
@Service
@RequiredArgsConstructor
public class ArtServiceImpl implements ArtService {

    private final ArtRepository artRepository;

    @Override
    public ArtGetResponse getArtByCollectionId(Integer collectionId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Art> artsPage;

        if (collectionId == null) {
            artsPage = artRepository.findAll(pageable);
        } else {
            artsPage = artRepository.findByCollectionId(collectionId, pageable);
        }

        List<ArtResponse> artResponses = artsPage.getContent().stream()
                .map(ArtResponse::fromEntity)
                .toList();

        return new ArtGetResponse(
                artResponses,
                new ArtGetResponse.Pagination(artsPage)
        );
    }
}
