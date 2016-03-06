package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Created by qiuzhanghua on 16/3/5.
 */
@Entity
@Table(name = "permissions")
public class Permission {

  @Id
  @GeneratedValue
  private long id;

  @Size(max = 100)
  private String permission;

  @ManyToOne()
  @JoinColumn(name = "USER_ID")
  private User user;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}