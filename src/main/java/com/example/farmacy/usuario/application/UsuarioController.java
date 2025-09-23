package com.example.farmacy.usuario.application;

import com.example.farmacy.usuario.domain.Usuario;
import com.example.farmacy.usuario.domain.UsuarioService;
import com.example.farmacy.usuario.dto.NuevoUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/nuevo")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody NuevoUsuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.created(URI.create("/usuario/" + nuevoUsuario.getId())).body(nuevoUsuario);
    }

    @PutMapping("/editUsuario/{id}")
    public ResponseEntity<Usuario> editUsuario(@PathVariable Long id, @Valid @RequestBody NuevoUsuario usuario) {
        Optional<Usuario> existingUser = usuarioService.getUsuarioRepository().findById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Usuario updatedUser = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> traerUsuarios() {
        List<Usuario> lista = usuarioService.getUsuarioRepository().findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> traerUsuario(@PathVariable Long id) {
        Optional<Usuario> existingUser = usuarioService.getUsuarioRepository().findById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingUser.get());
    }
}
