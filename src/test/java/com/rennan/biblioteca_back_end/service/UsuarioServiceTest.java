package com.rennan.biblioteca_back_end.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rennan.biblioteca_back_end.exception.ResourceNotFoundException;
import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
  
  @Mock
  private UsuarioRepository usuarioRepository;

  @InjectMocks
  private UsuarioService usuarioService;

  private Usuario defaultUsuario;
  private static final Long VALID_ID = 1L;
  private static final Long INVALID_ID = 99L;

  @BeforeEach
  void setUp() {
      defaultUsuario = new Usuario();
      defaultUsuario.setNome("John Doe");
      defaultUsuario.setEmail("johndoe@email.com");
      defaultUsuario.setTelefone("9876543210");
      defaultUsuario.setId(VALID_ID);
      defaultUsuario.setDataCadastro(LocalDateTime.now().minusDays(1)); 
  }

  @Test
  void listMustReturnAllUsuarios() {
      List<Usuario> usuarios = List.of(defaultUsuario, new Usuario("Joao", "joao@email.com", "1234567890"));
      when(usuarioRepository.findAll()).thenReturn(usuarios);

      List<Usuario> resultado = usuarioService.list();

      assertNotNull(resultado);
      assertEquals(2, resultado.size());
      verify(usuarioRepository, times(1)).findAll();
  }

  @Test
  void findByIdWithExistingIdMustReturnUsuario() {
      when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(defaultUsuario));

      Usuario result = usuarioService.findById(VALID_ID);

      assertNotNull(result);
      assertEquals(VALID_ID, result.getId());
      assertEquals("John Doe", result.getNome());
      verify(usuarioRepository, times(1)).findById(VALID_ID);
  }

  @Test
  void findByIdWithNonExistentIdMustThrowException() {
      when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class, () -> {
          usuarioService.findById(INVALID_ID);
      }, "An exception should be thrown when the user is not found.");

      verify(usuarioRepository, times(1)).findById(INVALID_ID);
  }
  
  @Test
  void createWithNewUsuarioMustSaveAndReturnUsuario() {
      Usuario newUsuario = new Usuario();
      newUsuario.setId(2L);
      newUsuario.setNome("Carlos");
      newUsuario.setEmail("carlos@email.com");
      newUsuario.setTelefone("9876543210");
      newUsuario.setDataCadastro(LocalDateTime.now());
      
      when(usuarioRepository.save(any(Usuario.class))).thenReturn(newUsuario);

      Usuario result = usuarioService.create(newUsuario);

      assertNotNull(result.getId());
      assertEquals("carlos@email.com", result.getEmail());
      verify(usuarioRepository, times(1)).save(newUsuario);
  }
    
  @Test
  void updateWithExistingIdMustUpdate() {
      when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(defaultUsuario));
      
      Usuario updatedUsuario = new Usuario("Maria Silva Updated", "maria.updated@email.com", "1111111111");
      
      when(usuarioRepository.save(any(Usuario.class))).thenReturn(defaultUsuario);

      Usuario resultado = usuarioService.update(VALID_ID, updatedUsuario);

      assertEquals("Maria Silva Updated", resultado.getNome());
      assertEquals("maria.updated@email.com", resultado.getEmail());
      assertEquals("1111111111", resultado.getTelefone());
      
      verify(usuarioRepository, times(1)).save(defaultUsuario);
  }

  @Test
  void updateWithNonExistentIdMustThrowException() {
      when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());
      Usuario updatedUsuario = new Usuario("X", "x@email.com", "123");

      assertThrows(ResourceNotFoundException.class, () -> {
          usuarioService.update(INVALID_ID, updatedUsuario);
      }, "An exception should be thrown when the user is not found.");

      verify(usuarioRepository, never()).save(any(Usuario.class));
  }
  
  @Test
  void deleteWithExistingIdMustCallDeleteFromRepository() {
      when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(defaultUsuario));

      usuarioService.delete(VALID_ID);

      verify(usuarioRepository, times(1)).findById(VALID_ID);
      verify(usuarioRepository, times(1)).delete(defaultUsuario);
  }

  @Test
  void deleteWithNonExistentIdMustThrowExceptionAndSkipDelete() {
      when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class, () -> {
          usuarioService.delete(INVALID_ID);
      }, "An exception should be thrown if the user to be deleted cannot be found.");

      verify(usuarioRepository, never()).delete(any(Usuario.class));
  }
}
