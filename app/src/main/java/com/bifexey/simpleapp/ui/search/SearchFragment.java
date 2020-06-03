package com.bifexey.simpleapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.R;
import com.bifexey.simpleapp.ads;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSubjects = myRef.child("subjects");

    ArrayAdapter<String> adapter;

    String TAG = "lol";
    List<String> subjects = new ArrayList<>();

    ListView listOfSubjects;
    SearchView search_view_sub;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, null);

        //listOfSubjects = v.findViewById(R.id.list_of_subjects);
        //search_view_sub = v.findViewById(R.id.search_view_sub);

        myRefSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        if (dss.getKey().equals("subject_name"))
                            subjects.add(dss.getValue().toString());
                    }

                    if (getActivity() != null) {
                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, subjects);
                        listOfSubjects.setAdapter(adapter);
                    }

                    search_view_sub.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                    listOfSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ads.class);

                            TextView t_v = (TextView) view;
                            String t_s = (String) t_v.getText();

                            intent.putExtra("sub_name", t_s);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return v;
    }
}