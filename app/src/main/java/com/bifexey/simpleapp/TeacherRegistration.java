package com.bifexey.simpleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherRegistration extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserTeacherInfo = myRefCurrentUser.child("teacher_info");
    DatabaseReference myRefCurrentUserTeacherInfoWhere = myRefCurrentUserTeacherInfo.child("where");
    DatabaseReference myRefCurrentUserTeacherSubjects = myRefCurrentUserTeacherInfo.child("subjects");

    FirebaseListAdapter adapter;

    String metro_station, other;
    boolean to_me = false, to_student = false, online = false;

    TextView btn_to_student, btn_online, to_me_text, other_text;
    Spinner subjects_spinner_r;
    EditText edit_text_hour_cost_r, edit_text_comment_r, edit_about_teacher;
    LinearLayout btn_add_subject, btn_register_teacher;
    ListView list_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);

//        subjects_spinner_r = findViewById(R.id.subjects_spinner_r);
//        edit_text_hour_cost_r = findViewById(R.id.edit_text_hour_cost_r);
//        edit_text_comment_r = findViewById(R.id.edit_text_comment_r);
        edit_about_teacher = findViewById(R.id.edit_about_teacher);
        btn_to_student = findViewById(R.id.btn_to_student);
        btn_online = findViewById(R.id.btn_online);
        btn_register_teacher = findViewById(R.id.btn_register_teacher);
        btn_add_subject = findViewById(R.id.btn_add_subject);
        list_subjects = findViewById(R.id.list_subjects);
        to_me_text = findViewById(R.id.to_me_text);
        other_text = findViewById(R.id.other_text);

        btn_add_subject.setOnClickListener(this);
        to_me_text.setOnClickListener(this);
        btn_to_student.setOnClickListener(this);
        btn_online.setOnClickListener(this);
        other_text.setOnClickListener(this);
        btn_register_teacher.setOnClickListener(this);

        displayTeacherSubjects();
        getIntentInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_add_subject:
