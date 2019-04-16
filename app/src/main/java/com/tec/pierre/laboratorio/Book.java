package com.tec.pierre.laboratorio;

import java.util.Date;

public class Book {

  private Author author;
  private String title;
  private Date publish_date;

  public Book(Author author, String title, Date publish_date) {
    this.author = author;
    this.title = title;
    this.publish_date = publish_date;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getPublish_date() {
    return publish_date;
  }

  public void setPublish_date(Date publish_date) {
    this.publish_date = publish_date;
  }

  @Override
  public String toString() {
    return "Titulo: " + title + " Autor: " + author.getName() + " " + author.getLastname() + " Fecha: " + publish_date.toString();
  }

}
