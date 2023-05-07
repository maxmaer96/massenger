package com.example.massenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Auth extends AppCompatActivity {
    EditText email,password,age,username;
    TextView err_text;
    ImageView nice_logo;
    Button login,sign_up;
    private boolean registration_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        registration_mode=false;
        email=findViewById(R.id.email_text_field); //находим все элементы
        password=findViewById(R.id.password_text_field);
        age=findViewById(R.id.age_text_field);
        username=findViewById(R.id.username_text_field);
        err_text=findViewById(R.id.error_text);
        nice_logo=findViewById(R.id.nice_logo);
        login=findViewById(R.id.log_in);
        sign_up=findViewById(R.id.sing_up);

    }

    public void Registartion(View view) {
        if (!registration_mode) { //если не вошли в режим регистрации то входим
            nice_logo.setVisibility(View.INVISIBLE);
            age.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            registration_mode=true;
        } else

        {

        }

    }

    public void login(View view) {
        if (registration_mode){ //выходим из режима регистрации если мы в нем прибывали
            nice_logo.setVisibility(View.VISIBLE);
            age.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            registration_mode=false;
        }

    }
}