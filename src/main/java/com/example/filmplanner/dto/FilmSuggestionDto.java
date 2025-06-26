package com.example.filmplanner.dto;

import com.example.filmplanner.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmSuggestionDto {

    @NotBlank(message = "Film title must not be empty")
    private String title;

    @NotNull(message = "Genre must be provided")
    private Genre genre;

    @NotNull(message = "Poll ID must be provided")
    private Long pollId;
}
