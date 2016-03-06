package com.example;

import com.example.domain.User;
import com.example.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiroDemoApplication.class)
public class ShiroDemoApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Test
	public void contextLoads() {
	}

	// @Test
	public void addUser() {

    String login = "you";
    String password = "123456";
    User user = new User();
    user.setLogin(login);
    user.setSalt(ShiroDemoApplication.getSalt());
    user.setPassword(ShiroDemoApplication.hashPassword(password, user.getSalt()));
    userRepository.save(user);

  }
}
