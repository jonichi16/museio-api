package com.springzr.museio.services.collection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.services.auth.config.JwtAuthFilter;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class CollectionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CollectionService collectionService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void getCollectionsByPortfolio_shouldReturn200Ok() throws Exception {

        CollectionResponse mockResponse = new CollectionResponse(
                List.of(),
                new CollectionResponse.Pagination(10, 1, 0, 0)
        );

        when(collectionService.getCollectionsByPortfolio(anyString(), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/collections")
                        .param("portfolio", "PORTFOLIO_VISUAL")
                        .param("page", "1")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("getCollectionsByPortfolioSuccess",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("Indicates if the request was successful"),
                                fieldWithPath("code").description("HTTP status code of the response"),
                                fieldWithPath("message").description("Descriptive message about the result"),
                                fieldWithPath("data.collections").description("List of collections returned"),
                                fieldWithPath("data.pagination.size").description("Number of items per page"),
                                fieldWithPath("data.pagination.page").description("Current page number (1-based)"),
                                fieldWithPath("data.pagination.totalElements").description("Total number of collections available"),
                                fieldWithPath("data.pagination.totalPages").description("Total number of pages"),
                                fieldWithPath("timestamp").description("Time at which the response was generated")
                        )
                ));
    }

    @Test
    void createCollection_shouldReturn201Created() throws Exception {
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Sample Title");
        request.setDescription("A test description.");
        request.setPortfolio("PORTFOLIO_VISUAL");

        Collection mockCollection = new Collection();
        mockCollection.setId(123L);

        when(collectionService.createCollection(any(CollectionRequest.class)))
                .thenReturn(mockCollection);

        mockMvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("createCollectionSuccess",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}