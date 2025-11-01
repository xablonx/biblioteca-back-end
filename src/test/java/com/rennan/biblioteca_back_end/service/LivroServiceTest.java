package com.rennan.biblioteca_back_end.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.repository.LivroRepository;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

  @Mock
  private LivroRepository livroRepository;

  @Mock
  private GoogleBookService googleBookService;

  @InjectMocks
  private LivroService livroService;

  private Livro defaultLivro;
  private static final Long VALID_ID = 1L;
  private static final Long INVALID_ID = 99L;

  @BeforeEach
  void setUp() {
    defaultLivro = new Livro();
    defaultLivro.setTitulo("Profeta de Duna"); 
    defaultLivro.setAutor("Frank Herbert"); 
    defaultLivro.setIsbn("9788535914856"); 
    defaultLivro.setDataPublicacao(LocalDate.of(1967, 5, 30)); 
    defaultLivro.setCategoria("Ficção");
    defaultLivro.setId(VALID_ID);
  }

  @Test
  void listMustReturnAllLivros() {
    List<Livro> livros = List.of(defaultLivro, new Livro("1984", "George Orwell", "9788520935560", LocalDate.of(1949, 1, 1), "Distopia"));
    when(livroRepository.findAll()).thenReturn(livros);

    List<Livro> result = livroService.list();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(livroRepository, times(1)).findAll();
  }
  
  @Test
  void findByIdWithExistingIdMustReturnLivro() {
    when(livroRepository.findById(VALID_ID)).thenReturn(Optional.of(defaultLivro));

    Livro result = livroService.findById(VALID_ID);

    assertNotNull(result);
    assertEquals(defaultLivro.getTitulo(), result.getTitulo());
    verify(livroRepository, times(1)).findById(VALID_ID);
  }

  @Test
  void findByIdWithNonExistentIdMustThrowException() {
    when(livroRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
        livroService.findById(INVALID_ID);
    }, "An exception should be thrown when the book is not found.");

    verify(livroRepository, times(1)).findById(INVALID_ID);
  }
  
  @Test
  void createWithNewLivroMustSaveAndReturnLivro() {
    Livro newLivro = new Livro();
    newLivro.setId(2L);
    newLivro.setTitulo("O Alquimista");
    newLivro.setAutor("Paulo Coelho");
    newLivro.setIsbn("9788500000000");
    newLivro.setDataPublicacao(LocalDate.of(1988, 1, 1));
    newLivro.setCategoria("Aventura");
    
    when(livroRepository.save(any(Livro.class))).thenReturn(newLivro);

    Livro result = livroService.create(newLivro);

    assertNotNull(result.getId());
    assertEquals("O Alquimista", result.getTitulo());
    verify(livroRepository, times(1)).save(newLivro);
  }
  
  @Test
  void updateWithExistingIdMustUpdate() {
    when(livroRepository.findById(VALID_ID)).thenReturn(Optional.of(defaultLivro));
    
    Livro updatedData = new Livro();
    updatedData.setTitulo("Novo Título");
    updatedData.setAutor("Novo Autor");
    updatedData.setIsbn("9786000000000");
    updatedData.setDataPublicacao(LocalDate.of(2020, 1, 1));
    updatedData.setCategoria("Biografia");

    when(livroRepository.save(any(Livro.class))).thenReturn(defaultLivro);

    Livro result = livroService.update(VALID_ID, updatedData);

    assertEquals("Novo Título", result.getTitulo());
    assertEquals("Novo Autor", result.getAutor());
    assertEquals("9786000000000", result.getIsbn());
    assertEquals("Biografia", result.getCategoria());
    
    verify(livroRepository, times(1)).save(defaultLivro);
  }
  
  @Test
  void findNotBorrowedByCategoria_MustCallCorrectMethodFromRepository() {
    List<String> categorias = List.of("Fantasia", "Ficção");
    List<Long> borrowedLivros = List.of(2L, 3L);
    List<Livro> recommended = List.of(defaultLivro);
    
    when(livroRepository.findByCategoriaInAndIdNotIn(categorias, borrowedLivros)).thenReturn(recommended);

    List<Livro> result = livroService.findNotBorrowedByCategoria(categorias, borrowedLivros);

    assertEquals(1, result.size());
    assertEquals(defaultLivro.getTitulo(), result.get(0).getTitulo());
    verify(livroRepository, times(1)).findByCategoriaInAndIdNotIn(categorias, borrowedLivros);
  }

  @Test
  void searchGoogleBooksMustDelegateSearchToGoogleBookService() {
    String tituloSearch = "Duna";
    Livro livro = new Livro();
    livro.setTitulo("Duna");
    livro.setAutor("Frank Herbert");
    livro.setIsbn("9786555601234");
    livro.setDataPublicacao(LocalDate.of(1965, 8, 1));
    livro.setCategoria("Ficção Científica");
    List<Livro> foundLivros = List.of(livro);
    
    when(googleBookService.searchLivrosByTitulo(tituloSearch)).thenReturn(foundLivros);

    List<Livro> result = livroService.searchGoogleBooks(tituloSearch);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals("Duna", result.get(0).getTitulo());
    
    verify(googleBookService, times(1)).searchLivrosByTitulo(tituloSearch);
    verify(livroRepository, never()).save(any(Livro.class));
  }

  @Test
  void searchGoogleBooksWIthoutTituloMustReturnEmptyList() {
    String tituloSearch = "";
    
    when(googleBookService.searchLivrosByTitulo(tituloSearch)).thenReturn(List.of());

    List<Livro> result = livroService.searchGoogleBooks(tituloSearch);

    assertTrue(result.isEmpty());
    
    verify(googleBookService, times(1)).searchLivrosByTitulo(tituloSearch);
  }

  @Test
  void saveGoogleBookWithGoogleBookMustSaveToLocalRepository() {
    Livro googleBook = new Livro();
    googleBook.setId(99L);
    googleBook.setTitulo("Duna");
    googleBook.setAutor("Frank Herbert");
    googleBook.setIsbn("9788599296316");
    googleBook.setDataPublicacao(LocalDate.of(1965, 8, 1));
    googleBook.setCategoria("Ficção Científica");
    
    Livro savedLivro = new Livro();
    savedLivro.setId(2L);
    savedLivro.setTitulo("Duna");
    savedLivro.setAutor("Frank Herbert");
    savedLivro.setIsbn("9788599296316");
    savedLivro.setDataPublicacao(LocalDate.of(1965, 8, 1));
    savedLivro.setCategoria("Ficção Científica");
    
    when(livroRepository.save(any(Livro.class))).thenReturn(savedLivro);

    Livro result = livroService.saveGoogleBook(googleBook);

    assertNotNull(result.getId());
    assertEquals(2L, result.getId());
    assertEquals("Duna", result.getTitulo());
    
    verify(livroRepository, times(1)).save(googleBook);
    verify(googleBookService, never()).searchLivrosByTitulo(anyString()); 
  }
}
