package com.example.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue
  private long id;

  @Size(max = 100)
  private String roleName;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}