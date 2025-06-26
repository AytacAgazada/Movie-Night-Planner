 package com.example.filmplanner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollRequest {

    @NotBlank(message = "Poll title is mandatory")
    private String title;
}
