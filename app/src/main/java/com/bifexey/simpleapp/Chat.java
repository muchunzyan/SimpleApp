package com.bifexey.simpleapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefChats = myRef.child("chats");
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference  myRefCurrentChat, myRefTalker;

    FirebaseListAdapter adapter;

    String o_user_phone_number_extra, first_name;
    String talker_first_name, talker_second_name;

    List<String> users_phone_numbers = new ArrayList<>();

    ListView listOfMessages;
    EditText edit_text;
    ImageView btn_send, btn_scroll_down;
    LinearLayout go_back_lay;
    TextView name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        o_user_phone_number_extra = getIntent().getStringExtra("o_user_phone_number");
        myRefTalker = myRefUsers.child(o_user_phone_number_extra);

        users_phone_numbers.add(o_user_phone_number_extra);
        users_phone_numbers.add(user.getPhoneNumber());
        Collections.sort(users_phone_numbers);

        edit_text = findViewById(R.id.edit_text);
        btn_send = findViewById(R.id.btn_send);
        go_back_lay = findViewById(R.id.go_back_lay);
        name_text = findViewById(R.id.name_text);
        btn_scroll_down = findViewById(R.id.btn_scroll_down);

        btn_send.setOnClickListener(this);
        go_back_lay.setOnClickListener(this);
        btn_scroll_down.setOnClickListener(this);

        myRefCurrentChat = myRefChats.child(users_phone_numbers.get(0) + "-" + users_phone_numbers.get(1));

        myRefTalker.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("first_name")){
                        talker_first_name = ds.getValue().toString();
                    }
                    if (ds.getKey().equals("second_name")){
                        talker_second_name = ds.getValue().toString();
                    }
                }
                name_text.setText(talker_first_name + " " + talker_second_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                first_name = dataSnapshot.child("first_name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayChatMessages();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                String messageText = edit_text.getText().toString();

                if (messageText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Write something", Toast.LENGTH_SHORT).show();
                }
                else if (messageText.length() > 300) {
                    Toast.makeText(getApplicationContext(), "This message is too big", Toast.LENGTH_SHORT).show();
                } else {
                    myRefCurrentChat.push().setValue(new ChatMessage(first_name, messageText, user.getPhoneNumber()));
                }

                edit_text.setText("");
                break;
            case R.id.go_back_lay:
                finish();
                break;
            case R.id.btn_scroll_down:
                listOfMessages.smoothScrollToPosition(adapter.getCount());
                break;
        }
    }

    private void displayChatMessages(){
        listOfMessages = findViewById(R.id.list_of_messages);

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(myRefCurrentChat, ChatMessage.class)
                .setLayout(R.layout.message_item)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messagePhoneNumber = v.findViewById(R.id.message_phone_number);
                TextView messageTime = v.findViewById(R.id.message_time);

                if (model.getMessagePhoneNumber().equals(user.getPhoneNumber()))
                    messagePhoneNumber.setText("Вы");
                else
                    messagePhoneNumber.setText(model.getFirst_name());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                messageText.setText(model.getMessageText());
            }
        };

        listOfMessages.setAdapter(adapter);

        myRefCurrentChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfMessages.smoothScrollToPosition(adapter.getCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
