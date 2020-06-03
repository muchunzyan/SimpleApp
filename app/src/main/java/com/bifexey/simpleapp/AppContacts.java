package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AppContacts extends AppCompatActivity {

    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_contacts);

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SpannableString ss = new SpannableString("По всем остальным вопросам посетите FAQ");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(AppContacts.this, FAQList.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 36, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        TextView bottom_text = findViewById(R.id.bottom_text);
////        bottom_text.setText(ss);
////        bottom_text.setMovementMethod(LinkMovementMethod.getInstance());
////        bottom_text.setHighlightColor(Color.TRANSPARENT);
    }
}
