package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.QuoteResponse;
import de.zedalite.quotes.data.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class QuoteGenerator {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  public static QuoteRequest getQuoteRequest() {
    return new QuoteRequest("tester", "quotes are 42", "@Mars");
  }

  public static Quote getQuote() {
    return new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home", Optional.of(1));
  }

  public static QuoteResponse getQuoteMessage() {
    return QUOTE_MAPPER.mapToResponse(getQuote(), getMentions());
  }

  public static List<Quote> getQuotes() {
    return List.of(
      new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home", Optional.of(4)),
      new Quote(2, "qa", LocalDateTime.MIN, "tests are important", null, Optional.of(5)),
      new Quote(3, "pipeline", LocalDateTime.MAX, "Going brrrrr", "@Work", null)
    );
  }

  public static List<QuoteResponse> getQuoteMessages() {
    return getQuotes().stream().map(quote -> QUOTE_MAPPER.mapToResponse(quote, getMentions())).toList();
  }

  private static List<User> getMentions() {
    return List.of(new User(12, "mentionedUser", "secret555", "mentionedUser", LocalDateTime.MIN));
  }
}
