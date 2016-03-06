package com.example.domain;

/**
 * Created by qiuzhanghua on 16/3/5.
 */
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  private String login;

  @NotNull
  @Column(length = 64)
  private String password;

  @NotNull
  @Column(length = 80)
  private String salt;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private Set<Role> roles;

  @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
  private Set<Permission> permissions;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }
}