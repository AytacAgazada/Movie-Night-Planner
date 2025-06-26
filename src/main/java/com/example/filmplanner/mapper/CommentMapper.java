package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.entity.Comment;

public class CommentMapper {
    public static Comment toEntity(CommentDto dto) {
        if (dto == null) return null;
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setUserIdentifier(dto.getUserIdentifier());
        return comment;
    }
}