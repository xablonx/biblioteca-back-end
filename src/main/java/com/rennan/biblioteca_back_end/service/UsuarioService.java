package com.rennan.biblioteca_back_end.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.repository.UsuarioRepository;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public List<Usuario> list() {
    return this.usuarioRepository.findAll();
  }

  public Usuario findById(Long id) {
    return this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
  }

  public Usuario create(Usuario newUsuario) {
    return this.usuarioRepository.save(newUsuario);
  }

  public Usuario update(Long id, Usuario updateUsuario) {
    Usuario usuario = this.findById(id);
    usuario.setNome(updateUsuario.getNome());
    usuario.setEmail(updateUsuario.getEmail());
    usuario.setTelefone(updateUsuario.getTelefone());

    return this.usuarioRepository.save(usuario);
  }

  public void delete(Long id) {
    this.usuarioRepository.delete(this.findById(id));
  }
}
