package com.example.filmplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotBlank(message = "User identifier is required")
    private String userIdentifier;

    @NotNull(message = "Film ID must be provided")
    private Long filmId;
}
