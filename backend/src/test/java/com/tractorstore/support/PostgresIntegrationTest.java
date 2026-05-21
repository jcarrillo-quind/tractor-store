package com.tractorstore.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integración REST con PostgreSQL y migraciones Flyway.
 *
 * <p>Con Docker: Testcontainers. Sin Docker: define {@code TRACTOR_DB_URL} (p. ej. tras {@code
 * docker compose up}) o los tests se deshabilitan ({@code disabledWithoutDocker}).
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("dev")
public abstract class PostgresIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("tractorstore")
          .withUsername("tractor")
          .withPassword("tractor");

  @DynamicPropertySource
  static void datasourceProperties(DynamicPropertyRegistry registry) {
    String externalUrl = System.getenv("TRACTOR_DB_URL");
    if (externalUrl != null && !externalUrl.isBlank()) {
      registry.add("spring.datasource.url", () -> externalUrl);
      registry.add(
          "spring.datasource.username",
          () -> System.getenv().getOrDefault("TRACTOR_DB_USER", "tractor"));
      registry.add(
          "spring.datasource.password",
          () -> System.getenv().getOrDefault("TRACTOR_DB_PASSWORD", "tractor"));
      return;
    }
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }
}
