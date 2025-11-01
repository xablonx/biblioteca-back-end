package com.rennan.biblioteca_back_end.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rennan.biblioteca_back_end.model.Livro;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookDTO {

  private List<Item> items;

  public List<Livro> toLivros() {
    if (items == null) {
      return List.of();
    }

    return items.stream()
      .map(Item::toLivro)
      .filter(livro -> livro != null && livro.getTitulo() != null)
      .toList();
  }

  public List<Item> getItems() {
    return this.items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Item {
    private VolumeInfo volumeInfo;

    public Livro toLivro() {
      if (volumeInfo == null) {
        return null;
      }
      return volumeInfo.toLivro();
    }

    public VolumeInfo getVolumeInfo() {
      return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
      this.volumeInfo = volumeInfo;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class VolumeInfo {
    public String title;
    public List<String> authors;
    public String publishedDate;
    public List<String> categories;
    public List<IndustryIdentifier> industryIdentifiers;

    public Livro toLivro() {
      String autor = (authors != null && !authors.isEmpty()) ? authors.get(0) : "Desconhecido";
      String categoria = (categories != null && !categories.isEmpty()) ? categories.get(0) : "Geral";
      String isbn = (industryIdentifiers != null && !industryIdentifiers.isEmpty()) ? industryIdentifiers.get(0).identifier : "N/A";
      
      LocalDate dataPublicacao = LocalDate.of(1900, 1, 1);
      if (publishedDate != null && !publishedDate.isEmpty()) {
        try {
          if (publishedDate.length() >= 10) {
            dataPublicacao = LocalDate.parse(publishedDate.substring(0, 10));
          } else if (publishedDate.length() == 7 && publishedDate.contains("-")) {
            dataPublicacao = LocalDate.parse(publishedDate + "-01");
          } else if (publishedDate.length() == 4) {
            dataPublicacao = LocalDate.of(Integer.parseInt(publishedDate), 1, 1);
          }
        } catch (DateTimeParseException | NumberFormatException exception) {
          System.err.println("Erro ao converter data do Google Books: " + publishedDate + " - " + exception.getMessage());
        }
      }

      return new Livro(title, autor, isbn, dataPublicacao, categoria);
    }    
  }

  public static class IndustryIdentifier {
    public String identifier;
  }
}
