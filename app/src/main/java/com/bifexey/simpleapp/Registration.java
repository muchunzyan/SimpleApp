package com.bifexey.simpleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Registration extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefLocations = myRef.child("locations");

    ArrayAdapter<String> adapter;

    List<String> locations = new ArrayList<>();

    String TAG = "lol";
    String t_l;

    Spinner location_spinner_r;
    EditText edit_text_first_name_r, edit_text_second_name_r;
    LinearLayout btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edit_text_first_name_r = findViewById(R.id.edit_text_first_name_r);
        edit_text_second_name_r = findViewById(R.id.edit_text_second_name_r);
        btn_register = findViewById(R.id.btn_register);
        location_spinner_r = findViewById(R.id.location_spinner_r);

        myRefLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    locations.add(ds.getValue().toString());

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location_spinner_r.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        location_spinner_r.setPrompt("Location");
        location_spinner_r.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView t_v = (TextView) view;
                t_l = (String) t_v.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name_r = edit_text_first_name_r.getText().toString().trim();
                String second_name_r = edit_text_second_name_r.getText().toString().trim();

                if (first_name_r.equals("") || second_name_r.equals("")){
                    Toast.makeText(getApplicationContext(), "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
                }
                else {
                    myRefCurrentUser.setValue(new UserInfo(first_name_r, second_name_r, t_l, "student"));

                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        SpannableString ss1 = new SpannableString("Создавая аккаунт, вы соглашаетесь с нашим  Пользовательским соглашением");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Registration.this, AppTermsOfUse.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 43, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView bottom_text1 = findViewById(R.id.bottom_text1);
        bottom_text1.setText(ss1);
        bottom_text1.setMovementMethod(LinkMovementMethod.getInstance());
        bottom_text1.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss2 = new SpannableString("с нашей Политикой конфиденциальности");
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Registration.this, AppPrivacyPolicy.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss2.setSpan(clickableSpan2, 8, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView bottom_text2 = findViewById(R.id.bottom_text2);
        bottom_text2.setText(ss2);
        bottom_text2.setMovementMethod(LinkMovementMethod.getInstance());
        bottom_text2.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
    }
}
