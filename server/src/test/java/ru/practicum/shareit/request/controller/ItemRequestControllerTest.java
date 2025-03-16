package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Тесты для контроллера для работы с запросами вещей.
 */
@WebMvcTest(controllers = ItemRequestController.class)
public final class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService requestService;

    private final CreateItemRequestDto createRequestDto = CreateItemRequestDto.builder()
            .description("Ударная дрель")
            .build();

    private final ItemRequest request = ItemRequest.builder()
            .id(1L)
            .description("Ударная дрель")
            .build();

    private final ItemRequestWithItemsDto requestWithItemsDto = ItemRequestWithItemsDto.builder()
            .id(1L)
            .description("Ударная дрель")
            .items(new ArrayList<>())
            .build();

    @Test
    void createItemRequestTest() throws Exception {
        Mockito.when(requestService.createItemRequest(Mockito.any()))
                .thenReturn(request);

        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(createRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(request.getDescription())));
    }

    @Test
    void getUserRequestsTest() throws Exception {
        Mockito.when(requestService.getUserItemRequests(Mockito.anyLong()))
                .thenReturn(List.of(request));

        mvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(request.getDescription())));
    }

    @Test
    void getOtherUsersItemRequestsTest() throws Exception {
        Mockito.when(requestService.getOtherUsersItemRequests(Mockito.anyLong()))
                .thenReturn(List.of(request));

        mvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(request.getDescription())));
    }

    @Test
    void getRequestTest() throws Exception {
        Mockito.when(requestService.getRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestWithItemsDto);

        mvc.perform(MockMvcRequestBuilders.get("/requests/" + 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(request.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }
}
