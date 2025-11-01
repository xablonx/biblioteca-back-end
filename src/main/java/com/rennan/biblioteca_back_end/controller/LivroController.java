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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.service.LivroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("api/livros")
public class LivroController {
  private final LivroService livroService;

  public LivroController(LivroService livroService) {
    this.livroService = livroService;
  }

  @GetMapping
  public List<Livro> list() {
    return this.livroService.list();
  }

  @GetMapping("/{id}")
  public Livro findById(@PathVariable @NotNull @Positive Long id) {
    return livroService.findById(id);
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public Livro create(@RequestBody @Valid @NotNull Livro livro) {
    return this.livroService.create(livro);
  }

  @PutMapping("/{id}")
  public Livro update(@PathVariable @NotNull @Positive Long id, 
    @RequestBody @Valid @NotNull Livro livro) {
      return this.livroService.update(id, livro);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @NotNull @Positive Long id) {
    this.livroService.delete(id);
  }

  @GetMapping("/google-search")
  public List<Livro> searchGoogleBooks(@RequestParam String titulo) {
    return this.livroService.searchGoogleBooks(titulo);
  }

  @PostMapping("/google-save")
  public Livro saveGoogleBook(@RequestBody Livro livro) {
    return livroService.saveGoogleBook(livro);
  }
}
