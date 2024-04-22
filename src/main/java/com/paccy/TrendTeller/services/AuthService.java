package com.paccy.TrendTeller.services;

import com.paccy.TrendTeller.dto.JwtAuthResponseDTO;
import com.paccy.TrendTeller.dto.LoginDTO;
import com.paccy.TrendTeller.dto.RegisterDTO;
import com.paccy.TrendTeller.dto.UserResponseDTO;

public interface AuthService {
    JwtAuthResponseDTO login(LoginDTO loginDTO);
    UserResponseDTO register(RegisterDTO registerDTO);
}
