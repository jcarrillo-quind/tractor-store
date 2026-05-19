package com.tractorstore.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tractor-store.cors")
public class CorsProperties {

  private List<String> allowedOrigins = List.of("http://localhost:4200", "http://127.0.0.1:4200");

  public List<String> getAllowedOrigins() {
    return allowedOrigins;
  }

  public void setAllowedOrigins(List<String> allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }
}
