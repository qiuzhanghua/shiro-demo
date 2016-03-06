package com.example;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;

@SpringBootApplication
public class ShiroDemoApplication {

  @Autowired
  private DataSource dataSource;


  @Bean
  public String appName() {
    System.out.println(dataSource);
    return "Shiro-domain";
  }

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ShiroDemoApplication.class, args);
//		System.out.println(applicationContext.getBean(UserRepository.class));
//		System.out.println(applicationContext.getBean(DataSource.class));

	}


  public static final String REALM_NAME = "MY_REALM";
  public static final int HASH_ITERATIONS = 200;


  public static final int SALT_LENGTH = 80;
  public static final int PASSWORD_LENGTH = 64;

  public static String getSalt() {
    return new SecureRandomNumberGenerator().nextBytes(60).toBase64();
  }

  public static String hashPassword(final String value, final String salt) {
    final Sha256Hash sha256Hash = new Sha256Hash(value, salt, HASH_ITERATIONS);
    return sha256Hash.toHex();
  }

}
