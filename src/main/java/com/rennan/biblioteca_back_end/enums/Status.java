package com.rennan.biblioteca_back_end.enums;

public enum Status {
  EMPRESTADO("Emprestado"), DEVOLVIDO("Devolvido");

  private String value;

  private Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
