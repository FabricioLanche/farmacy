package com.example.farmacy.usuario.infrastructure;


import com.example.farmacy.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario existsByEmail(String email);
}
