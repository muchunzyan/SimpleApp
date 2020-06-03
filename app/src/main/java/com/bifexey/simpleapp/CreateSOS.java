package com.bifexey.simpleapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateSOS extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSubjects = myRef.child("subjects");
    DatabaseReference myRefLocations = myRef.child("locations");
    DatabaseReference myRefSOSAds = myRef.child("AllSOSAds");
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserSOSAds = myRefCurrentUser.child("SOSAds");
    DatabaseReference myRefCurrentUserCurrentSOSAd;
    DatabaseReference myRefCurrentUserSOSAdsDelete;

    ArrayAdapter<String> adapter;

    List<String> subjects = new ArrayList<>();
    List<String> locations = new ArrayList<>();

    String TAG = "lol";
    String key;
    int FinYear = 0, FinMonth, FinDayOfMonth;
    int FinHourOfDay = 100, FinMinute;
    String SOS_first_name, SOS_second_name, SOS_location;
    String spinSub, spinLoc;

    Calendar calendar = Calendar.getInstance();

    Button SOS_btn_choose_date, SOS_btn_choose_time, SOS_btn_today;
    LinearLayout SOS_btn_publish;
    DatePickerDialog.OnDateSetListener DataSetListener;
    TimePickerDialog.OnTimeSetListener TimeSetListener;
    Spinner SOS_subject_spinner, SOS_locations_spinner;
    EditText SOS_edit_subject_name, SOS_edit_topic, SOS_edit_price, SOS_edit_comment;
    ImageView btn_close;
    TextView SOS_title_text, SOS_btn_publish_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sos);

        key = getIntent().getStringExtra("key");

        SOS_subject_spinner = findViewById(R.id.SOS_subject_spinner);
        SOS_locations_spinner = findViewById(R.id.SOS_locations_spinner);
        SOS_btn_choose_date = findViewById(R.id.SOS_btn_choose_date);
        SOS_btn_choose_time = findViewById(R.id.SOS_btn_choose_time);
        SOS_btn_today = findViewById(R.id.SOS_btn_today);
        SOS_btn_publish = findViewById(R.id.SOS_btn_publish);
        SOS_edit_subject_name = findViewById(R.id.SOS_edit_subject_name);
        SOS_edit_topic = findViewById(R.id.SOS_edit_topic);
        SOS_edit_price = findViewById(R.id.SOS_edit_price);
        SOS_edit_comment = findViewById(R.id.SOS_edit_comment);
        btn_close = findViewById(R.id.btn_close);
        SOS_title_text = findViewById(R.id.SOS_title_text);
        SOS_btn_publish_text = findViewById(R.id.SOS_btn_publish_text);

        SOS_btn_choose_time.setOnClickListener(this);
        SOS_btn_choose_date.setOnClickListener(this);
        SOS_btn_today.setOnClickListener(this);
        SOS_btn_publish.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        subjects.add("Предмет");
        locations.add("Корпус");

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
                    SOS_subject_spinner.setAdapter(adapter);
                }

                if (key != null){
                    SOS_title_text.setText("Редактирование");
                    SOS_btn_publish_text.setText("Сохранить");

                    setInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        myRefLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    locations.add(ds.getValue().toString());

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SOS_locations_spinner.setAdapter(adapter);
                }

                if (key != null){
                    SOS_title_text.setText("Редактирование");
                    SOS_btn_publish_text.setText("Сохранить");

                    setInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("first_name")){
                        SOS_first_name = ds.getValue().toString();
                    }
                    if (ds.getKey().equals("second_name")){
                        SOS_second_name = ds.getValue().toString();
                    }
                    if (ds.getKey().equals("location")){
                        SOS_location = ds.getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                SOS_btn_choose_date.setText(addZero(Integer.toString(dayOfMonth)) + "/" +
                        addZero(Integer.toString(month)) + "/" +
                        addZero(Integer.toString(year)));

                FinYear = year;
                FinMonth = month;
                FinDayOfMonth = dayOfMonth;
            }
        };

        TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SOS_btn_choose_time.setText(addZero(Integer.toString(hourOfDay)) + ":" +
                        addZero(Integer.toString(minute)));

                FinHourOfDay = hourOfDay;
                FinMinute = minute;
            }
        };
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.SOS_btn_choose_time:
//                int hourOfDay, minute;
//
//                if (key == null) {
//                    hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//                    minute = calendar.get(Calendar.MINUTE);
//                }
//                else {
//                    hourOfDay = FinHourOfDay;
//                    minute = FinMinute;
//                }
//
//                TimePickerDialog tDialog = new TimePickerDialog(
//                        CreateSOS.this,
//                        TimeSetListener,
//                        hourOfDay, minute,
//                        DateFormat.is24HourFormat(CreateSOS.this)
//                );
//
//                tDialog.show();
//
//                SOS_btn_choose_time.setText(addZero(Integer.toString(hourOfDay)) + ":" +
//                        addZero(Integer.toString(minute)));
//
//                FinHourOfDay = hourOfDay;
//                FinMinute = minute;
//                break;
//            case R.id.SOS_btn_choose_date:
//                int year, month, day;
//
//                if (key == null) {
//                    year = calendar.get(Calendar.YEAR);
//                    month = calendar.get(Calendar.MONTH);
//                    day = calendar.get(Calendar.DAY_OF_MONTH);
//                }
//                else {
//                    year = FinYear;
//                    month = FinMonth;
//                    day = FinDayOfMonth;
//                }
//
//                DatePickerDialog dDialog = new DatePickerDialog(
//                        CreateSOS.this,
//                        DataSetListener,
//                        year, month, day
//                );
//
//                dDialog.show();
//
//                SOS_btn_choose_date.setBackgroundColor(getResources().getColor(R.color.colorPurple));
//                SOS_btn_choose_date.setTextColor(getResources().getColor(R.color.colorWhite));
//                SOS_btn_today.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                SOS_btn_today.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//            case R.id.SOS_btn_today:
//                SOS_btn_choose_date.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                SOS_btn_choose_date.setTextColor(getResources().getColor(R.color.colorPrimary));
//                SOS_btn_today.setBackgroundColor(getResources().getColor(R.color.colorPurple));
//                SOS_btn_today.setTextColor(getResources().getColor(R.color.colorWhite));
//                SOS_btn_choose_date.setText("Выбрать дату");
//
//                FinYear = calendar.get(Calendar.YEAR);
//                FinMonth = calendar.get(Calendar.MONTH) + 1;
//                FinDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//                break;
//            case R.id.SOS_btn_publish:
//                String SOS_subjectFromSpinner = SOS_subject_spinner.getSelectedItem().toString();
//                String SOS_locationFromSpinner = SOS_locations_spinner.getSelectedItem().toString();
//                String SOS_subjectName = SOS_edit_subject_name.getText().toString().trim();
//                String SOS_subjectTopic = SOS_edit_topic.getText().toString().trim();
//                String SOS_price = "";
//                if (!SOS_edit_price.getText().toString().equals(""))
//                    SOS_price = Integer.toString(Integer.parseInt(SOS_edit_price.getText().toString()));
//                String SOS_comment = SOS_edit_comment.getText().toString().trim();
//
//                if (SOS_subjectFromSpinner.equals("Предмет") || SOS_locationFromSpinner.equals("Корпус") ||
//                        SOS_subjectName.equals("") || SOS_subjectTopic.equals("") || FinYear == 0 || FinHourOfDay == 100 ||
//                        SOS_price.equals("")){
//                    Toast.makeText(getApplicationContext(), "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
//                }
//                else if (!checkTimeAndDate(FinYear, FinMonth, FinDayOfMonth, FinHourOfDay, FinMinute)){
//                    Toast.makeText(getApplicationContext(), "Неподходящее время или дата", Toast.LENGTH_SHORT).show();
//                }
//                else if (SOS_subjectName.length() > 50 || SOS_subjectTopic.length() > 50 ||
//                        SOS_price.length() > 10 || SOS_comment.length() > 80){
//                    Toast.makeText(getApplicationContext(), "Слишком длинный текст", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    DatabaseReference myRefCurrentUserSOSAdsPush = myRefCurrentUserSOSAds.push();
//                    String idKey = myRefCurrentUserSOSAdsPush.getKey();
//
//                    if (key != null) {
//                        myRefCurrentUserSOSAdsDelete = myRefCurrentUserSOSAds.child(key);
//
//                        myRefCurrentUserSOSAdsDelete.setValue(new SOSAd(SOS_first_name, SOS_second_name,
//                                SOS_location, SOS_subjectFromSpinner, SOS_locationFromSpinner, SOS_subjectName, SOS_subjectTopic,
//                                addZero(Integer.toString(FinYear)), addZero(Integer.toString(FinMonth)),
//                                addZero(Integer.toString(FinDayOfMonth)), addZero(Integer.toString(FinHourOfDay)),
//                                addZero(Integer.toString(FinMinute)),
//                                SOS_price, SOS_comment, user.getPhoneNumber(), key));
//
//
//                        myRefCurrentUserSOSAdsDelete = myRef.child("AllSOSAds").child(key);
//
//                        myRefCurrentUserSOSAdsDelete.setValue(new SOSAd(SOS_first_name, SOS_second_name,
//                                SOS_location, SOS_subjectFromSpinner, SOS_locationFromSpinner, SOS_subjectName, SOS_subjectTopic,
//                                addZero(Integer.toString(FinYear)), addZero(Integer.toString(FinMonth)),
//                                addZero(Integer.toString(FinDayOfMonth)), addZero(Integer.toString(FinHourOfDay)),
//                                addZero(Integer.toString(FinMinute)),
//                                SOS_price, SOS_comment, user.getPhoneNumber(), key));
//                    }else {
//                        myRefCurrentUserSOSAdsPush.setValue(new SOSAd(SOS_first_name, SOS_second_name,
//                                SOS_location, SOS_subjectFromSpinner, SOS_locationFromSpinner, SOS_subjectName, SOS_subjectTopic,
//                                addZero(Integer.toString(FinYear)), addZero(Integer.toString(FinMonth)),
//                                addZero(Integer.toString(FinDayOfMonth)), addZero(Integer.toString(FinHourOfDay)),
//                                addZero(Integer.toString(FinMinute)),
//                                SOS_price, SOS_comment, user.getPhoneNumber(), idKey));
//
//                        myRefSOSAds.child(myRefCurrentUserSOSAdsPush.getKey()).setValue(new SOSAd(SOS_first_name, SOS_second_name,
//                                SOS_location, SOS_subjectFromSpinner, SOS_locationFromSpinner, SOS_subjectName, SOS_subjectTopic,
//                                addZero(Integer.toString(FinYear)), addZero(Integer.toString(FinMonth)),
//                                addZero(Integer.toString(FinDayOfMonth)), addZero(Integer.toString(FinHourOfDay)),
//                                addZero(Integer.toString(FinMinute)),
//                                SOS_price, SOS_comment, user.getPhoneNumber(), idKey));
//                    }
//
//                    Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
//
//                    if (key != null){
//                        Intent intent = new Intent(CreateSOS.this, EditSOS.class);
//                        intent.putExtra("key", key);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        finish();
//                    }
//                }
//                break;
//            case R.id.btn_close:
//                finish();
//                break;
//        }
    }

    private boolean checkTimeAndDate(int year, int month, int day, int hour, int minute){
        boolean ret = false;
        String date = day + "." + month + "." + year + " " + hour + ":" + minute;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        try {
            Date parse = dateFormat.parse(date);
            long timeCurr = System.currentTimeMillis() - parse.getTime();
            if (timeCurr < 0) {
                ret = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void setInfo(){
        myRefCurrentUserCurrentSOSAd = myRefCurrentUserSOSAds.child(key);

        myRefCurrentUserCurrentSOSAd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    switch (ds.getKey()) {
                        case "sos_subjectFromSpinner":
                            spinSub = ds.getValue().toString();
                            SOS_subject_spinner.setSelection(subjects.indexOf(spinSub));
                            break;
                        case "sos_locationFromSpinner":
                            spinLoc = ds.getValue().toString();
                            SOS_locations_spinner.setSelection(locations.indexOf(spinLoc));
                            break;
                        case "sos_subjectName":
                            SOS_edit_subject_name.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectTopic":
                            SOS_edit_topic.setText(ds.getValue().toString());
                            break;
                        case "sos_year":
                            FinYear = Integer.parseInt(ds.getValue().toString());
                            break;
                        case "sos_month":
                            FinMonth = Integer.parseInt(ds.getValue().toString());
                            break;
                        case "sos_day":
                            FinDayOfMonth = Integer.parseInt(ds.getValue().toString());
                            break;
                        case "sos_hour":
                            FinHourOfDay = Integer.parseInt(ds.getValue().toString());
                            break;
                        case "sos_minute":
                            FinMinute = Integer.parseInt(ds.getValue().toString());
                            break;
                        case "sos_comment":
                            SOS_edit_comment.setText(ds.getValue().toString());
                            break;
                        case "sos_price":
                            SOS_edit_price.setText(ds.getValue().toString());
                            break;
                    }
                }

                Calendar now = Calendar.getInstance();
                if (now.get(Calendar.YEAR) == FinYear &&
                        now.get(Calendar.MONTH) + 1 == FinMonth &&
                        now.get(Calendar.DAY_OF_MONTH) == FinDayOfMonth) {
                    SOS_btn_today.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                    SOS_btn_today.setTextColor(getResources().getColor(R.color.colorWhite));
                    SOS_btn_choose_date.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    SOS_btn_choose_date.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    SOS_btn_choose_date.setText(addZero(Integer.toString(FinDayOfMonth)) + "/" +
                            addZero(Integer.toString(FinMonth)) + "/" + addZero(Integer.toString(FinYear)));
                    SOS_btn_choose_date.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                    SOS_btn_choose_date.setTextColor(getResources().getColor(R.color.colorWhite));
                    SOS_btn_today.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    SOS_btn_today.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                SOS_btn_choose_time.setText(addZero(Integer.toString(FinHourOfDay)) + ":" +
                        addZero(Integer.toString(FinMinute)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String addZero(String num){
        if (Integer.parseInt(num) < 10)
            return ("0" + num);
        else
            return num;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            if (key != null) {
//                Intent intent = new Intent(CreateSOS.this, EditSOS.class);
//                intent.putExtra("key", key);
//                startActivity(intent);
//                finish();
//            }else{
//                finish();
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        if (key != null) {
//            Intent intent = new Intent(CreateSOS.this, EditSOS.class);
//            intent.putExtra("key", key);
//            startActivity(intent);
//            finish();
//        }else{
//            finish();
//        }
    }
}
