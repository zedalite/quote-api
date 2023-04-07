package de.zedproject.quotesapi.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

  @Bean
  public ServletContextRequestLoggingFilter requestLoggingFilter() {
    final var filter = new ServletContextRequestLoggingFilter();

    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setIncludeClientInfo(true);
    filter.setIncludeHeaders(false);
    filter.setMaxPayloadLength(512);

    return filter;
  }
}
