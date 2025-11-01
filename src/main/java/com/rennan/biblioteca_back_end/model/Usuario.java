package com.rennan.biblioteca_back_end.model;

import java.time.LocalDateTime;
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
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; 
  
  @NotBlank
  @Length(min = 5, max = 100)
  @Column(length = 100, nullable = false)
  private String nome;
  
  @NotBlank
  @Email
  @Length(min = 5, max = 100)
  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "data_cadastro", nullable = false)
  private LocalDateTime dataCadastro;

  @NotBlank
  @Length(min = 10, max = 20)
  @Column(length = 20, nullable = false)
  private String telefone;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Emprestimo> emprestimos;

  @PrePersist
  public void prePersist() {
    this.dataCadastro = LocalDateTime.now();
  }

  public Usuario() {
  }

  public Usuario(String nome, String email, String telefone) {
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getDataCadastro() {
    return this.dataCadastro;
  }

  public void setDataCadastro(LocalDateTime dataCadastro) {
    this.dataCadastro = dataCadastro;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public List<Emprestimo> getEmprestimos() {
    return this.emprestimos;
  }

  public void setEmprestimos(List<Emprestimo> emprestimos) {
    this.emprestimos = emprestimos;
  }
}
