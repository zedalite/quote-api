package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.tables.records.QuotesRecord;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteMapperTest {

  private static final QuoteMapper instance = QuoteMapper.INSTANCE;

  @Test
  @DisplayName("Should map quoteRecord to quote")
  void shouldMapQuoteRecordToQuote() {
    final var quoteRec = new QuotesRecord(0, "test", LocalDateTime.MIN, "Successful test.", "sub", 2);

    final var quote = instance.mapToQuote(quoteRec);

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isZero();
    assertThat(quote.author()).isEqualTo("test");
    assertThat(quote.creationDate()).isEqualTo(LocalDateTime.MIN);
    assertThat(quote.text()).isEqualTo("Successful test.");
    assertThat(quote.context()).isEqualTo("sub");
    assertThat(quote.creatorId()).isEqualTo(2);
  }

  @ParameterizedTest
  @DisplayName("Should map empty quoteRecord to null")
  @NullSource
  void shouldMapEmptyQuoteRecordToNull(final QuotesRecord quotesRecord) {
    final var quote = instance.mapToQuote(quotesRecord);

    assertThat(quote).isNull();
  }

  @Test
  @DisplayName("Should map quoteRecords to quotes")
  void shouldMapQuoteRecordsToQuotes() {
    final var quoteRecSonar = new QuotesRecord(0, "sonar", LocalDateTime.MIN, "I like code coverage.", "sub", 3);
    final var quoteRecMapper = new QuotesRecord(1, "mapper", LocalDateTime.MAX, "Mappers facilitate the work.", null, 2);
    final var quotesRecords = List.of(quoteRecSonar, quoteRecMapper);

    final var quotes = instance.mapToQuoteList(quotesRecords);

    final var quoteSonar = quotes.get(0);
    final var quoteMapper = quotes.get(1);

    final var softly = new SoftAssertions();

    softly.assertThat(quoteSonar).isNotNull();
    softly.assertThat(quoteSonar.id()).isZero();
    softly.assertThat(quoteSonar.author()).isEqualTo("sonar");
    softly.assertThat(quoteSonar.creationDate()).isEqualTo(LocalDateTime.MIN);
    softly.assertThat(quoteSonar.text()).isEqualTo("I like code coverage.");
    softly.assertThat(quoteSonar.context()).isEqualTo("sub");
    softly.assertThat(quoteSonar.creatorId()).isEqualTo(3);

    softly.assertThat(quoteMapper).isNotNull();
    softly.assertThat(quoteMapper.id()).isEqualTo(1);
    softly.assertThat(quoteMapper.author()).isEqualTo("mapper");
    softly.assertThat(quoteMapper.creationDate()).isEqualTo(LocalDateTime.MAX);
    softly.assertThat(quoteMapper.text()).isEqualTo("Mappers facilitate the work.");
    softly.assertThat(quoteMapper.context()).isNull();
    softly.assertThat(quoteMapper.creatorId()).isEqualTo(2);

    softly.assertAll();
  }

  @ParameterizedTest
  @DisplayName("Should map empty quoteRecords to null")
  @NullSource
  void shouldMapEmptyQuoteRecordsToNull(final List<QuotesRecord> quotesRecords) {
    final var quotes = instance.mapToQuoteList(quotesRecords);

    assertThat(quotes).isNull();
  }
}
