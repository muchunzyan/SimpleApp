package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSubjectForm extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefSubjects = myRef.child("subjects");
    DatabaseReference myRefPodSubjects;
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserTeacherInfo = myRefCurrentUser.child("teacher_info");
    DatabaseReference AllTeacherAds = myRef.child("AllTeacherAds");
    DatabaseReference AllTeacherAdsPodSubject;

    ArrayAdapter<String> adapter, adapterPod;

    List<String> subjects_t = new ArrayList<>();
    List<String> pod_subjects_t = new ArrayList<>();

    String TAG = "lol";
    String selectedItem = "";
    String first_name = "", second_name = "";
    int came_from_register;

    Spinner subjects_spinner_r, pod_subjects_spinner_r;
    EditText edit_text_hour_cost_r, edit_text_comment_r;
    LinearLayout btn_ok;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject_form);

        subjects_t.add("Предмет");
        pod_subjects_t.add("Раздел");

        came_from_register = getIntent().getIntExtra("from_reg", 0);

        subjects_spinner_r = findViewById(R.id.subjects_spinner_r);
        pod_subjects_spinner_r = findViewById(R.id.pod_subjects_spinner_r);
        edit_text_hour_cost_r = findViewById(R.id.edit_text_hour_cost_r);
        edit_text_comment_r = findViewById(R.id.edit_text_comment_r);
        btn_ok = findViewById(R.id.btn_ok);
        btn_back = findViewById(R.id.btn_back);

        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("first_name"))
                        first_name = ds.getValue().toString();
                    if (ds.getKey().equals("second_name"))
                        second_name = ds.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        if (dss.getKey().equals("subject_name"))
                            subjects_t.add(dss.getValue().toString());
                    }
                }

                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, subjects_t);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjects_spinner_r.setAdapter(adapter);

                String subject = getIntent().getStringExtra("subject");
                if (subject != null){
                    int spinnerPosition = adapter.getPosition(subject);
                    subjects_spinner_r.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        RecheckSubName();

        subjects_spinner_r.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = subjects_spinner_r.getSelectedItem().toString();
                RecheckSubName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void RecheckSubName(){
        if (selectedItem.equals("Бух. учёт"))
            myRefPodSubjects = myRefSubjects.child("Бух учёт").child("pod_subject");
        else
            myRefPodSubjects = myRefSubjects.child(selectedItem).child("pod_subject");

        myRefPodSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pod_subjects_t.clear();
                if (selectedItem.equals("Русский как иностранный"))
                    pod_subjects_t.add("Язык обучения");
                else
                    pod_subjects_t.add("Раздел");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    pod_subjects_t.add(ds.getValue().toString());
                }
                adapterPod = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, pod_subjects_t);
                adapterPod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pod_subjects_spinner_r.setAdapter(adapterPod);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void remakeIntentInfoAndStartTRegister(){
        Intent intent = new Intent(getApplicationContext(), TeacherRegistration.class);
        intent.putExtra("to_me", getIntent().getStringExtra("to_me"));
        intent.putExtra("metro_station", getIntent().getStringExtra("metro_station"));
        intent.putExtra("to_student", getIntent().getStringExtra("to_student"));
        intent.putExtra("online", getIntent().getStringExtra("online"));
        intent.putExtra("other", getIntent().getStringExtra("other"));
        intent.putExtra("about_teacher", getIntent().getStringExtra("about_teacher"));
        startActivity(intent);
        finish();
    }

    private void ChooseTreeSubject(String subject){
        switch (subject){
            case "Английский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Английский язык");
                break;
            case "Биология":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Биология");
                break;
            case "География":
                AllTeacherAdsPodSubject = AllTeacherAds.child("География");
                break;
            case "Информатика и программирование":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Информатика и программирование");
                break;
            case "Испанский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Испанский язык");
                break;
            case "История":
                AllTeacherAdsPodSubject = AllTeacherAds.child("История");
                break;
            case "Итальянский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Итальянский язык");
                break;
            case "Китайский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Китайский язык");
                break;
            case "Литература":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Литература");
                break;
            case "Математика":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Математика");
                break;
            case "Немецкий язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Немецкий язык");
                break;
            case "Обществознание":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Обществознание");
                break;
            case "Право":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Право");
                break;
            case "Русский как иностранный":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Русский как иностранный");
                break;
            case "Физика":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Физика");
                break;
            case "Французский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Французский язык");
                break;
            case "Химия":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Химия");
                break;
            case "Экономика":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Экономика");
                break;
            case "Японский язык":
                AllTeacherAdsPodSubject = AllTeacherAds.child("Японский язык");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                if (edit_text_hour_cost_r.getText().toString().equals("") || edit_text_comment_r.getText().toString().trim().equals("") ||
                        subjects_spinner_r.getSelectedItem().toString().equals("Предмет") || pod_subjects_spinner_r.getSelectedItem().toString().equals("Раздел") || pod_subjects_spinner_r.getSelectedItem().toString().equals("Язык обучения")){
                    Toast.makeText(getApplicationContext(), "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
                }
                else {
                    String hourCost = Integer.toString(Integer.parseInt(edit_text_hour_cost_r.getText().toString()));
                    String commentReg = edit_text_comment_r.getText().toString().trim();

                    DatabaseReference myRefCurrentUserTeacherSubjectsPush = myRefCurrentUserTeacherInfo.child("subjects").push();

                    myRefCurrentUserTeacherSubjectsPush.setValue(new SubjectFormItem(
                            first_name + " " + second_name,
                            subjects_spinner_r.getSelectedItem().toString(),
                            pod_subjects_spinner_r.getSelectedItem().toString(),
                            hourCost, commentReg, myRefCurrentUserTeacherSubjectsPush.getKey(),
                            user.getPhoneNumber()));

                    ChooseTreeSubject(subjects_spinner_r.getSelectedItem().toString());

                    AllTeacherAdsPodSubject.child(myRefCurrentUserTeacherSubjectsPush.getKey()).setValue(new SubjectFormItem(
                            first_name + " " + second_name,
                            subjects_spinner_r.getSelectedItem().toString(),
                            pod_subjects_spinner_r.getSelectedItem().toString(),
                            hourCost, commentReg, myRefCurrentUserTeacherSubjectsPush.getKey(),
                            user.getPhoneNumber()));

                    if (came_from_register != 0)
                        remakeIntentInfoAndStartTRegister();
                    else {
                        finish();
                    }
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}

