package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.model.BookingSearchState;

public final class BookingSearchStateConverter implements Converter<String, BookingSearchState> {
    @Override
    public BookingSearchState convert(String source) {
        return BookingSearchState.valueOf(source);
    }
}
