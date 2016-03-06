package com.example.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by qiuzhanghua on 16/3/5.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {
  @RequestMapping(value = "login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }

  @RequestMapping(value = "login", method = RequestMethod.POST)
  public String toLogin(String username, String password) {
    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
    Subject currentUser = SecurityUtils.getSubject();
    try {
      currentUser.login(usernamePasswordToken);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (currentUser.isAuthenticated())
      return "index";
    else {
      usernamePasswordToken.clear();
      return "redirect:login";
    }
  }

  @RequestMapping("")
  public String home() {
    return "index";
  }
}
