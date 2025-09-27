package com.example.farmacy.auth;
import com.example.farmacy.admin.AdminNotFound;
import com.example.farmacy.admin.dto.NuevoAdmin;
import com.example.farmacy.security.AuthenticationService;
import com.example.farmacy.security.JwtAuthenticationResponse;
import com.example.farmacy.security.dto.SigninRequest;
import com.example.farmacy.usuario.dto.NuevoUsuario;
import com.example.farmacy.usuario.exception.InvalidCredentialsUser;
import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signupCliente(@RequestBody @Valid NuevoUsuario request) {
        return ResponseEntity.ok(authenticationService.signupPaciente(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<?> login(@RequestBody NuevoAdmin request) {
        try {
            JwtAuthenticationResponse token = authenticationService.loginAdminFijo(
                    request.getEmail(), request.getPassword()
            );
            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsUser | AdminNotFound e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (usuarioRepository.existsById(id)) {
                usuarioRepository.deleteById(id);
                return ResponseEntity.ok().body("Usuario eliminado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}