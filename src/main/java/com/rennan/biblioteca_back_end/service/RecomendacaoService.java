package com.rennan.biblioteca_back_end.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;

@Service
public class RecomendacaoService {
  
  private final UsuarioService usuarioService;
  private final EmprestimoService emprestimoService;
  private final LivroService livroService;

  public RecomendacaoService(UsuarioService usuarioService, 
  EmprestimoService emprestimoService, 
  LivroService livroService) {
    this.usuarioService = usuarioService;
    this.emprestimoService = emprestimoService;
    this.livroService = livroService;
  }

  public List<Livro> recommendLivro(Long usuarioId) {
    Usuario usuario = this.usuarioService.findById(usuarioId);
    List<String> categoriasBorrowed = this.emprestimoService.findCategoriesBorrowedByUser(usuario);

    if (categoriasBorrowed.isEmpty()) {
      throw new ResourceNotFoundException("Nenhum livro emprestado");
    }

    List<Long> livrosBorrowedIds = this.emprestimoService.findAllBorrowedByUser(usuario);

    return this.livroService.findNotBorrowedByCategoria(categoriasBorrowed, livrosBorrowedIds);
  }
}
