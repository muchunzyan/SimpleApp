package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class ChooseMetro extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefMetroStations = myRef.child("metro_stations");

    ArrayAdapter<String> adapter;
    List<String> metro_stations = new ArrayList<>();

    String TAG = "lol";
    String t_m;

    SearchView search_view_metro;
    ListView list_of_metro;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_metro);

//        search_view_metro = findViewById(R.id.search_view_metro);
//        list_of_metro = findViewById(R.id.list_of_metro);
//        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myRefMetroStations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    metro_stations.add(ds.getValue().toString());

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_list_item , metro_stations);
                    list_of_metro.setAdapter(adapter);

                    search_view_metro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });

                    list_of_metro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView t_v = (TextView) view;
                            t_m = (String) t_v.getText();

                            remakeIntentInfoAndStartTRegister();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void remakeIntentInfoAndStartTRegister(){
        Intent intent = new Intent(getApplicationContext(), TeacherRegistration.class);
        intent.putExtra("metro_station", t_m);
        intent.putExtra("to_student", getIntent().getStringExtra("to_student"));
        intent.putExtra("online", getIntent().getStringExtra("online"));
        intent.putExtra("other", getIntent().getStringExtra("other"));
        intent.putExtra("about_teacher", getIntent().getStringExtra("about_teacher"));
        startActivity(intent);
        finish();
    }
}

