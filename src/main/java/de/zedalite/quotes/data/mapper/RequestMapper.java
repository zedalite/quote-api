package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.model.RequestLog;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface RequestMapper {

  RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

  @Mapping(target = "uri", source = "request.requestURI")
  @Mapping(target = "query", source = "request.queryString", qualifiedByName = "mapQuery")
  @Mapping(target = "client", source = "request", qualifiedByName = "mapClient")
  RequestLog map(final HttpServletRequest request, final Integer status, final String user, final Long time);

  @Named("mapClient")
  default String mapClient(final HttpServletRequest request) {
    return Objects.requireNonNullElse(request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
  }

  @Named("mapQuery")
  default String mapQuery(final String query) {
    return (query != null && !query.isBlank()) ? "?" + query : "";
  }
}
