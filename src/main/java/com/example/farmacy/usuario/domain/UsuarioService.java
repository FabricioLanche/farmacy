package com.example.farmacy.usuario.domain;

import com.example.farmacy.usuario.dto.NuevoUsuario;
import com.example.farmacy.usuario.infrastructure.UsuarioRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Usuario guardarUsuario(NuevoUsuario nuevoUsuario) {

        if (nuevoUsuario.getDni() != null && usuarioRepository.existsById(nuevoUsuario.getDni())) {
            Usuario existingUsuario = usuarioRepository.findById(nuevoUsuario.getDni())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            modelMapper.map(nuevoUsuario, existingUsuario);
            return usuarioRepository.save(existingUsuario);
        } else {
            Usuario usuario = modelMapper.map(nuevoUsuario, Usuario.class);
            return usuarioRepository.save(usuario);
        }
    }

    public Usuario actualizarUsuario(Long id, NuevoUsuario nuevoUsuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        modelMapper.map(nuevoUsuario, usuarioExistente);

        return usuarioRepository.save(usuarioExistente);
    }
}
