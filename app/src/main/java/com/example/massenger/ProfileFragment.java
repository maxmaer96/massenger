package com.example.massenger;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ProfileFragment extends Fragment {
    private UserSerial curr_user;
    String photo_enc;
    Uri imageUri;
    TextView email, username, about, age;
    EditText editAbout;
    final int Pick_image = 1;
    FloatingActionButton updBtn, backBtn, logOutBT;
    ScrollView textHolder;
    ImageView avatar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (!bundle.getSerializable("current_user").equals(null)) {
            curr_user = (UserSerial) bundle.getSerializable("current_user");
        }

        Log.i("tes", curr_user.getUsername()+ curr_user.getAge()+ curr_user.getAbout_me());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);

        editAbout = view.findViewById(R.id.editAbout);
        updBtn = view.findViewById(R.id.updBtn);
        backBtn = view.findViewById(R.id.backBtn);
        textHolder = view.findViewById(R.id.aboutHolder);
        avatar = view.findViewById(R.id.avatar_view);
        logOutBT= view.findViewById(R.id.logOutBt);



        email = view.findViewById(R.id.aboutTextViewItem);
        email.setText("Ваша почта: " + curr_user.getEmail());

        username = view.findViewById(R.id.usernameTextView);
        username.setText("Ваше имя: " + curr_user.getUsername());

        about = view.findViewById(R.id.aboutTextView);
        if (!curr_user.getAbout_me().equals("пусто")) {about.setText(curr_user.getAbout_me());}

        age = view.findViewById(R.id.ageTextView);
        age.setText("Вам: " + curr_user.getAge() + " лет");

        if(curr_user.getPhoto() != null) {
            avatar.setImageBitmap(curr_user.getPhoto());
        }

        about.setOnClickListener(new View.OnClickListener() { //покажем элементы нужные для добавления инфы
            @Override
            public void onClick(View v) {
                about.setVisibility(View.GONE);
                editAbout.setVisibility(View.VISIBLE);
                updBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                editAbout.setText(about.getText());

            }
        });
        updBtn.setOnClickListener(new View.OnClickListener() { //запрос в базу на изменения
            @Override
            public void onClick(View v) {
                if (editAbout.getText().toString() != "" && curr_user.getUsername() != null) {
                    String api = "https://messapi.space/api/users/updateAbout.php";

                    JSONObject json = new JSONObject();
                    try {
                        json.put("username", curr_user.getUsername());
                        json.put("about", editAbout.getText().toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                            response -> {
                                Toast.makeText(getActivity(), "Данные успешно обновлены", LENGTH_SHORT).show();
                                about.setText(editAbout.getText().toString());
                                curr_user.setAbout_me(about.getText().toString());
                                editAbout.setText("");
                                about.setVisibility(View.VISIBLE);
                                editAbout.setVisibility(View.GONE);
                                updBtn.setVisibility(View.GONE);
                                backBtn.setVisibility(View.GONE);


                            },
                            error -> {
                                if (error.getMessage() != null) Log.e("Volley", error.getMessage());
                                else Log.e("Volley", error.toString());
                                Toast.makeText(getActivity(), "Не оставляй поле пустым!!", LENGTH_SHORT).show();

                            });

                    queue.add(postRequest);
                }
            }
        });
        logOutBT.setOnClickListener(new View.OnClickListener() { //обрабатываем выход из аккаунта
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("acount",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("jwt","");
                editor.apply();
                Intent intent = new Intent(getActivity(),Auth.class);
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() { //уберем элементы нужные для добавления инфы , если юзер передумал
            @Override
            public void onClick(View v) {
                about.setVisibility(View.VISIBLE);
                editAbout.setVisibility(View.GONE);
                updBtn.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImg_res();
            }
        });
        return view;
    }

    public void getImg_res() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Pick_image);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    final Uri imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo_enc=(ImageHandler.convert(selectedImage).replaceAll("\\s+",""));
                    curr_user.setPhoto(selectedImage);
                    Toast.makeText(this.getActivity(), "фотография загружена успешно", LENGTH_SHORT).show();
                    avatar.setImageBitmap(curr_user.getPhoto());

                    //запрос в базу на добавление изображения
                    if (curr_user.getPhoto()!=null) {
                        String api = "https://messapi.space/api/users/updatePhoto.php";
                        JSONObject json = new JSONObject();
                        try {
                            json.put("username", curr_user.getUsername());
                            json.put("photo", photo_enc);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                                response -> {
                                    Toast.makeText(this.getActivity(), "успешно загружено в базу", LENGTH_SHORT).show();
                                },
                                error -> {
                                    Toast.makeText(this.getActivity(), "ошибка!", LENGTH_SHORT).show();
                                });
                        queue.add(postRequest);
                    }
                }
        }
    }
}

