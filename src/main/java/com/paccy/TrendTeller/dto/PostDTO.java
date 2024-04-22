package com.paccy.TrendTeller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDTO {
    @NotEmpty
    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @NotEmpty
    private String content;
}
