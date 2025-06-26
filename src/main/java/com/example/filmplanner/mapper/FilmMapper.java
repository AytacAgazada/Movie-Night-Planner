package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.FilmSuggestionDto;
import com.example.filmplanner.entity.Film;

public class FilmMapper {
    public static FilmSuggestionDto toDto(Film film) {
        if (film == null) return null;
        FilmSuggestionDto dto = new FilmSuggestionDto();
        dto.setTitle(film.getTitle());
        dto.setGenre(film.getGenre());
        return dto;
    }

    public static Film toEntity(FilmSuggestionDto dto) {
        if (dto == null) return null;
        Film film = new Film();
        film.setTitle(dto.getTitle());
        film.setGenre(dto.getGenre());
        return film;
    }
}