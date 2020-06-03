package com.bifexey.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ads extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference AllTeacherAds = myRef.child("AllTeacherAds");
    DatabaseReference AllTeacherAdsCurrentSubject;
    StorageReference mStorageRef;

    FirebaseListAdapter adapter;

    ArrayList<String> phone_numbers = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> subjects = new ArrayList<>();

    String sub_name;

    ImageView btn_back;
    TextView subject_text;
    ListView list_teacher_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        sub_name = getIntent().getStringExtra("sub_name");

        AllTeacherAdsCurrentSubject = AllTeacherAds.child(sub_name);

        btn_back = findViewById(R.id.btn_back);
        subject_text = findViewById(R.id.subject_text);
        //list_teacher_subjects = findViewById(R.id.list_teacher_subjects);

        subject_text.setText(sub_name);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        FirebaseListOptions<SubjectFormItem> options = new FirebaseListOptions.Builder<SubjectFormItem>()
//                .setQuery(AllTeacherAdsCurrentSubject, SubjectFormItem.class)
//                .setLayout(R.layout.current_subject_teacher_ads)
//                .build();
//
//        adapter = new FirebaseListAdapter<SubjectFormItem>(options){
//            @Override
//            protected void populateView(View v, SubjectFormItem model, int position) {
//                ImageView teacher_image = v.findViewById(R.id.teacher_image);
//                TextView teacher_name = v.findViewById(R.id.teacher_name);
//                TextView teacher_pod_subject = v.findViewById(R.id.teacher_pod_subject);
//                TextView teacher_comment = v.findViewById(R.id.teacher_comment);
//                TextView teacher_price = v.findViewById(R.id.teacher_price);
//
//                downloadImage(teacher_image, model.getPhone_number());
//                teacher_name.setText(model.getName());
//                teacher_pod_subject.setText(model.getSpecial_subject());
//                teacher_comment.setText(model.getComment());
//                teacher_price.setText("\u20BD" + model.getPrice());
//
//                phone_numbers.add(model.getPhone_number());
//                keys.add(model.getKey());
//                subjects.add(model.getSubject());
//            }
//        };
//
//        adapter.startListening();
//
//        list_teacher_subjects.setAdapter(adapter);
//
//        list_teacher_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ads.this, WatchTeacherSubject.class);
//                intent.putExtra("teacher_phone_number", phone_numbers.get(position));
//                intent.putExtra("teacher_ad_key", keys.get(position));
//                intent.putExtra("teacher_ad_sub", subjects.get(position));
//                startActivity(intent);
//            }
//        });
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
