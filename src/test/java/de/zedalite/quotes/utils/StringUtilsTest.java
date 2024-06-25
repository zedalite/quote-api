package de.zedalite.quotes.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

  @Test
  @DisplayName("Should throw when constructor called")
  void shouldThrowWhenConstructorCalled() {
    assertThatCode(StringUtils::new).isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("Should extract user ids")
  void shouldExtractUserIds() {
    final String text = "<@2>The quick <@2313> brown fox <@jumps> <@-1>over the la<@13>zy dog.<@3>";

    final List<Integer> userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).containsAll(List.of(2, 2313, 13, 3));
  }

  @Test
  @DisplayName("Should return empty list when no user ids found")
  void shouldReturnEmptyListWhenNoUserIdsFound() {
    final String text = "The quick brown fox jumps over the lazy dog.";

    final List<Integer> userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).isEmpty();
  }

  @Test
  @DisplayName("Should handle too large numbers")
  void shouldHandleTooLargeNumbers() {
    final String text = "<@2147483648>";

    final List<Integer> userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).isEmpty();
  }
}
