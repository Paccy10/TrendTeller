package com.paccy.TrendTeller.services.impl;

import com.paccy.TrendTeller.dto.JwtAuthResponseDTO;
import com.paccy.TrendTeller.dto.LoginDTO;
import com.paccy.TrendTeller.dto.RegisterDTO;
import com.paccy.TrendTeller.dto.UserResponseDTO;
import com.paccy.TrendTeller.exceptions.BlogAPIException;
import com.paccy.TrendTeller.exceptions.NotFoundException;
import com.paccy.TrendTeller.models.Role;
import com.paccy.TrendTeller.models.User;
import com.paccy.TrendTeller.repositories.RoleRepository;
import com.paccy.TrendTeller.repositories.UserRepository;
import com.paccy.TrendTeller.security.JwtTokenProvider;
import com.paccy.TrendTeller.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public JwtAuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();

        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(token);
        jwtAuthResponseDTO.setUser(modelMapper.map(user, UserResponseDTO.class));

        return jwtAuthResponseDTO;
    }

    @Override
    public UserResponseDTO register(RegisterDTO registerDTO) {
        if(userRepository.existsByUsername(registerDTO.getUsername()))
            throw new BlogAPIException(
                    String.format("User with username '%s' already exists", registerDTO.getUsername()),
                    HttpStatus.BAD_REQUEST);
        if(userRepository.existsByEmail(registerDTO.getEmail()))
            throw new BlogAPIException(
                    String.format("User with email '%s' already exists", registerDTO.getEmail()),
                    HttpStatus.BAD_REQUEST);

        User user = modelMapper.map(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role", "name", "ROLE_USER"));
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
}
