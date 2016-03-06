package com.example.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by qiuzhanghua on 16/3/5.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByLogin(String login);
}

/**
 public class TestClass {

@Inject
private UserDao userDao;

public void register(final String login, final String password) {
User user = new User();
user.setLogin(login);
user.setSalt(JpaSecurityUtil.getSalt());
user.setPassword(JpaSecurityUtil.hashPassword(password, user.getSalt()));
userDao.persist(user);
}
 */
