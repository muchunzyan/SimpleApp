package com.bifexey.simpleapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Filter extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSubjects = myRef.child("subjects");

    List<String> subjects = new ArrayList<>();
    ArrayAdapter<String> adapter;

    boolean to_me = false, to_student = false, online = false, university = false, cafe = false, library = false, co_working = false;
    String TAG = "lol";

    Spinner f_sub_spinner;
    TextView filter_to_me, filter_to_student, filter_online, filter_university, filter_cafe, filter_library, filter_co_working;
    LinearLayout filter_search_btn;
    EditText filter_price_from, filter_price_to;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        subjects.add("Предмет");

//        f_sub_spinner = findViewById(R.id.f_sub_spinner);
//        filter_to_me = findViewById(R.id.filter_to_me);
//        filter_to_student = findViewById(R.id.filter_to_student);
//        filter_online = findViewById(R.id.filter_online);
//        filter_university = findViewById(R.id.filter_university);
//        filter_cafe = findViewById(R.id.filter_cafe);
//        filter_library = findViewById(R.id.filter_library);
//        filter_co_working = findViewById(R.id.filter_co_working);
//        filter_price_from = findViewById(R.id.filter_price_from);
//        filter_price_to = findViewById(R.id.filter_price_to);
//        filter_search_btn = findViewById(R.id.filter_search_btn);
//        btn_back = findViewById(R.id.btn_back);

        filter_to_me.setOnClickListener(this);
        filter_to_student.setOnClickListener(this);
        filter_online.setOnClickListener(this);
        filter_university.setOnClickListener(this);
        filter_cafe.setOnClickListener(this);
        filter_library.setOnClickListener(this);
        filter_co_working.setOnClickListener(this);
        filter_search_btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        myRefSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        if (dss.getKey().equals("subject_name"))
                            subjects.add(dss.getValue().toString());
                    }

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, subjects);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    f_sub_spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.filter_to_me:
//                if (!to_me) {
//                    filter_to_me.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_to_me.setTextColor(getResources().getColor(R.color.colorWhite));
//                    to_me = true;
//                } else {
//                    filter_to_me.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_to_me.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    to_me = false;
//                }
//                break;
//            case R.id.filter_to_student:
//                if (!to_student) {
//                    filter_to_student.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_to_student.setTextColor(getResources().getColor(R.color.colorWhite));
//                    to_student = true;
//                } else {
//                    filter_to_student.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_to_student.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    to_student = false;
//                }
//                break;
//            case R.id.filter_online:
//                if (!online) {
//                    filter_online.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_online.setTextColor(getResources().getColor(R.color.colorWhite));
//                    online = true;
//                } else {
//                    filter_online.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_online.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    online = false;
//                }
//                break;
//            case R.id.filter_university:
//                if (!university) {
//                    filter_university.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_university.setTextColor(getResources().getColor(R.color.colorWhite));
//                    university = true;
//                } else {
//                    filter_university.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_university.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    university = false;
//                }
//                break;
//            case R.id.filter_cafe:
//                if (!cafe) {
//                    filter_cafe.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_cafe.setTextColor(getResources().getColor(R.color.colorWhite));
//                    cafe = true;
//                } else {
//                    filter_cafe.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_cafe.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    cafe = false;
//                }
//                break;
//            case R.id.filter_library:
//                if (!library) {
//                    filter_library.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_library.setTextColor(getResources().getColor(R.color.colorWhite));
//                    library = true;
//                } else {
//                    filter_library.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_library.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    library = false;
//                }
//                break;
//            case R.id.filter_co_working:
//                if (!co_working) {
//                    filter_co_working.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    filter_co_working.setTextColor(getResources().getColor(R.color.colorWhite));
//                    co_working = true;
//                } else {
//                    filter_co_working.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    filter_co_working.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    co_working = false;
//                }
//                break;
//            case R.id.filter_search_btn:
//                if ((to_me || to_student || online || university || cafe || library || co_working) && !f_sub_spinner.getSelectedItem().toString().equals("Предмет") && !filter_price_from.getText().toString().equals("") && !filter_price_to.getText().toString().equals("")){
//                    if (Integer.parseInt(filter_price_from.getText().toString()) < Integer.parseInt(filter_price_to.getText().toString())) {
//                        Intent intent = new Intent(Filter.this, MainPage.class);
//                        intent.putExtra("subject", f_sub_spinner.getSelectedItem().toString());
//                        intent.putExtra("to_me", to_me);
//                        intent.putExtra("to_student", to_student);
//                        intent.putExtra("online", online);
//                        intent.putExtra("university", university);
//                        intent.putExtra("cafe", cafe);
//                        intent.putExtra("library", library);
//                        intent.putExtra("co_working", co_working);
//                        intent.putExtra("price_from", filter_price_from.getText().toString());
//                        intent.putExtra("price_to", filter_price_to.getText().toString());
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(Filter.this, "Границы цены введены некорректно", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(Filter.this, "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.btn_back:
//                finish();
//                break;
//        }
    }
}

