package com.sparta.homework_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HomeworkLoginApplication {

  public static void main(String[] args) {
    // test
    SpringApplication.run(HomeworkLoginApplication.class, args);
  }

}
