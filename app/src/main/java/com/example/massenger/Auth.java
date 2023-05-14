package com.example.massenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;



public class Auth extends AppCompatActivity {

    String conn_res;
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
        } else {
            if (!username.getText().toString().isEmpty()&&!age.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()&&!username.getText().toString().isEmpty() ) {
                try_reg(username.getText().toString(),age.getText().toString(),email.getText().toString(),password.getText().toString());
            }
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
    public void try_reg(String username,String age,String email,String password) {

        String api = "http://messapi.space/api/users/create.php/";
//        JSONObject params = new JSONObject();
//        try {
//            params.put("username", username);
//            params.put("email", email);
//            params.put("password", password);
//            params.put("age", age);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestQueue queue = Volley.newRequestQueue(Auth.this);
//        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,api,params,
//                response ->{
//
//                err_text.setVisibility(View.VISIBLE);
//                err_text.setText(response.toString());
//                },error -> {
//            if(error.getMessage()!= null) Log.e("Volley", error.getMessage());
//            else Log.e("Volley", error.toString());
//
//        }
//                );
        RequestQueue queue = Volley.newRequestQueue(Auth.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api,
                response ->
        {
            err_text.setVisibility(View.VISIBLE);
            err_text.setText(response.toString());

        },
                error -> Log.e("Volley", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("age", age);

                return params;
            }
        };
        queue.add(postRequest);
    }

}





