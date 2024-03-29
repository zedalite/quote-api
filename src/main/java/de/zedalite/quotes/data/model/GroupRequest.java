package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
public record GroupRequest(
  @NotBlank
  @Size(max = 32)
  String name,

  @NotBlank
  @Size(max = 32)
  String displayName,

  @NotNull
  @PastOrPresent
  LocalDateTime creationDate,

  @PositiveOrZero
  Integer creatorId

) {

  public GroupRequest withCreatorId(final Integer creatorId) {
    return new GroupRequest(name, displayName, creationDate, creatorId);
  }
}
