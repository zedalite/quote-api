package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record Quote(

  @NotNull
  @PositiveOrZero
  Integer id,
  @NotBlank
  @Size(max = 32)
  String author,

  @NotNull
  @PastOrPresent
  LocalDateTime creationDate,

  @NotBlank
  @Size(max = 256)
  String text,

  @Size(max = 64)
  String context,

  @PositiveOrZero
  Integer creatorId

) {
}
