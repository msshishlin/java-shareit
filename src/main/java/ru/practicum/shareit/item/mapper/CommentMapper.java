package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public class CommentMapper {
    public static Comment mapToComment(long authorId, long itemId, CreateCommentDto dto) {
        return Comment.builder()
                .text(dto.getText())
                .item(Item.builder().id(itemId).build())
                .author(User.builder().id(authorId).build())
                .created(LocalDateTime.now())
                .build();

    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(ItemMapper.mapToItemDto(comment.getItem()))
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Collection<CommentDto> mapToCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream().map(CommentMapper::mapToCommentDto).toList();
    }
}
