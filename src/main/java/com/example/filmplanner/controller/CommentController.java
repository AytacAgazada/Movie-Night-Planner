package com.example.filmplanner.controller;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.entity.Comment;
import com.example.filmplanner.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{filmId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long filmId, @Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.addComment(filmId, dto));
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long filmId) {
        return ResponseEntity.ok(commentService.getCommentsForFilm(filmId));
    }
}
