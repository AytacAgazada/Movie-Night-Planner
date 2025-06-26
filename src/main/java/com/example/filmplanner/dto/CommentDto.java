package com.example.filmplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotBlank(message = "Username must not be empty")
    private String username;

    @Size(min = 5, max = 1000, message = "Message must be between 5 and 1000 characters")
    private String message;

    @NotNull(message = "Film ID must be provided")
    private Long filmId;
}
