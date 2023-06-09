package com.example.massenger;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DialogeFragment extends Fragment implements OnBackPressedListener {
    EditText messageText;
    BottomNavigationView bottomNavigationView;

    FloatingActionButton sendBt,BackBt;
    ImageView userAva;
    TextView userName;
    RecyclerView messageRecycler;
    Timer timer;
User collocutor ;
UserSerial current_user;
ArrayList<message> messages_stable;

    @Override
    public void onStop() { //здесь мы переопределим метод который срабатывает при сворачивании приложения что бы выдавать уведомления о новых сообщениях
        super.onStop();
        Log.i("not","вот ту должно бы быть уведомление ");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"SwirliChat")
                                                                                                .setSmallIcon(R.mipmap.ic_app_logo).
                                                                                                setContentTitle("Новое сообщение").
                                                                                                setContentText("у вас новое сообщение от ")
                                                                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getActivity());
        Timer closeTimer = new Timer();
        closeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ArrayList<message> messages= new ArrayList<>();

                String api = "https://messapi.space/api/messages/read.php/";
                JSONObject json = new JSONObject();
                try {
                    json.put("user1",current_user.getUsername());
                    json.put("user2",collocutor.getUsername());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                        response -> {
                            try {
                                if (response.getString("count")!=null);
                                int count=response.getInt("count");
                                for(int i =1;i<=count;i++){
                                    messages.add(new message(response.getString("message"+i+" from_who"),response.getString("message"+i+" to_who"),response.getString("message"+i+" when_sent"),response.getString("message"+i)));
                                    if (i==count){
                                        if (!messages.equals(messages_stable));{
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"SwirliChat")
                                                    .setSmallIcon(R.mipmap.ic_app_logo).
                                                    setContentTitle("Новое сообщение").
                                                    setContentText("у вас новое сообщение от ")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                            NotificationManagerCompat notificationManager =
                                                    NotificationManagerCompat.from(getActivity());
                                            notificationManager.notify(127,builder.build());
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        error -> {
                            Log.i("Volley", error.toString());


                        });
                queue.add(request);

            }
        },2000,5000);
  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
            collocutor =(User) bundle.getSerializable("user"); //принимаем друга которому юзер захотел написать
            current_user =(UserSerial) bundle.getSerializable("current_user");


        messages_stable= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialoge, container, false);


        userAva=view.findViewById(R.id.userAva);
        userAva.setImageBitmap(collocutor.getPhoto());
        userAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("person",collocutor);
                bundle.putString("from","dialog");
                AnotherUserProfile anotherUserProfile= new AnotherUserProfile();
                anotherUserProfile.setArguments(bundle);
                FragmentManager fg=  getActivity().getSupportFragmentManager();
                fg.beginTransaction().add(R.id.fragmentsHolder,anotherUserProfile,"4").hide(fg.findFragmentByTag("3")).show(anotherUserProfile).commit();
            }
        });
        // подгуржаем данные пользователя в переписку с которым вошли
        userName=view.findViewById(R.id.username);
        userName.setText(collocutor.getUsername());


        messageRecycler=view.findViewById(R.id.messageView);
        MessageUpdateTask messageUpdateTask = new MessageUpdateTask(); //подгружаем сообщения
        messageUpdateTask.execute();

// по таймеру отправляем запросы на базу что бы проверить не пришло ли новых сообщений
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MessageUpdateTask mstask = new MessageUpdateTask();
                mstask.execute();
            }
        },0,3000);


        messageText= view.findViewById(R.id.messageTextField);
        sendBt=view.findViewById(R.id.sendMessage);
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageText.getText().toString().trim().equals("")) {
                    String api = "https://messapi.space/api/messages/create.php/";
                    JSONObject json = new JSONObject();
                    try {
                        json.put("from_who", current_user.getUsername());
                        json.put("to_who", collocutor.getUsername());
                        json.put("text", messageText.getText().toString());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                            response -> {
                                try {
                                    Log.i("Volley",response.getString("message"));
                                   MessageUpdateTask messageUpdateTask = new MessageUpdateTask();
                                   messageUpdateTask.execute();
                                   messageText.setText("");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            error -> {
                                Log.i("Volley", error.toString());
                                Toast.makeText(getActivity(), "ошибка подключения к базе", Toast.LENGTH_LONG).show();

                            });
                    queue.add(request);
                }
            }
        });
        //обрабатываем нажатие на кнопку назад в интерфейсе
        BackBt= view.findViewById(R.id.backButton);
        BackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                FragmentManager fg=  getActivity().getSupportFragmentManager();
                fg.beginTransaction().remove(fg.findFragmentByTag("3")).show(fg.findFragmentByTag("2")).commit();
            }
        });

        return view;

    }
// обрабатываем нажатие на кнопку "назад" в системе
    @Override
    public void onBackPressed() {
        timer.cancel();
        FragmentManager fg=  getActivity().getSupportFragmentManager();
        fg.beginTransaction().remove(fg.findFragmentByTag("3")).show(fg.findFragmentByTag("2")).commit();
    }

    class MessageUpdateTask extends AsyncTask<Void, ArrayList<message>, Void> { //асинхронная задача для подгрузки данных на страницу
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<message> messages= new ArrayList<>();

            String api = "https://messapi.space/api/messages/read.php/";
            JSONObject json = new JSONObject();
            try {
                json.put("user1",current_user.getUsername());
                json.put("user2",collocutor.getUsername());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                    response -> {
                        try {
                            if (response.getString("count")!=null);
                            int count=response.getInt("count");
                            for(int i =1;i<=count;i++){
                                messages.add(new message(response.getString("message"+i+" from_who"),response.getString("message"+i+" to_who"),response.getString("message"+i+" when_sent"),response.getString("message"+i)));
                                if(count==i) publishProgress(messages);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> {
                        Log.i("Volley", error.toString());
                        Toast.makeText(getActivity(), "ошибка подключения к базе", Toast.LENGTH_LONG).show();

                    });
            queue.add(request);

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<message>... messagesUPD) {
            super.onProgressUpdate(messagesUPD);
            if(!messages_stable.equals(messagesUPD[0])) {
                messageRecycler.setAdapter(new messageAdapter(messagesUPD[0], getActivity().getApplicationContext(), collocutor, current_user));
                messages_stable = messagesUPD[0];
                messageRecycler.scrollToPosition(messages_stable.size() - 1);
                if(getLifecycle().equals(Lifecycle.State.DESTROYED)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"SwirliChat")
                                                    .setSmallIcon(R.mipmap.ic_app_logo).
                                                    setContentTitle("Новое сообщение").
                                                    setContentText("у вас новое сообщение от ")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                            NotificationManagerCompat notificationManager =
                                                    NotificationManagerCompat.from(getActivity());
                                            notificationManager.notify(127,builder.build());
                                            Log.i("not","вот ту должно бы быть уведомление ");
                }
            }
        }

        @Override
        protected void onPostExecute(Void users) {
            super.onPostExecute(users);

        }

    }

}
