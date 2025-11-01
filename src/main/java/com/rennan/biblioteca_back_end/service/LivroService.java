package com.rennan.biblioteca_back_end.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.repository.LivroRepository;

@Service
public class LivroService {

  private final LivroRepository livroRepository;
  private final GoogleBookService googleBookService;

  public LivroService(LivroRepository livroRepository, GoogleBookService googleBookService) {
    this.livroRepository = livroRepository;
    this.googleBookService = googleBookService;
  }

  public List<Livro> list() {
    return this.livroRepository.findAll();
  }

  public Livro findById(Long id) {
    return this.livroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));
  }

  public Livro create(Livro newLivro) {
    return this.livroRepository.save(newLivro);
  }

  public Livro update(Long id, Livro updateLivro) {
    Livro livro = this.findById(id);
    livro.setTitulo(updateLivro.getTitulo());
    livro.setAutor(updateLivro.getAutor());
    livro.setIsbn(updateLivro.getIsbn());
    livro.setDataPublicacao(updateLivro.getDataPublicacao());
    livro.setCategoria(updateLivro.getCategoria());

    return this.livroRepository.save(livro);
  }

  public void delete(Long id) {
    this.livroRepository.delete(this.findById(id));
  }

  public List<Livro> findNotBorrowedByCategoria(List<String> categoriasBorrowed, List<Long> livrosAlreadyBorrowedIds) {
    return this.livroRepository.findByCategoriaInAndIdNotIn(categoriasBorrowed, livrosAlreadyBorrowedIds);
  }

  public List<Livro> searchGoogleBooks(String titulo) {
    return this.googleBookService.searchLivrosByTitulo(titulo);
  }

  public Livro saveGoogleBook(Livro livro) {
    return livroRepository.save(livro);
  }
}
