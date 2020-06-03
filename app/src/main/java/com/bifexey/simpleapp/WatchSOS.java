package com.bifexey.simpleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class WatchSOS extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSOSAds = myRef.child("AllSOSAds");
    DatabaseReference myRefCurrentUserCurrentSOSAd;

    StorageReference mStorageRef;

    String key;
    String first_name, second_name;
    String year, month, day, hour, minute;
    String o_user_phone_number;

    ImageView W_SOS_image, btn_back;
    TextView W_SOS_name, W_SOS_location, W_SOS_comment, W_SOS_price;
    TextView W_SOS_subject, W_SOS_subjectName, W_SOS_subjectTopic, W_SOS_date_time;
    Button W_SOS_btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_sos);

        key = getIntent().getStringExtra("key");

        W_SOS_image = findViewById(R.id.W_SOS_image);
        W_SOS_name = findViewById(R.id.W_SOS_name);
        W_SOS_location = findViewById(R.id.W_SOS_location);
        W_SOS_subject = findViewById(R.id.W_SOS_subject);
        W_SOS_subjectName = findViewById(R.id.W_SOS_subjectName);
        W_SOS_subjectTopic = findViewById(R.id.W_SOS_subjectTopic);
        W_SOS_date_time = findViewById(R.id.W_SOS_date_time);
        W_SOS_comment = findViewById(R.id.W_SOS_comment);
        W_SOS_price = findViewById(R.id.W_SOS_price);
        W_SOS_btn_chat = findViewById(R.id.W_SOS_btn_chat);
        btn_back = findViewById(R.id.btn_back);

        W_SOS_btn_chat.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        setInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.W_SOS_btn_chat:
                Intent intent = new Intent(WatchSOS.this, Chat.class);
                intent.putExtra("o_user_phone_number", o_user_phone_number);
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void setInfo(){
        myRefCurrentUserCurrentSOSAd = myRefSOSAds.child(key);

        myRefCurrentUserCurrentSOSAd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    switch (ds.getKey()) {
                        case "sos_phone_number":
                            o_user_phone_number = ds.getValue().toString();
                            downloadImage(W_SOS_image, o_user_phone_number);
                            break;
                        case "sos_first_name":
                            first_name = ds.getValue().toString();
                            break;
                        case "sos_second_name":
                            second_name = ds.getValue().toString();
                            break;
                        case "sos_location":
                            W_SOS_location.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectFromSpinner":
                            W_SOS_subject.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectName":
                            W_SOS_subjectName.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectTopic":
                            W_SOS_subjectTopic.setText(ds.getValue().toString());
                            break;
                        case "sos_year":
                            year = ds.getValue().toString();
                            break;
                        case "sos_month":
                            month = ds.getValue().toString();
                            break;
                        case "sos_day":
                            day = ds.getValue().toString();
                            break;
                        case "sos_hour":
                            hour = ds.getValue().toString();
                            break;
                        case "sos_minute":
                            minute = ds.getValue().toString();
                            break;
                        case "sos_comment":
                            if (!ds.getValue().toString().equals(""))
                                W_SOS_comment.setText(ds.getValue().toString());
                            else
                                W_SOS_comment.setText("Без комментария");
                            break;
                        case "sos_price":
                            W_SOS_price.setText("₽" + ds.getValue().toString());
                            break;
                    }
                }

                W_SOS_name.setText(first_name + " " + second_name);
                W_SOS_date_time.setText(day + "/" + month + "/" + year + " " + hour + ":" + minute);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void downloadImage(final ImageView img, String phoneNumber) {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(phoneNumber, 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img);
            }
        });
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}

