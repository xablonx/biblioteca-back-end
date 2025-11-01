package com.rennan.biblioteca_back_end.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.service.RecomendacaoService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoController {
  
  private final RecomendacaoService recomendacaoService;

  public RecomendacaoController(RecomendacaoService recomendacaoService) {
    this.recomendacaoService = recomendacaoService;
  }

  @GetMapping("usuarios/{usuarioId}")
  public List<Livro> recommendLivros(@PathVariable @NotNull @Positive Long usuarioId) {
    return this.recomendacaoService.recommendLivro(usuarioId);
  }
}
