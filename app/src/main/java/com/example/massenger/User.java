package com.example.massenger;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable { // класс в котором будут ХРАНИТЬСЯ данные о пользователе
    private String Email;
    private String Username;
    private transient Bitmap Photo; //аватарка
    private String About_me; //о себе
    private String Age;
    public User(String email, String username, Bitmap photo, String about_me, String age) {
        this.Email = email;
        this.Username = username;
        this.Photo = photo;
        this.About_me = about_me;
        this.Age = age;
    }
    public User(String about_me, String username, Bitmap photo){
        this.Email = "whatever";
        this.Username = username;
        this.Photo = photo;
        this.About_me =about_me;
        this.Age ="пусто";
    }

    public  String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public Bitmap getPhoto() {
        return Photo;
    }

    public  void setPhoto(Bitmap photo) {
        this.Photo = photo;
    }

    public String getAbout_me() {
        return About_me;
    }

    public void setAbout_me(String about_me) {
        this.About_me = about_me;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        this.Age = age;
    }
}
