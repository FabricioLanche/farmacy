package com.example.farmacy.security;

import com.example.farmacy.admin.Role;
import com.example.farmacy.usuario.domain.Usuario;
import com.example.farmacy.usuario.dto.NuevoUsuario;
import com.example.farmacy.usuario.exception.UsuarioConflictException;
import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPasswordHash;

    public JwtAuthenticationResponse signupCliente(NuevoUsuario request) {
        // Verificar si ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioConflictException("Ya existe un usuario con este email.");
        }

        // Crear cliente en tu base
        Usuario cliente = new Usuario();
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setRole(Role.PACIENTE);
        cliente.setGender(request.getGenero());
        cliente.setDni(request.getDni());
        cliente.setStripeCustomerId(stripeCustomerId); // << aquí

        clienteRepository.save(cliente);

        String jwt = jwtService.generateToken(cliente);

        return new JwtAuthenticationResponse(jwt);
    }
}
