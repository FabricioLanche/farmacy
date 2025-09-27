package com.example.farmacy.security;

import com.example.farmacy.admin.Admin;
import com.example.farmacy.admin.AdminNotFound;
import com.example.farmacy.admin.AdminRepository;
import com.example.farmacy.admin.Role;
import com.example.farmacy.security.dto.SigninRequest;
import com.example.farmacy.usuario.domain.Usuario;
import com.example.farmacy.usuario.dto.NuevoUsuario;
import com.example.farmacy.usuario.exception.InvalidCredentialsUser;
import com.example.farmacy.usuario.exception.UsuarioConflictException;
import com.example.farmacy.usuario.exception.UsuarioNotFoundException;
import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AdminRepository adminRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPasswordHash;

    public JwtAuthenticationResponse signupPaciente(NuevoUsuario request) {
        // Verificar si ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioConflictException("Ya existe un usuario con este email.");
        }

        // Crear paciente en tu base
        Usuario paciente = new Usuario();
        paciente.setNombre(request.getNombre());
        paciente.setEmail(request.getEmail());
        paciente.setRole(Role.PACIENTE);
        paciente.setDni(request.getDni());

        usuarioRepository.save(paciente);

        String jwt = jwtService.generateToken(paciente);

        return new JwtAuthenticationResponse(jwt);
    }
    public JwtAuthenticationResponse signin(SigninRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsUser("Usuario o contraseña inválidos");
        }

        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse loginAdminFijo(String email, String password) {
        System.out.println("Email recibido: " + email);
        System.out.println("Esperado: " + adminEmail);
        System.out.println("¿Email ok?: " + email.equals(adminEmail));
        System.out.println("¿Password ok?: " + passwordEncoder.matches(password, adminPasswordHash));

        if (!email.equals(adminEmail) || !passwordEncoder.matches(password, adminPasswordHash)) {
            throw new AdminNotFound("Credenciales inválidas para admin");
        }

        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword(adminPasswordHash);
        admin.setRole(Role.ADMINISTRADOR);

        String jwt = jwtService.generateToken(admin);
        return new JwtAuthenticationResponse(jwt);
    }
}

