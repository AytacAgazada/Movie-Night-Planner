package com.example.filmplanner.service;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.entity.Comment;
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.repository.CommentRepository;
import com.example.filmplanner.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FilmRepository filmRepository;

    public CommentService(CommentRepository commentRepository, FilmRepository filmRepository) {
        this.commentRepository = commentRepository;
        this.filmRepository = filmRepository;
    }

    public Comment addComment(Long filmId, CommentDto commentDto) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new RuntimeException("Film not found"));

        Comment comment = new Comment();
        comment.setFilm(film);
        comment.setText(commentDto.getText());
        comment.setUserIdentifier(commentDto.getUserIdentifier());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForFilm(Long filmId) {
        return commentRepository.findByFilmId(filmId);
    }
}