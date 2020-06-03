package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FillOther extends AppCompatActivity implements View.OnClickListener {

    String TAG = "lol";

    EditText edit_other;
    ImageView btn_back, btn_other_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_other);

        Log.d(TAG, "other activity");

//        edit_other = findViewById(R.id.edit_other);
//        btn_other_ok = findViewById(R.id.btn_other_ok);
//        btn_back = findViewById(R.id.btn_back);

        btn_other_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_other_ok:
//                if (edit_other.getText().toString().trim().equals(""))
//                    Toast.makeText(getApplicationContext(), "Вы ничего не написали", Toast.LENGTH_LONG).show();
//                else
//                    remakeIntentInfoAndStartTRegister();
//                break;
//            case R.id.btn_back:
//                finish();
//                break;
//        }
    }

    private void remakeIntentInfoAndStartTRegister(){
        Intent intent = new Intent(getApplicationContext(), TeacherRegistration.class);
        intent.putExtra("to_me", getIntent().getStringExtra("to_me"));
        intent.putExtra("metro_station", getIntent().getStringExtra("metro_station"));
        intent.putExtra("to_student", getIntent().getStringExtra("to_student"));
        intent.putExtra("online", getIntent().getStringExtra("online"));
        intent.putExtra("other", edit_other.getText().toString().trim());
        intent.putExtra("about_teacher", getIntent().getStringExtra("about_teacher"));
        startActivity(intent);
        finish();
    }
}

