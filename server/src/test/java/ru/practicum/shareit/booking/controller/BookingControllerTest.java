package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(controllers = BookingController.class)
public final class BookingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    private final CreateBookingDto createBookingDto = CreateBookingDto.builder()
            .itemId(1L)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .booker(User.builder().build())
            .item(Item.builder().build())
            .build();

    @Test
    void createItemTest() throws Exception {
        Mockito.when(bookingService.createBooking(Mockito.any()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(createBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(booking.getStatus().name())));
    }

    @Test
    void getBookingsTest() throws Exception {
        Mockito.when(bookingService.getBookings(Mockito.anyLong(), Mockito.any(BookingSearchState.class)))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(booking.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(booking.getStatus().name())));
    }

    @Test
    void getOwnerBookingsTest() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(Mockito.anyLong(), Mockito.any(BookingSearchState.class)))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(booking.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(booking.getStatus().name())));
    }

    @Test
    void getBookingTest() throws Exception {
        Mockito.when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/" + 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(booking.getStatus().name())));
    }

    @Test
    void approveBookingTest() throws Exception {
        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/" + 1 + "?approved=true")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(booking.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(booking.getStatus().name())));
    }
}
