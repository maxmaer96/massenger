package com.example.massenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;

public class Splash extends AppCompatActivity {
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        SharedPreferences preferences = getSharedPreferences("acount",MODE_PRIVATE);
        String jwt_enc = preferences.getString("jwt","");
        if(jwt_enc.length()>1){
            JWT jwt = new JWT(jwt_enc);
            Intent intent = new Intent(Splash.this, Main.class); //в случае если пользователь уже авторизован переносим его данные в систему
           UserSerial curr_userSerial = new UserSerial(jwt.getClaim("email").asString(),jwt.getClaim("username").asString(),ImageHandler.convert(jwt.getClaim("photo").asString()),jwt.getClaim("about").asString(),jwt.getClaim("age").asString());
            intent.putExtra("user", curr_userSerial);
            startActivity(intent);
            finish();
        } else { //а если нет , то идем по умолчанию
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);
            finish();
        }
    }
}
