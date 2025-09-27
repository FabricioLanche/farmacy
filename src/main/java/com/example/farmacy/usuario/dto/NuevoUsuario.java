package com.example.farmacy.usuario.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NuevoUsuario {
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
    @NotNull
    private String password;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Long dni;

}
