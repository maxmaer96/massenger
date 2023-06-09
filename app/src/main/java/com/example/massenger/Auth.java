package com.example.massenger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;



public class Auth extends AppCompatActivity {

    public String jwt_enc;
    EditText email,password,age,username;
    TextView err_text;
    ImageView nice_logo;
    Button login,sign_up;
    private boolean registration_mode;
    UserSerial curr_userSerial;


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

    public void Registartion(View view) throws JSONException {
        if (!registration_mode)  ChangeMode(); //если не вошли в режим регистрации то входим
         else {
            if (!username.getText().toString().isEmpty()&&!age.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()&&!username.getText().toString().isEmpty() ) {
                if (email.getText().toString().matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,3})$")){
                    if((password.getText().toString().length()>6)&&(password.getText().toString().matches(".*[A-Z].*"))&&(password.getText().toString().matches(".*[0-9].*"))){
                        try {
                            if (age.getText().toString().length() < 4) {
                                Integer ageasnubmer=0 + Integer.parseInt(age.getText().toString());
                                if (ageasnubmer<=120) {
                                    try_reg(username.getText().toString(),age.getText().toString(),email.getText().toString(),password.getText().toString());
                                } //после проверок отправляем запрос на базу
                                else{
                                    err_text.setVisibility(View.VISIBLE);
                                    err_text.setText("а не староват ли ты для таких приложений ?");
                                }

                            } else {

                                err_text.setVisibility(View.VISIBLE);
                                err_text.setText("а не староват ли ты для таких приложений ?");
                            }
                        }catch (NumberFormatException e)
                        {
                            err_text.setVisibility(View.VISIBLE);
                            err_text.setText("то что ты ввёл в поле возраст это не число, дружище ");                            //куча проверок на дурачков и тестеров
                        }
                    }else
                    {
                        err_text.setVisibility(View.VISIBLE);
                        err_text.setText("ваш пароль должен быть длиннее 6-ти символов, и содержать в себе один большой символ и хотя бы одну цифру");
                    }
                } else
                {
                    err_text.setVisibility(View.VISIBLE);
                    err_text.setText("ваша почта не соответствует формату");
                }
            }
            else {
                err_text.setVisibility(View.VISIBLE);
                err_text.setText("не оставляй поля пустыми");
            }
        }

    }

    public void login(View view) throws JSONException {
        if (registration_mode) ChangeMode(); //выходим из режима регистрации если мы в нем прибывали
        else if (!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()){ //проверяем в нужном ли формате ввел пользователь данные
             try_log(email.getText().toString(),password.getText().toString());
        }else {
            Toast.makeText(this,"не оставляй поля пустыми !",Toast.LENGTH_SHORT).show();
        }
    }
    public void try_reg(String username,String age,String email,String password) throws JSONException { //метод отправляющий на сервер запрос на регистрацию

        String api = "https://messapi.space/api/users/create.php";
        JSONObject json = new JSONObject();

            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            json.put("age", age);

        RequestQueue queue = Volley.newRequestQueue(Auth.this);
        @SuppressLint("SuspiciousIndentation") JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                    err_text.setVisibility(View.INVISIBLE);
                    Toast.makeText(Auth.this,"Регистрация прошла успешно!",Toast.LENGTH_SHORT).show();
                    ChangeMode();
                    },
                error -> {
                 if (error.getMessage() != null) Log.e("Volley", error.getMessage());
                    else Log.e("Volley", error.toString());
                    err_text.setVisibility(View.VISIBLE);
                    err_text.setText("пользователь с таким username уже существует");
                    ClearFields();
                    });

        queue.add(postRequest);
    }
    public void try_log(String email,String password) throws JSONException {  //метод с запросом на авторизацию
        String api = "https://messapi.space/api/users/login.php";
        JSONObject json = new JSONObject();

        json.put("email", email);
        json.put("password", password);

        RequestQueue queue = Volley.newRequestQueue(Auth.this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                    Toast.makeText(Auth.this,"Добро пожаловать",Toast.LENGTH_SHORT).show();
                    try {
                        if(response.getString("jwt")!=null) {
                            jwt_enc = response.getString("jwt");  //перехватываем токен в котором содержаться данные пользователя
                            Log.i("Volley","jwt is:"+ jwt_enc);
                            JWT jwt = new JWT(jwt_enc);

                            Intent intent = new Intent(Auth.this, Main.class); //и передаем токен на следующую активность

                            curr_userSerial = new UserSerial(jwt.getClaim("email").asString(),jwt.getClaim("username").asString(),ImageHandler.convert(jwt.getClaim("photo").asString()),jwt.getClaim("about").asString(),jwt.getClaim("age").asString());
                            intent.putExtra("user", curr_userSerial);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    if (error.getMessage()!=null)Log.e("Volley", error.getMessage() );
                    Log.e("Volley", error.toString() );
                    Toast.makeText(Auth.this,"ошибка в данных!",Toast.LENGTH_SHORT).show();
                    ClearFields();
                });

        queue.add(getRequest);
    }
        public void ClearFields(){ // очистка полей при смене режима
        email.setText("");
        password.setText("");
        username.setText("");
        age.setText("");
    }
    public void ChangeMode(){ //для смены формы с режима регистрации на режим авторизации
        if(!registration_mode){
            nice_logo.setVisibility(View.INVISIBLE);
            age.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            ClearFields();
            registration_mode=true;
        }
        else
        {
            nice_logo.setVisibility(View.VISIBLE);
            age.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            err_text.setVisibility(View.INVISIBLE);
            ClearFields();
            registration_mode=false;
        }
    }
    public boolean passwordIsStrong(String pass){ //проверяем пароль на корректность
        if (pass.length()<7) return false;
        if(!pass.matches(".*[A-Z].*"))return false; //латиница
        if (!pass.matches(".*[0-9].*")) return false;//хотя бы одну цифру
        return true;
    }

}





