package com.example.filmplanner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotBlank(message = "Comment text is mandatory")
    private String text;

    @NotBlank(message = "User identifier is required")
    private String userIdentifier;
}
