package com.tractorstore.cart.support;

import com.tractorstore.config.CartSessionProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/** Resuelve o crea el id de sesión del carrito vía cookie HttpOnly. */
@Component
public class CartSessionSupport {

  private final CartSessionProperties properties;

  public CartSessionSupport(CartSessionProperties properties) {
    this.properties = properties;
  }

  public String resolveSessionId(HttpServletRequest request, HttpServletResponse response) {
    Optional<String> existing = readSessionId(request);
    if (existing.isPresent()) {
      return existing.get();
    }
    String sessionId = UUID.randomUUID().toString();
    writeSessionCookie(response, sessionId);
    return sessionId;
  }

  public Optional<String> readSessionId(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return Optional.empty();
    }
    return Arrays.stream(request.getCookies())
        .filter(c -> properties.getCookieName().equals(c.getName()))
        .map(Cookie::getValue)
        .filter(v -> v != null && !v.isBlank())
        .findFirst();
  }

  public void writeSessionCookie(HttpServletResponse response, String sessionId) {
    ResponseCookie cookie =
        ResponseCookie.from(properties.getCookieName(), sessionId)
            .path(properties.getPath())
            .maxAge(properties.getMaxAgeSeconds())
            .httpOnly(properties.isHttpOnly())
            .secure(properties.isSecure())
            .sameSite(properties.getSameSite())
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void clearSessionCookie(HttpServletResponse response) {
    ResponseCookie cookie =
        ResponseCookie.from(properties.getCookieName(), "")
            .path(properties.getPath())
            .maxAge(0)
            .httpOnly(properties.isHttpOnly())
            .secure(properties.isSecure())
            .sameSite(properties.getSameSite())
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
