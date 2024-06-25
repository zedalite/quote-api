package de.zedalite.quotes.data.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthLog(
  @NotNull String status,

  @NotBlank String uri,

  @NotBlank String query,

  @NotBlank String client,

  @NotBlank String user
) {
  @Override
  public String toString() {
    return String.format("auth [%s %s%s, client=%s, user=%s]", status, uri, query, client, user);
  }
}
