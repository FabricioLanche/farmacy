package com.example.farmacy.usuario.domain;

import com.example.farmacy.security.exceptions.UserNotFoundException;
import com.example.farmacy.usuario.dto.UpdateUserDto;
import com.example.farmacy.usuario.dto.UserResponseDto;
import com.example.farmacy.usuario.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
        return new UserResponseDto(
                user.getEmail(),
                user.getNombre(),
                user.getApellido(),
                user.getRole().name()
        );
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponseDto(
                        user.getEmail(),
                        user.getNombre(),
                        user.getApellido(),
                        user.getRole().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateProfile(String currentEmail, UpdateUserDto updateUserDto) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + currentEmail));

        if (updateUserDto.getNombre() != null && !updateUserDto.getNombre().trim().isEmpty()) {
            user.setNombre(updateUserDto.getNombre().trim());
        }

        if (updateUserDto.getApellido() != null && !updateUserDto.getApellido().trim().isEmpty()) {
            user.setApellido(updateUserDto.getApellido().trim());
        }

        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().trim().isEmpty()) {
            String newEmail = updateUserDto.getEmail().trim();
            if (!currentEmail.equals(newEmail)) {
                if (userRepository.existsByEmail(newEmail)) {
                    throw new IllegalArgumentException("El email ya está en uso");
                }
                user.setEmail(newEmail);
            }
        }

        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return new UserResponseDto(
                updatedUser.getEmail(),
                updatedUser.getNombre(),
                updatedUser.getApellido(),
                updatedUser.getRole().name()
        );
    }

    @Transactional
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));

        userRepository.delete(user);
    }
}