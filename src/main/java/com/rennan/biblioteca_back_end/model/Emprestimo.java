package com.rennan.biblioteca_back_end.model;

import java.time.LocalDateTime;

import com.rennan.biblioteca_back_end.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Emprestimo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  @NotNull 
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "livro_id", nullable = false) 
  @NotNull
  private Livro livro;

  @NotNull
  @PastOrPresent
  @Column(name = "data_emprestimo", nullable = false)
  private LocalDateTime dataEmprestimo;
  
  @Column(name = "data_devolucao", nullable = true)
  private LocalDateTime dataDevolucao;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Status status;

  public Emprestimo() {
  }

  public Emprestimo(Usuario usuario, Livro livro, LocalDateTime dataEmprestimo, LocalDateTime dataDevolucao) {
    this.usuario = usuario;
    this.livro = livro;
    this.dataEmprestimo = dataEmprestimo;
    this.dataDevolucao = dataDevolucao;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Usuario getUsuario() {
    return this.usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Livro getLivro() {
    return this.livro;
  }

  public void setLivro(Livro livro) {
    this.livro = livro;
  }

  public LocalDateTime getDataEmprestimo() {
    return this.dataEmprestimo;
  }

  public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
    this.dataEmprestimo = dataEmprestimo;
  }

  public LocalDateTime getDataDevolucao() {
    return this.dataDevolucao;
  }

  public void setDataDevolucao(LocalDateTime dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
