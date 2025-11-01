package com.rennan.biblioteca_back_end;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rennan.biblioteca_back_end.enums.Status;
import com.rennan.biblioteca_back_end.model.Emprestimo;
import com.rennan.biblioteca_back_end.model.Livro;
import com.rennan.biblioteca_back_end.model.Usuario;
import com.rennan.biblioteca_back_end.repository.EmprestimoRepository;
import com.rennan.biblioteca_back_end.repository.LivroRepository;
import com.rennan.biblioteca_back_end.repository.UsuarioRepository;

@SpringBootApplication
public class BibliotecaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaBackEndApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
	LivroRepository livroRepository,
	EmprestimoRepository emprestimoRepository) {
		return args -> {
			usuarioRepository.deleteAll();

				Usuario usuario = new Usuario();
				usuario.setNome("John Doe");
				usuario.setEmail("johndoe@gmail.com");
				usuario.setTelefone("5544999389151");
				usuarioRepository.save(usuario);

				Livro livro = new Livro();
				livro.setTitulo("The Witcher");
				livro.setAutor("Andrzej Sapkowski");
				livro.setIsbn("978-85-333-0227-3");
				livro.setCategoria("Fantasia");
				livro.setDataPublicacao(LocalDate.now());
				livroRepository.save(livro);

				Livro livro2 = new Livro();
				livro2.setTitulo("Duna");
				livro2.setAutor("Frank Herbert");
				livro2.setIsbn("978-85-333-0227-4");
				livro2.setCategoria("Fantasia");
				livro2.setDataPublicacao(LocalDate.now());
				livroRepository.save(livro2);

				Emprestimo emprestimo = new Emprestimo();
				emprestimo.setUsuario(usuario);
				emprestimo.setLivro(livro);
				emprestimo.setStatus(Status.EMPRESTADO);
				emprestimo.setDataEmprestimo(LocalDateTime.now());
				emprestimoRepository.save(emprestimo);
		};
	}	
}