//                putIntentInfoNewSubject();
//                break;
//            case R.id.to_me_text:
//                if (!to_me)
//                    putIntentInfoToMe();
//                else{
//                    to_me_text.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    to_me_text.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    to_me_text.setText("У себя");
//                    to_me = false;
//                    metro_station = null;
//                }
//                break;
//            case R.id.btn_to_student:
//                if (!to_student) {
//                    btn_to_student.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    btn_to_student.setTextColor(getResources().getColor(R.color.colorWhite));
//                    to_student = true;
//                }
//                else {
//                    btn_to_student.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    btn_to_student.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    to_student = false;
//                }
//                break;
//            case R.id.btn_online:
//                if (!online) {
//                    btn_online.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
//                    btn_online.setTextColor(getResources().getColor(R.color.colorWhite));
//                    online = true;
//                }
//                else {
//                    btn_online.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    btn_online.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    online = false;
//                }
//                break;
//            case R.id.other_text:
//                if (other == null)
//                    putIntentInfoOther();
//                else{
//                    other_text.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_white));
//                    other_text.setTextColor(getResources().getColor(R.color.colorBlueBlack));
//                    other_text.setText("Другое");
//                    other = null;
//                }
//                break;
//            case R.id.btn_register_teacher:
//                if (list_subjects.getCount() == 0 || (!to_me && !to_student && !online && other == null) || edit_about_teacher.getText().toString().trim().equals("")){
//                    Toast.makeText(getApplicationContext(), "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    myRefCurrentUser.child("activity").setValue("teacher");
//
//                    if(to_me) {
//                        myRefCurrentUserTeacherInfoWhere.child("to_me").setValue(to_me);
//                        myRefCurrentUserTeacherInfoWhere.child("metro_station").setValue(metro_station);
//                    }
//                    if(to_student)
//                        myRefCurrentUserTeacherInfoWhere.child("to_student").setValue(to_student);
//                    if(online)
//                        myRefCurrentUserTeacherInfoWhere.child("online").setValue(online);
//                    if(other != null)
//                        myRefCurrentUserTeacherInfoWhere.child("other").setValue(other);
//
//                    if(!edit_about_teacher.getText().toString().equals(""))
//                        myRefCurrentUser.child("about_teacher").setValue(edit_about_teacher.getText().toString().trim());
//
//                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
//                    startActivity(intent);
//                    finish();
//                }
//                break;
        }
    }

    private void putIntentInfoToMe(){
//        Intent intent = new Intent(getApplicationContext(), ChooseMetro.class);
//        intent.putExtra("from_reg", 1);
//        if (to_student)
//            intent.putExtra("to_student", "yes");
//        if (!to_student)
//            intent.putExtra("to_student", "no");
//        if (online)
//            intent.putExtra("online", "yes");
//        if (!online)
//            intent.putExtra("online", "no");
//        if (other != null)
//            intent.putExtra("other", other);
//        if (!edit_about_teacher.getText().toString().equals(""))
//            intent.putExtra("about_teacher", edit_about_teacher.getText().toString());
//        startActivity(intent);
    }

    private void putIntentInfoNewSubject(){
//        Intent intent = new Intent(getApplicationContext(), AddSubjectForm.class);
//        if (to_me) {
//            intent.putExtra("to_me", "yes");
//            intent.putExtra("metro_station", metro_station);
//        }
//        if (!to_me)
//            intent.putExtra("to_me", "no");
//        if (to_student)
//            intent.putExtra("to_student", "yes");
//        if (!to_student)
//            intent.putExtra("to_student", "no");
//        if (online)
//            intent.putExtra("online", "yes");
//        if (!online)
//            intent.putExtra("online", "no");
//        if (other != null)
//            intent.putExtra("other", other);
//        if (!edit_about_teacher.getText().toString().equals(""))
//            intent.putExtra("about_teacher", edit_about_teacher.getText().toString());
//        startActivity(intent);
    }

    private void putIntentInfoOther(){
//        Intent intent = new Intent(getApplicationContext(), FillOther.class);
//        if (to_me) {
//            intent.putExtra("to_me", "yes");
//            intent.putExtra("metro_station", metro_station);
//        }
//        if (!to_me)
//            intent.putExtra("to_me", "no");
//        if (to_student)
//            intent.putExtra("to_student", "yes");
//        if (!to_student)
//            intent.putExtra("to_student", "no");
//        if (online)
//            intent.putExtra("online", "yes");
//        if (!online)
//            intent.putExtra("online", "no");
//        if (!edit_about_teacher.getText().toString().equals(""))
//            intent.putExtra("about_teacher", edit_about_teacher.getText().toString());
//        startActivity(intent);
    }

    private void getIntentInfo(){
        metro_station = getIntent().getStringExtra("metro_station");
        if (metro_station != null) {
            to_me_text.setText(to_me_text.getText().toString() + "\n" + metro_station);
            to_me_text.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
            to_me_text.setTextColor(getResources().getColor(R.color.colorWhite));
            to_me = true;
        }
        if (getIntent().getStringExtra("to_student") != null && getIntent().getStringExtra("to_student").equals("yes")){
            btn_to_student.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
            btn_to_student.setTextColor(getResources().getColor(R.color.colorWhite));
            to_student = true;
        }
        if (getIntent().getStringExtra("online") != null && getIntent().getStringExtra("online").equals("yes")){
            btn_online.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
            btn_online.setTextColor(getResources().getColor(R.color.colorWhite));
            online = true;
        }
        other = getIntent().getStringExtra("other");
        if (other != null) {
            other_text.setText(other_text.getText().toString() + "\n" + other);
            other_text.setBackground(getResources().getDrawable(R.drawable.rounded_corners_oval_purple));
            other_text.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        String about_teacher = getIntent().getStringExtra("about_teacher");
        if (about_teacher != null) {
            edit_about_teacher.setText(about_teacher);
        }
    }

    private void displayTeacherSubjects(){
        FirebaseListOptions<SubjectFormItem> options = new FirebaseListOptions.Builder<SubjectFormItem>()
                .setQuery(myRefCurrentUserTeacherSubjects, SubjectFormItem.class)
                .setLayout(R.layout.subject_form_item)
                .build();
        adapter = new FirebaseListAdapter<SubjectFormItem>(options){
            @Override
            protected void populateView(View v, SubjectFormItem model, int position) {
                TextView subject_subject = v.findViewById(R.id.subject_subject);
                TextView subject_special_subjects = v.findViewById(R.id.subject_special_subjects);
                TextView subject_hour_cost = v.findViewById(R.id.subject_hour_cost);
                TextView subject_comment = v.findViewById(R.id.subject_comment);

                subject_subject.setText(model.getSubject());
                subject_special_subjects.setText(model.getSpecial_subject());
                subject_hour_cost.setText(model.getPrice() + "₽");
                subject_comment.setText(model.getComment());
            }
        };
        list_subjects.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
    }
}
