package com.example.farmacy.usuario;

import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthenticationService {
    @Autowired
    UsuarioRepository usuarioRepository;


}
