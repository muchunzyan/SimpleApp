package com.bifexey.simpleapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.Chat;
import com.bifexey.simpleapp.ChatForm;
import com.bifexey.simpleapp.ChatFormArrayAdapter;
import com.bifexey.simpleapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefChats = myRef.child("chats");
    DatabaseReference myRefUsers = myRef.child("users");

    ChatFormArrayAdapter chatFormArrayAdapter;

    List<String> check_phone_number = new ArrayList<>();
    List<String> need_phone_numbers;
    ArrayList<ChatForm> chatForms;
    String talker_phone_number;
    boolean first_time = true;

    ListView chats_list_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, null);

        chats_list_view = v.findViewById(R.id.chats_list_view);

        myRefChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                need_phone_numbers = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    check_phone_number = Arrays.asList(ds.getKey().split("-"));

                    if (check_phone_number.get(0).equals(user.getPhoneNumber())){
                        talker_phone_number = check_phone_number.get(1);
                    }
                    else if (check_phone_number.get(1).equals(user.getPhoneNumber())){
                        talker_phone_number = check_phone_number.get(0);
                    }

                    need_phone_numbers.add(talker_phone_number);
                }

                myRefUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatForms = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            for (String number : need_phone_numbers){
                                if (ds.getKey().equals(number)){
                                    String first_name = "";
                                    String second_name = "";
                                    String activity = "";
                                    for (DataSnapshot dss : ds.getChildren()){
                                        if (dss.getKey().equals("first_name"))
                                            first_name = dss.getValue().toString();
                                        if (dss.getKey().equals("second_name"))
                                            second_name = dss.getValue().toString();
                                        if (dss.getKey().equals("activity"))
                                            activity = dss.getValue().toString();
                                    }

                                    String name = first_name + " " + second_name;
                                    chatForms.add(new ChatForm(name, number, activity));
                                }
                            }
                        }

                        if (first_time) {
                            chatFormArrayAdapter = new ChatFormArrayAdapter(requireActivity(), R.layout.chat_form, chatForms);
                            chats_list_view.setAdapter(chatFormArrayAdapter);
                            first_time = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chats_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Chat.class);
                RelativeLayout relativeLayout = (RelativeLayout) view;
                String o_user_phone_number = "Null";

                for(int i = 0; i < relativeLayout.getChildCount(); i++) {
                    View child = relativeLayout.getChildAt(i);
                    if (child instanceof TextView) {
                        o_user_phone_number = ((TextView) child).getText().toString();
                    }
                }

                intent.putExtra("o_user_phone_number", o_user_phone_number);
                startActivity(intent);
            }
        });

        return v;
    }
}
