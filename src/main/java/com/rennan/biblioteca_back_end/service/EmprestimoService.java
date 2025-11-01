package com.rennan.biblioteca_back_end.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rennan.biblioteca_back_end.enums.Status;
import com.rennan.biblioteca_back_end.exception.LivroUnavailableException;
import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Emprestimo;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.repository.EmprestimoRepository;

@Service
public class EmprestimoService {

  private final EmprestimoRepository emprestimoRepository;
  private final UsuarioService usuarioService;
  private final LivroService livroService;

  public EmprestimoService(EmprestimoRepository emprestimoRepository,
    UsuarioService usuarioService, 
    LivroService livroService) {
      this.emprestimoRepository = emprestimoRepository;
      this.usuarioService = usuarioService;
      this.livroService = livroService;
  }

  public List<Emprestimo> list() {
    return this.emprestimoRepository.findAllWithUsuarioAndLivro();
  }

  public Emprestimo createEmprestimo(Long usuarioId, Long livroId) {
    Usuario usuario = this.usuarioService.findById(usuarioId);
    Livro livro = this.livroService.findById(livroId);

    if (this.emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro)) {
      throw new LivroUnavailableException("O livro '" + livro.getTitulo() + "' já possui um empréstimo ativo.");
    }

    Emprestimo emprestimo = new Emprestimo();
    emprestimo.setUsuario(usuario);
    emprestimo.setLivro(livro);
    emprestimo.setDataEmprestimo(LocalDateTime.now());
    emprestimo.setStatus(Status.EMPRESTADO);

    return this.emprestimoRepository.save(emprestimo);
  }

  public Emprestimo returnLivro(Long emprestimoId) {
    Emprestimo emprestimo = this.emprestimoRepository.findById(emprestimoId)
      .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com ID: " + emprestimoId));

    if (emprestimo.getStatus().equals(Status.DEVOLVIDO)) {
      throw new IllegalArgumentException("O livro já foi devolvido e o empréstimo está fechado.");
    }

    emprestimo.setDataDevolucao(LocalDateTime.now());
    emprestimo.setStatus(Status.DEVOLVIDO);
    return this.emprestimoRepository.save(emprestimo);
  }

  public List<String> findCategoriesBorrowedByUser(Usuario usuario) {
    return this.emprestimoRepository.findDistinctCategoriasBorrowedByUsuario(usuario);
  }

  public List<Long> findAllBorrowedByUser(Usuario usuario) {
    return this.emprestimoRepository.findBorrowedLivroIdsByUsuario(usuario);
  }
}
