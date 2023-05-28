package com.example.massenger;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DialogeFragment extends Fragment {
    EditText messageText;
    ScrollView messageScroll;
    FloatingActionButton sendBt,BackBt;
    ImageView userAva;
    TextView userName;
    RecyclerView messageRecycler;
User collocutor ;
UserSerial current_user;
ArrayList<message> messages_stable;
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

        userName=view.findViewById(R.id.username);
        userName.setText(collocutor.getUsername());



        messageRecycler=view.findViewById(R.id.messageView);
        MessageUpdateTask messageUpdateTask = new MessageUpdateTask();
        messageUpdateTask.execute();
        messageScroll=view.findViewById(R.id.messageScroll);
        messageScroll.post(new Runnable() {
            @Override
            public void run() {
                messageScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        Timer timer = new Timer();


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
        BackBt= view.findViewById(R.id.backButton);
        BackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                FragmentManager fg=  getActivity().getSupportFragmentManager();
                fg.beginTransaction().remove(fg.findFragmentByTag("3")).show(fg.findFragmentByTag("2")).commit();
            }
        });
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MessageUpdateTask mstask = new MessageUpdateTask();
                mstask.execute();
            }
        },0,3000);
        return view;

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
            }
        }

        @Override
        protected void onPostExecute(Void users) {
            super.onPostExecute(users);

        }
    }

}
