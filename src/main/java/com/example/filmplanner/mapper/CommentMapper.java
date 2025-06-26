package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentDto dto) {
        if (dto == null) return null;

        Comment comment = new Comment();
        comment.setUsername(dto.getUsername());
        comment.setMessage(dto.getMessage());
        comment.setCreatedAt(java.time.LocalDateTime.now());
        return comment;
    }

    public CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        CommentDto dto = new CommentDto();
        dto.setUsername(comment.getUsername());
        dto.setMessage(comment.getMessage());
        return dto;
    }
}
