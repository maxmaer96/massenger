package com.example.massenger;

import android.graphics.Bitmap;

import java.io.Serializable;


public class UserSerial implements Serializable { // класс в котором будет передаваться исключительно данные о текущем пользователе
    private static   String email;
    private static String username;
    private static Bitmap photo; //аватарка
    private static String about_me; //о себе
    private static String age;
    public UserSerial(String email, String username, Bitmap photo, String about_me, String age) {
        this.email= email;
        this.username = username;
        this.photo= photo;
        this.about_me = about_me;
        this.age = age;
    }


    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public  void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
