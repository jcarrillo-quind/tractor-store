package com.tractorstore.config;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.bootstrap.adapters.ClasspathJsonSeedDataAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataConfig {

  @Bean
  SeedBundle seedBundle() {
    return ClasspathJsonSeedDataAdapter.load("seed-data.json");
  }
}
