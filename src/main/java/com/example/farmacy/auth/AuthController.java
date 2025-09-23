package com.example.farmacy.auth;


import com.example.farmacy.security.JwtAuthenticationResponse;
import com.example.farmacy.usuario.domain.Usuario;
import com.example.farmacy.usuario.dto.NuevoUsuario;
import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtAuthenticationResponse jwtAuthenticationService;


    @PostMapping("/signup/cliente")
    public ResponseEntity<JwtAuthenticationResponse> signupCliente(@RequestBody @Valid NuevoUsuario request) {
        return ResponseEntity.ok(authenticationService.signupCliente(request));
    }

    @PostMapping("/signup/profesional")
    public ResponseEntity<String> signupProfesional(@RequestBody @Valid ProfLimpiezaDto request) {
        authenticationService.signupProfesional(request);
        return ResponseEntity.ok("Tu solicitud ha sido enviada. Te contactaremos una vez aprobada.");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

}
