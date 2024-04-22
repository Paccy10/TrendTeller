package com.paccy.TrendTeller.dto;

import com.paccy.TrendTeller.models.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostResponseDTO extends ResponseDTO {
    private List<Post> data;
}
