package com.bifexey.simpleapp.ui.teacherFilterSearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.Filter;
import com.bifexey.simpleapp.MainPage;
import com.bifexey.simpleapp.R;
import com.bifexey.simpleapp.SOSAd;
import com.bifexey.simpleapp.SOSAdAdapter;
import com.bifexey.simpleapp.WatchSOS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherSearchFilterFragment extends Fragment implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSOSAds = myRef.child("AllSOSAds");

    SOSAdAdapter sosAdAdapter, filteredSosAdAdapter;

    ArrayList<SOSAd> sosAds = new ArrayList<>();
    ArrayList<SOSAd> filtered_subjects = new ArrayList<>();
    ArrayList<String> places_from_filter = new ArrayList<>();

    boolean to_me, to_student, online, university, cafe, library, co_working;
    String subject;
    int price_from, price_to;

    ListView teacher_search_list_of_subjects;
    Button btn_teacher_filter;
    TextView filter_subject, filter_places, filter_price;
    ImageView discard_filter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teacher_search_filter_fragment, null);

        teacher_search_list_of_subjects = v.findViewById(R.id.teacher_search_list_of_subjects);
        btn_teacher_filter = v.findViewById(R.id.btn_teacher_filter);
        filter_subject = v.findViewById(R.id.filter_subject);
        filter_places = v.findViewById(R.id.filter_places);
        filter_price = v.findViewById(R.id.filter_price);
        discard_filter = v.findViewById(R.id.discard_filter);

        btn_teacher_filter.setOnClickListener(this);
        discard_filter.setOnClickListener(this);

        displaySOSAds();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_teacher_filter:
                Intent intent = new Intent(getActivity(), Filter.class);
                startActivity(intent);
                break;
            case R.id.discard_filter:
                Intent intent1 = new Intent(getActivity(), MainPage.class);
                startActivity(intent1);
                break;
        }
    }

    private void displaySOSAds(){
        myRefSOSAds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String sos_subjectFromSpinner, sos_locationFromSpinner, sos_subjectName, sos_subjectTopic, sos_day, sos_month, sos_year, sos_hour, sos_minute, sos_price, sos_phone_number, sos_key;

                    sos_subjectFromSpinner = ds.child("sos_subjectFromSpinner").getValue().toString();
                    sos_locationFromSpinner = ds.child("sos_subjectFromSpinner").getValue().toString();
                    sos_subjectName = ds.child("sos_subjectName").getValue().toString();
                    sos_subjectTopic = ds.child("sos_subjectTopic").getValue().toString();
                    sos_day = ds.child("sos_day").getValue().toString();
                    sos_month = ds.child("sos_month").getValue().toString();
                    sos_year = ds.child("sos_year").getValue().toString();
                    sos_hour = ds.child("sos_hour").getValue().toString();
                    sos_minute = ds.child("sos_minute").getValue().toString();
                    sos_price = ds.child("sos_price").getValue().toString();
                    sos_phone_number = ds.child("sos_phone_number").getValue().toString();
                    sos_key = ds.child("sos_key").getValue().toString();

                    if (!sos_phone_number.equals(user.getPhoneNumber()))
                        sosAds.add(new SOSAd("", "", "", sos_subjectFromSpinner, sos_locationFromSpinner, sos_subjectName, sos_subjectTopic, sos_year, sos_month, sos_day, sos_hour, sos_minute, "\u20BD" + sos_price, "", sos_phone_number, sos_key));
                }

                sosAdAdapter = new SOSAdAdapter(requireActivity(), R.layout.my_sos_ad_item, sosAds);
                teacher_search_list_of_subjects.setAdapter(sosAdAdapter);

                teacher_search_list_of_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RelativeLayout relativeLayout = (RelativeLayout) view;
                        String idd = "";

                        for(int i = 0; i < relativeLayout.getChildCount(); i++) {
                            View child = relativeLayout.getChildAt(i);

                            if (child instanceof TextView) {
                                idd = ((TextView) child).getText().toString();
                            }
                        }

                        Intent intent = new Intent(getActivity(), WatchSOS.class);
                        intent.putExtra("key", idd);
                        startActivity(intent);
                    }
                });

                readFilterData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readFilterData(){
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getString("subject") != null) {
            subject = getActivity().getIntent().getExtras().getString("subject");
            to_me = getActivity().getIntent().getExtras().getBoolean("to_me", false);
            to_student = getActivity().getIntent().getExtras().getBoolean("to_student", false);
            online = getActivity().getIntent().getExtras().getBoolean("online", false);
            university = getActivity().getIntent().getExtras().getBoolean("university", false);
            cafe = getActivity().getIntent().getExtras().getBoolean("cafe", false);
            library = getActivity().getIntent().getExtras().getBoolean("library", false);
            co_working = getActivity().getIntent().getExtras().getBoolean("co_working", false);
            price_from = Integer.parseInt(getActivity().getIntent().getExtras().getString("price_from"));
            price_to = Integer.parseInt(getActivity().getIntent().getExtras().getString("price_to"));

            filter_subject.setText(filter_subject.getText() + subject);
            filter_price.setText(filter_price.getText() + "" + price_from + "-" + price_to + " ₽");

            if (to_me) {
                places_from_filter.add("to_me");
                filter_places.setText(filter_places.getText() + "у меня, ");
            }
            if (to_student) {
                places_from_filter.add("to_student");
                filter_places.setText(filter_places.getText() + "у ученика, ");
            }
            if (online) {
                places_from_filter.add("online");
                filter_places.setText(filter_places.getText() + "онлайн, ");
            }
            if (university) {
                places_from_filter.add("university");
                filter_places.setText(filter_places.getText() + "университет, ");
            }
            if (cafe) {
                places_from_filter.add("cafe");
                filter_places.setText(filter_places.getText() + "кафе, ");
            }
            if (library) {
                places_from_filter.add("library");
                filter_places.setText(filter_places.getText() + "библиотека, ");
            }
            if (co_working) {
                places_from_filter.add("co_working");
                filter_places.setText(filter_places.getText() + "co-working, ");
            }

            String st = filter_places.getText().toString().trim();
            st = st.substring(0, st.length() - 1);
            filter_places.setText(st);

            filter();
        }
    }

    private void filter(){
        for (int i = 0; i < sosAds.size(); i++){
            if (sosAds.get(i).getSOS_subjectFromSpinner().equals(subject) &&
                    price_from <= Integer.parseInt(sosAds.get(i).getSOS_price().substring(1)) && Integer.parseInt(sosAds.get(i).getSOS_price().substring(1)) <= price_to){

                String ph_numb = sosAds.get(i).getSOS_phone_number();
                final ArrayList<String> teacher_places = new ArrayList<>();
                final int finalI = i;

                DatabaseReference myRefTeacherPlaces = myRef.child("users").child(ph_numb).child("teacher_info").child("where");
                myRefTeacherPlaces.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            if (!ds.getKey().equals("metro_station")){
                                teacher_places.add(ds.getKey());
                            }
                        }

                        boolean can = true;
                        for (int j = 0; j < teacher_places.size(); j ++){
                            if (can) {
                                for (int k = 0; k < places_from_filter.size(); k++) {
                                    if (teacher_places.get(j).equals(places_from_filter.get(k))) {
                                        filtered_subjects.add(sosAds.get(finalI));
                                        can = false;
                                        break;
                                    }
                                }
                            }
                        }

//                        filteredSosAdAdapter = new SOSAdAdapter(requireActivity(), R.layout.my_sos_ad_item, filtered_subjects);
//                        teacher_search_list_of_subjects.setAdapter(filteredSosAdAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}

