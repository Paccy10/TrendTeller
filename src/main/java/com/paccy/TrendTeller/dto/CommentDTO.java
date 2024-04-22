package com.paccy.TrendTeller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    @NotEmpty
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 10, message = "Body must be at least 10 characters long")
    private String body;
}
