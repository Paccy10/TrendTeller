package com.paccy.TrendTeller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private int page;
    private int limit;
    private long total;
    private int pages;
}
