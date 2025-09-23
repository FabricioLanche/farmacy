package com.example.farmacy.auth;


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
@RequestMapping("/o")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @PostMapping("/signup/cliente")
    public ResponseEntity<Usuario> signupCliente(@RequestBody @Valid NuevoUsuario request) {
        Usuario u = new Usuario();
        u.setNombre(request.getNombre());
        u.setApellido(request.getApellido());
        u.setEmail(request.getEmail());
        return ResponseEntity.ok(usuarioRepository.save(u));
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> getAllClientes(){
        return ResponseEntity.ok(usuarioRepository.findAll());
    }



}
