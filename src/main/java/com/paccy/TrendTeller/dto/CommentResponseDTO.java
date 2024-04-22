package com.paccy.TrendTeller.dto;

import com.paccy.TrendTeller.models.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentResponseDTO extends ResponseDTO {
    private List<Comment> data;
}
