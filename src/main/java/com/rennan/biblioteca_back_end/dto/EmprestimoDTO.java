package com.rennan.biblioteca_back_end.dto;

import jakarta.validation.constraints.NotNull;

public record EmprestimoDTO(
  @NotNull Long usuarioId,
  @NotNull Long livroId
) { }
