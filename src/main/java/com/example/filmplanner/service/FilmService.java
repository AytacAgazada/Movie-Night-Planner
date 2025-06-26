package com.example.filmplanner.service;

import com.example.filmplanner.dto.FilmSuggestionDto;
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.enums.Genre;
import com.example.filmplanner.repository.FilmRepository;
import com.example.filmplanner.repository.PollRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final PollRepository pollRepository;

    public FilmService(FilmRepository filmRepository, PollRepository pollRepository) {
        this.filmRepository = filmRepository;
        this.pollRepository = pollRepository;
    }

    public Film addFilmToPoll(Long pollId, FilmSuggestionDto dto) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        Film film = new Film();
        film.setTitle(dto.getTitle());
        film.setGenre(dto.getGenre());
        film.setPoll(poll);

        return filmRepository.save(film);
    }

    public List<Film> getFilmsByPoll(Long pollId) {
        return filmRepository.findByPollId(pollId);
    }

    public List<Film> getFilmsByGenre(Genre genre) {
        return filmRepository.findByGenre(genre);
    }

    public List<Film> getFilmsByPollAndGenre(Long pollId, Genre genre) {
        return filmRepository.findByPollIdAndGenre(pollId, genre);
    }
}