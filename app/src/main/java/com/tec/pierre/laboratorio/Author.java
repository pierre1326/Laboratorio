package com.tec.pierre.laboratorio;

public class Author {

  private String name;
  private String lastname;
  private int id;

  public Author(String name, String lastname, int id) {
    this.name = name;
    this.lastname = lastname;
    this.id = id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
}
