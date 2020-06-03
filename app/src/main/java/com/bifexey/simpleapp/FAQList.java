package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FAQList extends AppCompatActivity {

    ArrayList<String> questions = new ArrayList<>();

    ListView FAQ_list;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqlist);

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        questions.add("Зачем нужны статусы “ученик” и “преподаватель” и как они работают?");
        questions.add("Что такое SOS ( запросы о быстрой помощи )?");
        questions.add("Зачем в разделе SOS указывать свой корпус?");
        questions.add("Как найти репетитора?");
        questions.add("Как работает “SOS” ( запросы о быстрой помощи ), если я в статусе “ученик”?");
        questions.add("Как работает SOS, если я в статусе “преподавателя”?");
        questions.add("Как оформить подписку?");
        questions.add("Как отменить подписку?");
        questions.add("Почему меняется количество кнопок меню быстрого доступа при смене статуса?");
        questions.add("Нужен ли преподавательский опыт для регистрации в качестве преподавателя?");
        questions.add("Как создать репетиторское объявление?");
        questions.add("Как происходит сделка между учеником и репетитором?");
        questions.add("Зачем указывать корпус при создании репетиторского объявления?");
        questions.add("Может ли приложение предоставить мою личную информацию сторонним лицам?");
        questions.add("Как много я могу заработать за месяц?");

        FAQ_list = findViewById(R.id.FAQ_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(FAQList.this, android.R.layout.simple_list_item_1, questions);
        FAQ_list.setAdapter(adapter);

        FAQ_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FAQList.this, EachFAQ.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });
    }
}
