package com.rennan.biblioteca_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rennan.biblioteca_back_end.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
