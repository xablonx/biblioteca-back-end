package com.rennan.biblioteca_back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rennan.biblioteca_back_end.model.Emprestimo;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
  boolean existsByLivroAndDataDevolucaoIsNull(Livro livro);

  @Query("SELECT DISTINCT e.livro.categoria FROM Emprestimo e WHERE e.usuario = :usuario")
  List<String> findDistinctCategoriasBorrowedByUsuario(Usuario usuario);

  @Query("SELECT e.livro.id FROM Emprestimo e WHERE e.usuario = :usuario")
  List<Long> findBorrowedLivroIdsByUsuario(Usuario usuario);

  @Query("SELECT e FROM Emprestimo e JOIN FETCH e.usuario JOIN FETCH e.livro ORDER BY e.dataEmprestimo DESC")
  List<Emprestimo> findAllWithUsuarioAndLivro();
}
