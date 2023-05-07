package com.example.massenger;

public class User { // класс в котором будет передаваться данные о пользователе
    public static String email;
    public static String username;
    public static String photo; //аватарка
    private static String password;
    public static String marital_status; //семейное положение
    public static String about_me; //о себе
    public static Integer age;
    public User(String email, String username, String photo, String password, String marital_status, String about_me, Integer age) {
        this.email= email;
        this.username = username;
        this.photo= photo;
        this.password= password;
        this.marital_status= marital_status;
        this.about_me = about_me;
        this.age = age;
    }

}
