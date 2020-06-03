package com.bifexey.simpleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutApp extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btn_contacts, btn_terms_of_use, btn_privacy_policy;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        btn_contacts = findViewById(R.id.btn_contacts);
        btn_terms_of_use = findViewById(R.id.btn_terms_of_use);
        btn_privacy_policy = findViewById(R.id.btn_privacy_policy);
        btn_back = findViewById(R.id.btn_back);

        btn_contacts.setOnClickListener(this);
        btn_terms_of_use.setOnClickListener(this);
        btn_privacy_policy.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        SpannableString ss = new SpannableString("Продолжая использовать приложение, вы даёте согласие на обработку персональных данных в соответствии с политикой конфиденциальности.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(AboutApp.this, AppPrivacyPolicy.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 102, 131, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv_text_about = findViewById(R.id.tv_text_about);
        tv_text_about.setText(ss);
        tv_text_about.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text_about.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_contacts:
                Intent intent = new Intent(AboutApp.this, AppContacts.class);
                startActivity(intent);
                break;
            case R.id.btn_terms_of_use:
                Intent intent1 = new Intent(AboutApp.this, AppTermsOfUse.class);
                startActivity(intent1);
                break;
            case R.id.btn_privacy_policy:
                Intent intent2 = new Intent(AboutApp.this, AppPrivacyPolicy.class);
                startActivity(intent2);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}

