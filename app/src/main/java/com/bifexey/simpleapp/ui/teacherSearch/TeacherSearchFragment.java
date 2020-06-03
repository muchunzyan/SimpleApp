package com.bifexey.simpleapp.ui.teacherSearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.Filter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeacherSearchFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefSOSAds = myRef.child("AllSOSAds");

    DatabaseReference myRefCurrentUserSOSAdsDelete;

    SOSAdAdapter sosAdAdapter;

    ArrayList<SOSAd> sosAds = new ArrayList<>();

    ListView teacher_search_list_of_subjects;
    LinearLayout btn_teacher_filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teacher_search, null);

        teacher_search_list_of_subjects = v.findViewById(R.id.teacher_search_list_of_subjects);
        btn_teacher_filter = v.findViewById(R.id.btn_teacher_filter);

        btn_teacher_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Filter.class);
                startActivity(intent);
            }
        });

        displaySOSAds();
        checkOldData();
        return v;
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkOldData(){
        final String[] key = new String[1];
        final int[] year = new int[1];
        final int[] month = new int[1];
        final int[] day = new int[1];
        final int[] hour = new int[1];
        final int[] minute = new int[1];

        myRefSOSAds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        switch (dss.getKey()) {
                            case "sos_year":
                                year[0] = Integer.parseInt(dss.getValue().toString());
                                break;
                            case "sos_month":
                                month[0] = Integer.parseInt(dss.getValue().toString());
                                break;
                            case "sos_day":
                                day[0] = Integer.parseInt(dss.getValue().toString());
                                break;
                            case "sos_hour":
                                hour[0] = Integer.parseInt(dss.getValue().toString());
                                break;
                            case "sos_minute":
                                minute[0] = Integer.parseInt(dss.getValue().toString());
                                break;
                        }
                    }

                    String date = day[0] + "." + month[0] + "." + year[0] + " " + hour[0] + ":" + minute[0];
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    try {
                        Date parse = dateFormat.parse(date);
                        long timeCurr = System.currentTimeMillis() - parse.getTime();
                        if (timeCurr >= 0) {
                            key[0] = ds.getKey();

                            myRefCurrentUserSOSAdsDelete = myRef.child("AllSOSAds").child(key[0]);
                            myRefCurrentUserSOSAdsDelete.removeValue();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
