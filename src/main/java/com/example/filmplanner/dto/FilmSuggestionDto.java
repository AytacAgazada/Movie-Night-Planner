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

    @NotBlank(message = "Film title is mandatory")
    private String title;

    @NotNull(message = "Genre must be specified")
    private Genre genre;
}
