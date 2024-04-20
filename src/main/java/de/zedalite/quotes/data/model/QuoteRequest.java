package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonSerialize
@JsonDeserialize
public record QuoteRequest(
  @Schema(description = "Person who said the quote", example = "Scott") @NotBlank @Size(max = 32) String author,

  @Schema(description = "What the person said", example = "The universe is so extraordinary")
  @NotBlank
  @Size(max = 256)
  String text,

  @Schema(description = "In what context it was said", example = "At the press conference")
  @Size(max = 64)
  String context
) {}
