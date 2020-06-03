package com.bifexey.simpleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlaggedTeachers extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserFlaggedTeachers = myRefCurrentUser.child("flagged_teachers");
    StorageReference mStorageRef;

    FirebaseListAdapter adapter;

    ArrayList<String> phone_numbers = new ArrayList<>();

    ListView list_flagged_teachers;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_teachers);

        list_flagged_teachers = findViewById(R.id.list_flagged_teachers);
        btn_back = findViewById(R.id.btn_back);

        FirebaseListOptions<FlaggedTeacherForm> options = new FirebaseListOptions.Builder<FlaggedTeacherForm>()
                .setQuery(myRefCurrentUserFlaggedTeachers, FlaggedTeacherForm.class)
                .setLayout(R.layout.flagged_teachers_item)
                .build();

        adapter = new FirebaseListAdapter<FlaggedTeacherForm>(options){
            @Override
            protected void populateView(View v, FlaggedTeacherForm model, int position) {
                ImageView teacher_image = v.findViewById(R.id.teacher_image);
                TextView teacher_name = v.findViewById(R.id.teacher_name);
                TextView teacher_phone_number = v.findViewById(R.id.teacher_phone_number);

                downloadImage(teacher_image, model.getTeacher_phone_number());
                teacher_name.setText(model.teacher_name);

                String ph_n = model.teacher_phone_number;
                phone_numbers.add(ph_n);
                teacher_phone_number.setText(ph_n);
            }
        };

        adapter.startListening();

        list_flagged_teachers.setAdapter(adapter);

        list_flagged_teachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlaggedTeachers.this, WatchTeacherProfile.class);
                intent.putExtra("teacher_ph", phone_numbers.get(position));
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}
