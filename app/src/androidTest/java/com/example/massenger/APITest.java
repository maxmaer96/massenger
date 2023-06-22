package com.example.massenger;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class APITest {
    @Test
    public void createUserApiFunWorking() throws JSONException {
        String api = "https://messapi.space/api/users/create.php";
        JSONObject json = new JSONObject();

        json.put("username", "newbie");
        json.put("email", "newbie@gmail.com");
        json.put("password", "Password23");
        json.put("age", "20");

        RequestQueue queue = Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext());
        @SuppressLint("SuspiciousIndentation") JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                    assertNotNull(response);
                },
                error -> {
                    assertNull(error);
                });

        queue.add(postRequest);
    }
    @Test
    public void LogInApiFunWorking() throws JSONException {
        String api = "https://messapi.space/api/users/login.php";
        JSONObject json = new JSONObject();

        json.put("email", "test@test.com");
        json.put("password", "tes");

        RequestQueue queue = Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {

                    try {
                         assertNotNull(response.getString("jwt"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    assertNull(error);

                });

        queue.add(getRequest);
    }
    @Test
    public void updateProfileApiFunWorking() throws JSONException {
        String api = "https://messapi.space/api/users/updateAbout.php";

        JSONObject json = new JSONObject();
        try {
            json.put("username", "test");
            json.put("about", "testing something");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        RequestQueue queue = Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                    assertNotNull(response);
                },
                error -> {
                    assertNull(error);
                });

        queue.add(postRequest);
    }
    @Test
    public void getFriendsApiFunWorking() throws JSONException {
        String api = "https://messapi.space/api/friend/read.php/";
        JSONObject json = new JSONObject();
            json.put("friend1", "test");
        RequestQueue queue = Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                   assertNotNull(response);
                },
                error -> {
                   assertNull(error);
                });
        queue.add(request);
    }
    @Test
    public void getMessagesApiFunWorking() throws JSONException {
        String api = "https://messapi.space/api/messages/read.php/";
        JSONObject json = new JSONObject();
        try {
            json.put("user1","test");
            json.put("user2","maxmaer1996");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestQueue queue = Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                response -> {
                        assertNotNull(response);
                },
                error -> {
                        assertNull(error);
                });
        queue.add(request);

    }
}