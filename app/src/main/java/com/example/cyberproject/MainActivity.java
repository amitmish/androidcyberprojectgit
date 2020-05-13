package com.example.cyberproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button login_button = findViewById(R.id.login);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText email = findViewById(R.id.email);
                final EditText password = findViewById(R.id.password);
                OkHttpClient client = new OkHttpClient();

                String url = "http://192.168.1.29:12345/android/login";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("email", email.getText().toString())
                        .addFormDataPart("password", password.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (myResponse == "false" || myResponse.equals("false")) {
                                        login_button.setText("TRY AGAIN");
                                    }
                                    else {
                                        Room(email.getText().toString(), password.getText().toString());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void Room(final String email, final String password) {
        setContentView(R.layout.activity_room);
        final Button join_button = findViewById(R.id.submit);
        join_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText game_room_edit_text = findViewById(R.id.game_room);
                if (game_room_edit_text.getText().toString() == "" || game_room_edit_text.getText().toString().equals("")) {
                    join_button.setText("TRY AGAIN");
                }
                else {
                    final int game_room = Integer.parseInt(game_room_edit_text.getText().toString());
                    OkHttpClient client = new OkHttpClient();

                    String url = "http://192.168.1.29:12345/android/join";

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", email)
                            .addFormDataPart("password", password)
                            .addFormDataPart("game_room", String.valueOf(game_room))
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                final String myResponse = response.body().string();

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (myResponse == "false" || myResponse.equals("false")) {
                                            join_button.setText("TRY AGAIN");
                                        }
                                        else {
                                            Wait(email, password, game_room);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
    public void Wait(final String email, final String password, final int game_room) {
        setContentView(R.layout.activity_waiting);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mPostReference = mDatabase.child("rooms").child(String.valueOf(game_room)).child("started");
        ValueEventListener postListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("1")) {
                    Game(email, password, game_room);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    public void Game(final String email, final String password, final int game_room) {
        setContentView(R.layout.activity_game);
        final Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText ans1_edit_text = findViewById(R.id.ans5);
                String ans1_string = ans1_edit_text.getText().toString();
                final EditText ans2_edit_text = findViewById(R.id.ans2);
                String ans2_string = ans2_edit_text.getText().toString();
                final EditText ans3_edit_text = findViewById(R.id.ans2);
                String ans3_string = ans3_edit_text.getText().toString();
                final EditText ans4_edit_text = findViewById(R.id.ans2);
                String ans4_string = ans4_edit_text.getText().toString();
                final EditText ans5_edit_text = findViewById(R.id.ans5);
                String ans5_string = ans5_edit_text.getText().toString();
                if (ans1_string.equals("")) {
                    ans1_string = "0";
                }
                if (ans2_string.equals("")) {
                    ans2_string = "0";
                }
                if (ans3_string.equals("")) {
                    ans3_string = "0";
                }
                if (ans4_string.equals("")) {
                    ans4_string = "0";
                }
                if (ans5_string.equals("")) {
                    ans5_string = "0";
                }
                final int ans1 = Integer.parseInt(ans1_string);
                final int ans2 = Integer.parseInt(ans2_string);
                final int ans3 = Integer.parseInt(ans3_string);
                final int ans4 = Integer.parseInt(ans4_string);
                final int ans5 = Integer.parseInt(ans5_string);
                OkHttpClient client = new OkHttpClient();

                String url = "http://192.168.1.29:12345/android/game";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("game_room", String.valueOf(game_room))
                        .addFormDataPart("email", email)
                        .addFormDataPart("password", password)
                        .addFormDataPart("ans1", String.valueOf(ans1))
                        .addFormDataPart("ans2", String.valueOf(ans2))
                        .addFormDataPart("ans3", String.valueOf(ans3))
                        .addFormDataPart("ans4", String.valueOf(ans4))
                        .addFormDataPart("ans5", String.valueOf(ans5))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Room(email, password);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
