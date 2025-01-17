package com.sparta.homework_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HomeworkLoginApplication {

  public static void main(String[] args) {
    SpringApplication.run(HomeworkLoginApplication.class, args);
  }

}
