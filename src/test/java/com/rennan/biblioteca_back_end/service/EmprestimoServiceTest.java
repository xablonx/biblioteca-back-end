package com.rennan.biblioteca_back_end.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rennan.biblioteca_back_end.enums.Status;
import com.rennan.biblioteca_back_end.exception.LivroUnavailableException;
import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Emprestimo;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.repository.EmprestimoRepository;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {
  
  @Mock
  private EmprestimoRepository emprestimoRepository;

  @Mock
  private UsuarioService usuarioService;

  @Mock
  private LivroService livroService;    

  @InjectMocks
  private EmprestimoService emprestimoService;
  
  private Usuario defaultUsuario;
  private Livro availableLivro;
  private Emprestimo activeEmprestimo;
  private static final Long USUARIO_ID = 1L;
  private static final Long LIVRO_ID = 10L;
  private static final Long EMPRESTIMO_ID = 100L;

  @BeforeEach
  void setUp() {
    defaultUsuario = new Usuario();
    defaultUsuario.setId(USUARIO_ID);
    defaultUsuario.setNome("Ana");
    defaultUsuario.setEmail("ana@test.com");
    defaultUsuario.setTelefone("999");

    availableLivro = new Livro();
    availableLivro.setId(LIVRO_ID);
    availableLivro.setTitulo("A Arte da Guerra");
    availableLivro.setAutor("Sun Tzu");
    availableLivro.setIsbn("1111");
    availableLivro.setDataPublicacao(LocalDate.now());
    availableLivro.setCategoria("Estratégia");

    activeEmprestimo = new Emprestimo();
    activeEmprestimo.setId(EMPRESTIMO_ID);
    activeEmprestimo.setUsuario(defaultUsuario);
    activeEmprestimo.setLivro(availableLivro);
    activeEmprestimo.setDataEmprestimo(LocalDateTime.now().minusDays(5));
    activeEmprestimo.setStatus(Status.EMPRESTADO);
  }
  
  @Test
  void createEmprestimoWithAvailableLivroMustSaveWithStatusEmprestado() {
    when(usuarioService.findById(USUARIO_ID)).thenReturn(defaultUsuario);
    when(livroService.findById(LIVRO_ID)).thenReturn(availableLivro);
    
    when(emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(availableLivro)).thenReturn(false);
    
    when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(activeEmprestimo);

    Emprestimo result = emprestimoService.createEmprestimo(USUARIO_ID, LIVRO_ID);

    assertNotNull(result);
    assertEquals(Status.EMPRESTADO, result.getStatus());
    verify(emprestimoRepository, times(1)).save(any(Emprestimo.class));
  }

  @Test
  void createEmprestimoWithLivroEmprestadoMustThrowLivroUnavailableException() {
    when(usuarioService.findById(USUARIO_ID)).thenReturn(defaultUsuario);
    when(livroService.findById(LIVRO_ID)).thenReturn(availableLivro);

    when(emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(availableLivro)).thenReturn(true);

    assertThrows(LivroUnavailableException.class, () -> {
        emprestimoService.createEmprestimo(USUARIO_ID, LIVRO_ID);
    }, "An exception should be made when the book is already on active loan.");

    verify(emprestimoRepository, never()).save(any(Emprestimo.class));
  }
  
  @Test
  void returnLivroWithActiveEmprestimoMustUpdateStatusToDevolvido() {
    when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(activeEmprestimo));
    
    when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(activeEmprestimo);

    Emprestimo result = emprestimoService.returnLivro(EMPRESTIMO_ID);

    assertEquals(Status.DEVOLVIDO, result.getStatus());
    assertNotNull(result.getDataDevolucao());
    assertTrue(result.getDataDevolucao().isAfter(result.getDataEmprestimo()));
    verify(emprestimoRepository, times(1)).save(activeEmprestimo);
  }
  
  @Test
  void returnLivroWithEmprestimoNotFoundMustThrowResourceNotFoundException() {
    when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
        emprestimoService.returnLivro(999L);
    }, "Must raise an exception if the loan to be repaid cannot be found.");

    verify(emprestimoRepository, never()).save(any(Emprestimo.class));
  }
  
  @Test
  void returnLivroWithEmprestimoAlreadyDevolvidoMustThrowIllegalArgumentException() {
    activeEmprestimo.setStatus(Status.DEVOLVIDO);
    
    when(emprestimoRepository.findById(EMPRESTIMO_ID)).thenReturn(Optional.of(activeEmprestimo));

    assertThrows(IllegalArgumentException.class, () -> {
        emprestimoService.returnLivro(EMPRESTIMO_ID);
    }, "Must raise an exception if the loan has already been repaid.");

    verify(emprestimoRepository, never()).save(any(Emprestimo.class));
  }
  
  @Test
  void findCategoriasBorrowedByUsuarioMustCallCorrectMethodFromRepository() {
    List<String> categorias = List.of("Fantasia", "Suspense");
    when(emprestimoRepository.findDistinctCategoriasBorrowedByUsuario(defaultUsuario)).thenReturn(categorias);

    List<String> result = emprestimoService.findCategoriesBorrowedByUser(defaultUsuario);

    assertEquals(2, result.size());
    assertTrue(result.contains("Fantasia"));
    verify(emprestimoRepository, times(1)).findDistinctCategoriasBorrowedByUsuario(defaultUsuario);
  }

  @Test
  void findAllBorrowedByUsuarioMustCallCorrectMethodFromRepository() {
    List<Long> idsLivros = List.of(10L, 12L, 15L);
    when(emprestimoRepository.findBorrowedLivroIdsByUsuario(defaultUsuario)).thenReturn(idsLivros);

    List<Long> result = emprestimoService.findAllBorrowedByUser(defaultUsuario);

    assertEquals(3, result.size());
    assertTrue(result.contains(15L));
    verify(emprestimoRepository, times(1)).findBorrowedLivroIdsByUsuario(defaultUsuario);
  }

  @Test
  void listMustCallMethodFindAllWithUsuarioAndLivro() {
    when(emprestimoRepository.findAllWithUsuarioAndLivro()).thenReturn(List.of(activeEmprestimo));

    List<Emprestimo> result = emprestimoService.list();

    assertEquals(1, result.size());
    verify(emprestimoRepository, times(1)).findAllWithUsuarioAndLivro();
    verify(emprestimoRepository, never()).findAll();
  }
}
