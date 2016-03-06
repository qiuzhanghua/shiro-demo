package com.example;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.example.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.IDialect;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
public class ShiroWebApplication {

  @Autowired
  private DataSource dataSource;

  @Autowired
  UserRepository userRepository;

  @Bean
  public ObjectMapper jacksonObjectMapper() {
    System.out.println(dataSource);
    System.out.println(userRepository);
    return new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .setDateFormat(new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss.S"));
  }

  @Bean
	public ShiroDialect shiroDialect() {
    return new ShiroDialect();
	}



  @Bean
	public Collection<IDialect> dialects() {
		Collection<IDialect> dialects = new HashSet<IDialect>();
//		dialects
//				.add(new org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect());
		dialects.add(shiroDialect());
		return dialects;
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ShiroWebApplication.class, args);
//		System.out.println(applicationContext.getBean(UserRepository.class));
//		System.out.println(applicationContext.getBean(DataSource.class));

	}



}
