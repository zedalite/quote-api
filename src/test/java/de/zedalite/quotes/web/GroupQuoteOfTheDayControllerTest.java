package de.zedalite.quotes.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.data.model.QuoteResponse;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.service.GroupQuoteOfTheDayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupQuoteOfTheDayControllerTest {

  @InjectMocks
  private GroupQuoteOfTheDayController instance;

  @Mock
  private GroupQuoteOfTheDayService service;

  @Test
  @DisplayName("Should get quote of the day")
  void shouldGetQuoteOfTheDay() {
    final QuoteResponse expectedQuote = QuoteGenerator.getQuoteMessage();
    willReturn(expectedQuote).given(service).findQuoteOfTheDay(anyInt());

    instance.getQuoteOfTheDay(1);

    then(service).should().findQuoteOfTheDay(1);
  }
}
