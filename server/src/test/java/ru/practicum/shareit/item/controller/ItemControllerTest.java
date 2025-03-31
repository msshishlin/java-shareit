package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Тесты для контроллера для работы с вещами.
 */
@WebMvcTest(controllers = ItemController.class)
public final class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    private final CreateItemDto createItemDto = CreateItemDto.builder()
            .name("Ударная дрель")
            .description("Описание ударной дрели")
            .available(true)
            .requestId(1L)
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("Ударная дрель")
            .description("Описание ударной дрели")
            .available(true)
            .build();

    private final ExtendedItemDto extendedItemDto = ExtendedItemDto.builder()
            .id(1L)
            .name("Ударная дрель")
            .description("Описание ударной дрели")
            .available(true)
            .build();

    private final UpdateItemDto updateItemDto = UpdateItemDto.builder()
            .name("Ударная дрель")
            .description("Описание ударной дрели")
            .available(true)
            .build();

    private final CreateCommentDto createCommentDto = CreateCommentDto.builder()
            .text("Комментарий")
            .build();

    private final Comment comment = Comment.builder()
            .id(1L)
            .text("Комментарий")
            .item(Item.builder().build())
            .author(User.builder().build())
            .build();


    @Test
    void createItemTest() throws Exception {
        Mockito.when(itemService.createItem(Mockito.any()))
                .thenReturn(item);

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(createItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable())));
    }

    @Test
    void getItemsTest() throws Exception {
        Mockito.when(itemService.getUserItems(Mockito.anyLong()))
                .thenReturn(List.of(extendedItemDto));

        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(extendedItemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(extendedItemDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(extendedItemDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Matchers.is(extendedItemDto.getAvailable())));
    }

    @Test
    void getItemByIdTest() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong()))
                .thenReturn(extendedItemDto);

        mvc.perform(MockMvcRequestBuilders.get("/items/" + 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(extendedItemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(extendedItemDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(extendedItemDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(extendedItemDto.getAvailable())));
    }

    @Test
    void searchTest() throws Exception {
        Mockito.when(itemService.search(Mockito.anyString()))
                .thenReturn(List.of(item));

        mvc.perform(MockMvcRequestBuilders.get("/items/search?text=дрель")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Matchers.is(item.getAvailable())));
    }

    @Test
    void updateItemTest() throws Exception {
        Mockito.when(itemService.updateItem(Mockito.any()))
                .thenReturn(item);

        mvc.perform(MockMvcRequestBuilders.patch("/items/" + 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(updateItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(item.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(item.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(item.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Matchers.is(item.getAvailable())));
    }

    @Test
    void addCommentToItemTest() throws Exception {
        Mockito.when(itemService.addCommentToItem(Mockito.any()))
                .thenReturn(comment);

        mvc.perform(MockMvcRequestBuilders.post("/items/" + 1 + "/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(createCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(comment.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is(comment.getText())));
    }
}
