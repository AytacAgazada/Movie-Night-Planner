// package com.example.filmplanner.controller;
//
//import com.example.filmplanner.dto.FilmSuggestionDto;
//import com.example.filmplanner.entity.Film;
//import com.example.filmplanner.enums.Genre;
//import com.example.filmplanner.service.FilmService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/films")
//@RequiredArgsConstructor
//public class FilmController {
//
//    private final FilmService filmService;
//
//    @PostMapping("/{pollId}")
//    public ResponseEntity<Film> addFilm(@PathVariable Long pollId,
//                                        @Valid @RequestBody FilmSuggestionDto filmDto) {
//        Film film = filmService.addFilmToPoll(pollId, filmDto);
//        return ResponseEntity.ok(film);
//    }
//
//    @GetMapping("/poll/{pollId}")
//    public ResponseEntity<List<Film>> getFilmsByPoll(@PathVariable Long pollId) {
//        return ResponseEntity.ok(filmService.getFilmsByPoll(pollId));
//    }
//
//    @GetMapping("/genre/{genre}")
//    public ResponseEntity<List<Film>> getByGenre(@PathVariable Genre genre) {
//        return ResponseEntity.ok(filmService.getFilmsByGenre(genre));
//    }
//
//    @GetMapping("/poll/{pollId}/genre/{genre}")
//    public ResponseEntity<List<Film>> getByPollAndGenre(@PathVariable Long pollId,
//                                                        @PathVariable Genre genre) {
//        return ResponseEntity.ok(filmService.getFilmsByPollAndGenre(pollId, genre));
//    }
//}
