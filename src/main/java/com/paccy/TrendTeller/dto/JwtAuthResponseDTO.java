package com.paccy.TrendTeller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtAuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserResponseDTO user;
}
