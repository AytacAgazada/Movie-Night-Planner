package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.FilmSuggestionDto;
import com.example.filmplanner.entity.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public Film toEntity(FilmSuggestionDto dto) {
        if (dto == null) return null;

        Film film = new Film();
        film.setTitle(dto.getTitle());
        film.setGenre(dto.getGenre());
        return film;
    }

    public FilmSuggestionDto toDto(Film film) {
        if (film == null) return null;

        FilmSuggestionDto dto = new FilmSuggestionDto();
        dto.setTitle(film.getTitle());
        dto.setGenre(film.getGenre());
        dto.setPollId(film.getPoll() != null ? film.getPoll().getId() : null);
        return dto;
    }
}
