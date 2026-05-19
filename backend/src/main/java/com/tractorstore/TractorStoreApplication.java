package com.tractorstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TractorStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(TractorStoreApplication.class, args);
  }
}
