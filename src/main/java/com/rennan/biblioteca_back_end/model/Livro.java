package com.rennan.biblioteca_back_end.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Livro {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @NotBlank
  @Column(nullable = false, length = 200)
  @Length(min = 4, max = 200)
  private String titulo; 
  
  @NotBlank
  @Length(min = 5, max = 150)
  @Column(nullable = false, length = 150)
  private String autor; 
  
  @NotBlank
  @Length(min = 13, max = 20)
  @Column(nullable = false, length = 20, unique = true)
  private String isbn; 
  
  @NotNull
  @Column(name = "data_publicacao", nullable = false)
  private LocalDate dataPublicacao;

  @NotBlank
  @Length(min = 4, max = 50)
  @Column(nullable = false, length = 50)
  private String categoria;

  @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Emprestimo> emprestimos;


  public Livro() {
  }

  public Livro(String titulo, String autor, String isbn, LocalDate dataPublicacao, String categoria) {
    this.titulo = titulo;
    this.autor = autor;
    this.isbn = isbn;
    this.dataPublicacao = dataPublicacao;
    this.categoria = categoria;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getAutor() {
    return this.autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getIsbn() {
    return this.isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public LocalDate getDataPublicacao() {
    return this.dataPublicacao;
  }

  public void setDataPublicacao(LocalDate dataPublicacao) {
    this.dataPublicacao = dataPublicacao;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public List<Emprestimo> getEmprestimos() {
    return this.emprestimos;
  }

  public void setEmprestimos(List<Emprestimo> emprestimos) {
    this.emprestimos = emprestimos;
  }
}
