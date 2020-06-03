package com.bifexey.simpleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchTeacherSubject extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefAllTAds = myRef.child("AllTeacherAds");
    DatabaseReference myRefFlaggedTeachers = myRef.child("users").child(user.getPhoneNumber()).child("flagged_teachers");
    DatabaseReference myRefNeedTAdd, myRefUserTeacher;
    StorageReference mStorageRef;

    ArrayList<String> where = new ArrayList<>();

    String teacher_phone_number, teacher_ad_key, teacher_ad_sub;
    String metro_station, other;
    String o_user_phone_number, teacher_name;
    boolean to_me = false, online = false, to_student = false;
    boolean in_favorite = false;

    ImageView W_t_sub_image, W_t_sub_img_flag, btn_back;
    TextView W_t_sub_name, W_t_sub_subject, W_t_sub_pod_subject, W_t_sub_comment, W_t_sub_comment_teacher, W_t_sub_price;
    Button W_t_sub_btn_chat;
    TextView text_where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_teacher_subject);

        teacher_phone_number = getIntent().getStringExtra("teacher_phone_number");
        teacher_ad_key = getIntent().getStringExtra("teacher_ad_key");
        teacher_ad_sub = getIntent().getStringExtra("teacher_ad_sub");
        myRefNeedTAdd = myRefAllTAds.child(teacher_ad_sub).child(teacher_ad_key);
        myRefUserTeacher = myRefUsers.child(teacher_phone_number);

        W_t_sub_image = findViewById(R.id.W_t_sub_image);
        W_t_sub_img_flag = findViewById(R.id.W_t_sub_img_flag);
        W_t_sub_name = findViewById(R.id.W_t_sub_name);
        W_t_sub_subject = findViewById(R.id.W_t_sub_subject);
        W_t_sub_pod_subject = findViewById(R.id.W_t_sub_pod_subject);
        W_t_sub_comment = findViewById(R.id.W_t_sub_comment);
        W_t_sub_comment_teacher = findViewById(R.id.W_t_sub_comment_teacher);
        W_t_sub_price = findViewById(R.id.W_t_sub_price);
        W_t_sub_btn_chat = findViewById(R.id.W_t_sub_btn_chat);
        text_where = findViewById(R.id.text_where);
        btn_back = findViewById(R.id.btn_back);

        W_t_sub_img_flag.setOnClickListener(this);
        W_t_sub_btn_chat.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        myRefNeedTAdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                o_user_phone_number = dataSnapshot.child("phone_number").getValue().toString();
                downloadImage(W_t_sub_image, dataSnapshot.child("phone_number").getValue().toString());
                teacher_name = dataSnapshot.child("name").getValue().toString();
                W_t_sub_name.setText(teacher_name);
                W_t_sub_subject.setText(dataSnapshot.child("subject").getValue().toString());
                W_t_sub_pod_subject.setText(dataSnapshot.child("special_subject").getValue().toString());
                W_t_sub_comment.setText(dataSnapshot.child("comment").getValue().toString());
                W_t_sub_price.setText("\u20BD" + dataSnapshot.child("price").getValue().toString());

                checkFlaggedTeachers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefUserTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                W_t_sub_comment_teacher.setText(dataSnapshot.child("about_teacher").getValue().toString());
                for (DataSnapshot ds : dataSnapshot.child("teacher_info").child("where").getChildren()){
                    String q = ds.getKey();
                    String k = ds.getValue().toString();
                    switch (q){
                        case "metro_station":
                            metro_station = k;
                            where.add("У меня (" + metro_station + ")");
                            break;
                        case "to_me":
                            to_me = true;
                            break;
                        case "online":
                            online = true;
                            where.add("Онлайн");
                            break;
                        case "to_student":
                            to_student = true;
                            where.add("У студента");
                            break;
                        case "other":
                            other = k;
                            where.add("Другое место (" + other + ")");
                            break;
                    }
                }

                for(String i : where){
                    text_where.setText(text_where.getText() + "#" + i + "\n");
                }
                text_where.setText(text_where.getText().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (teacher_phone_number.equals(user.getPhoneNumber())) {
            W_t_sub_btn_chat.setVisibility(View.GONE);
            W_t_sub_img_flag.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.W_t_sub_img_flag:
                if(!in_favorite){
                    myRefFlaggedTeachers.child(o_user_phone_number).setValue(new FlaggedTeacherForm(o_user_phone_number, teacher_name));
                    Toast.makeText(WatchTeacherSubject.this, "Учитель добавлен в избранное", Toast.LENGTH_SHORT).show();
                    checkFlaggedTeachers();
                } else {
                    myRefFlaggedTeachers.child(o_user_phone_number).removeValue();
                    Toast.makeText(WatchTeacherSubject.this, "Учитель удалён из избранного", Toast.LENGTH_SHORT).show();
                    W_t_sub_img_flag.setImageDrawable(getResources().getDrawable(R.drawable.star2));
                    in_favorite = false;
                    checkFlaggedTeachers();
                }
                break;
            case R.id.W_t_sub_btn_chat:
                Intent intent = new Intent(WatchTeacherSubject.this, Chat.class);
                intent.putExtra("o_user_phone_number", o_user_phone_number);
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void downloadImage(final ImageView img, String phone_number) {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(phone_number, 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img);
            }
        });
    }

    private void checkFlaggedTeachers(){
        myRefFlaggedTeachers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().equals(o_user_phone_number)){
                        W_t_sub_img_flag.setImageDrawable(getResources().getDrawable(R.drawable.star_colored));
                        in_favorite = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

