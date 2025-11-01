package com.rennan.biblioteca_back_end.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;

@ExtendWith(MockitoExtension.class)
class RecomendacaoServiceTest {

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private EmprestimoService emprestimoService;
    @Mock
    private LivroService livroService;

    @InjectMocks
    private RecomendacaoService recomendacaoService;

    private Usuario defaultUsuario;
    private static final Long USUARIO_ID = 1L;

    @BeforeEach
    void setUp() {
        defaultUsuario = new Usuario();
        defaultUsuario.setId(USUARIO_ID);
        defaultUsuario.setNome("Lucas");
        defaultUsuario.setEmail("lucas@test.com");
        defaultUsuario.setTelefone("999999999");
    }

    @Test
    void recommendLivroWithPreviousLoanMustReturnRecommendedLivros() {
        when(usuarioService.findById(USUARIO_ID)).thenReturn(defaultUsuario);

        List<String> categorias = List.of("Ficção", "Drama");
        List<Long> borrowedIds = List.of(10L, 12L);
        when(emprestimoService.findCategoriesBorrowedByUser(defaultUsuario)).thenReturn(categorias);
        when(emprestimoService.findAllBorrowedByUser(defaultUsuario)).thenReturn(borrowedIds);

        Livro recommendedLivro = new Livro();
        recommendedLivro.setTitulo("Livro Legal");
        recommendedLivro.setAutor("Autor X");
        recommendedLivro.setIsbn("9785123132");
        recommendedLivro.setDataPublicacao(LocalDate.now());
        recommendedLivro.setCategoria("Ficção");
        
        when(livroService.findNotBorrowedByCategoria(categorias, borrowedIds)).thenReturn(List.of(recommendedLivro));

        List<Livro> result = recomendacaoService.recommendLivro(USUARIO_ID);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Livro Legal", result.get(0).getTitulo());

        verify(usuarioService, times(1)).findById(USUARIO_ID);
        verify(emprestimoService, times(1)).findCategoriesBorrowedByUser(defaultUsuario);
        verify(emprestimoService, times(1)).findAllBorrowedByUser(defaultUsuario);
        verify(livroService, times(1)).findNotBorrowedByCategoria(categorias, borrowedIds);
    }

    @Test
    void recommendLivroWithNoBorrowedCategoriasMustThrowException() {
        when(usuarioService.findById(USUARIO_ID)).thenReturn(defaultUsuario);

        when(emprestimoService.findCategoriesBorrowedByUser(defaultUsuario)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> {
            recomendacaoService.recommendLivro(USUARIO_ID);
        }, "An exception should be thrown when Usuário has no loan history.");

        verify(emprestimoService, times(1)).findCategoriesBorrowedByUser(defaultUsuario);
        verify(emprestimoService, never()).findAllBorrowedByUser(any());
        verify(livroService, never()).findNotBorrowedByCategoria(any(), any());
    }

    @Test
    void recommendLivroWhenUsuarioNotFoundMustThrowException() {
        when(usuarioService.findById(USUARIO_ID)).thenThrow(new ResourceNotFoundException("Usuário not found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            recomendacaoService.recommendLivro(USUARIO_ID);
        }, "Should throw the Usuário not found exception.");

        verify(usuarioService, times(1)).findById(USUARIO_ID);
        verify(emprestimoService, never()).findCategoriesBorrowedByUser(any());
    }
}