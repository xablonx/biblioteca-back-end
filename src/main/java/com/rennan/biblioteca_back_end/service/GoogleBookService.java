package com.rennan.biblioteca_back_end.service;

import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.rennan.biblioteca_back_end.dto.GoogleBookDTO;
import com.rennan.biblioteca_back_end.model.Livro;

import reactor.core.publisher.Mono;

@Service
public class GoogleBookService {

  private final WebClient webClient;
  private static final String APIURL = "https://www.googleapis.com/books/v1/volumes";

  public GoogleBookService(WebClient.Builder builder) {
    this.webClient = builder.baseUrl(APIURL).build();
  }

  public List<Livro> searchLivrosByTitulo(String titulo) {
    if (titulo == null || titulo.trim().isEmpty()) {
      return List.of();
    }

    Mono<GoogleBookDTO> mono = this.webClient.get()
      .uri(uriBuilder -> uriBuilder.queryParam("q", titulo).queryParam("maxResults", 10).build())
      .retrieve()
      .bodyToMono(GoogleBookDTO.class)
      .timeout(Duration.ofSeconds(5))
      .onErrorResume(error -> {
        System.err.println("Erro ao buscar no Google Books: " + error.getMessage());
        return Mono.empty();
      });
    
    GoogleBookDTO googleBookDTO = mono.block();

    return googleBookDTO != null ? googleBookDTO.toLivros() : List.of();
  }
}
