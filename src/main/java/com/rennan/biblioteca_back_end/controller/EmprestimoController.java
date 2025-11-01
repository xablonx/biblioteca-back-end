package com.rennan.biblioteca_back_end.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rennan.biblioteca_back_end.dto.EmprestimoDTO;
import com.rennan.biblioteca_back_end.model.Emprestimo;
import com.rennan.biblioteca_back_end.service.EmprestimoService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("api/emprestimos")
public class EmprestimoController {

  private final EmprestimoService emprestimoService;

  public EmprestimoController(EmprestimoService emprestimoService) {
    this.emprestimoService = emprestimoService;
  }

  @GetMapping
  public List<Emprestimo> list() {
    return this.emprestimoService.list();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Emprestimo createEmprestimo(@Valid @RequestBody EmprestimoDTO emprestimoDTO) {
    return emprestimoService.createEmprestimo(emprestimoDTO.usuarioId(), emprestimoDTO.livroId());
  }

  @PatchMapping("{id}/devolucao")
  public Emprestimo returnLivro(@PathVariable Long id) {
    return this.emprestimoService.returnLivro(id);
  }
}
