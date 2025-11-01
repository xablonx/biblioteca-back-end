package com.rennan.biblioteca_back_end.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.service.UsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
  
  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping
  public List<Usuario> list() {
    return this.usuarioService.list();
  }

  @GetMapping("/{id}")
  public Usuario findById(@PathVariable @NotNull @Positive Long id) {
    return usuarioService.findById(id);
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public Usuario create(@RequestBody @Valid @NotNull Usuario usuario) {
    return this.usuarioService.create(usuario);
  }

  @PutMapping("/{id}")
  public Usuario update(@PathVariable @NotNull @Positive Long id, 
    @RequestBody @Valid @NotNull Usuario usuario) {
      return this.usuarioService.update(id, usuario);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @NotNull @Positive Long id) {
    this.usuarioService.delete(id);
  }
}
