package com.rennan.biblioteca_back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rennan.biblioteca_back_end.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

  public List<Livro> findByCategoriaInAndIdNotIn(List<String> categoriasBorrowed, List<Long> livrosAlreadyBorrowedIds);
}
