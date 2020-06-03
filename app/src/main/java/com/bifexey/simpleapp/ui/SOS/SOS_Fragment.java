package com.bifexey.simpleapp.ui.SOS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.CreateSOS;
import com.bifexey.simpleapp.EditSOS;
import com.bifexey.simpleapp.R;
import com.bifexey.simpleapp.SOSAd;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SOS_Fragment extends Fragment implements View.OnClickListener{

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefSOSAds = myRef.child("AllSOSAds");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserSOSAds = myRefCurrentUser.child("SOSAds");

    DatabaseReference myRefCurrentUserSOSAdsDelete;
    StorageReference mStorageRef;

    FirebaseListAdapter adapter;
    AlertDialog.Builder dialogWindow;

    FloatingActionButton btn_create_SOS;
    ImageView SOS_question_btn;
    ListView SOS_F_list_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sos_, null);

        btn_create_SOS = v.findViewById(R.id.btn_create_SOS);
        SOS_F_list_view = v.findViewById(R.id.SOS_F_list_view);
        SOS_question_btn = v.findViewById(R.id.SOS_question_btn);

        btn_create_SOS.setOnClickListener(this);
        SOS_question_btn.setOnClickListener(this);

        displayAds();
        checkOldData();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_SOS:
                Intent intent = new Intent(getContext(), CreateSOS.class);
                startActivity(intent);
                break;
            case R.id.SOS_question_btn:
                createDialogWindow();
                dialogWindow.show();
                break;
        }
    }

    private void displayAds(){
        FirebaseListOptions<SOSAd> options = new FirebaseListOptions.Builder<SOSAd>()
                .setQuery(myRefCurrentUserSOSAds, SOSAd.class)
                .setLayout(R.layout.my_sos_ad_item)
                .build();

        adapter = new FirebaseListAdapter<SOSAd>(options){
            @Override
            protected void populateView(View v, SOSAd model, int position) {
                ImageView my_SOS_image = v.findViewById(R.id.my_SOS_image);
                TextView my_SOS_subjectFromSpinner = v.findViewById(R.id.my_SOS_subjectFromSpinner);
                TextView my_SOS_locationFromSpinner = v.findViewById(R.id.my_SOS_locationFromSpinner);
                TextView my_SOS_subjectName = v.findViewById(R.id.my_SOS_subjectName);
                TextView my_SOS_subjectTopic = v.findViewById(R.id.my_SOS_subjectTopic);
                TextView my_SOS_date_time = v.findViewById(R.id.my_SOS_date_time);
                TextView my_SOS_price = v.findViewById(R.id.my_SOS_price);
                TextView my_SOS_key = v.findViewById(R.id.my_SOS_key);

                downloadImage(my_SOS_image);
                my_SOS_subjectFromSpinner.setText(model.getSOS_subjectFromSpinner());
                my_SOS_locationFromSpinner.setText(model.getSOS_locationFromSpinner());
                my_SOS_subjectName.setText(model.getSOS_subjectName());
                my_SOS_subjectTopic.setText(model.getSOS_subjectTopic());
                my_SOS_date_time.setText(model.getSOS_day() + "/" + model.getSOS_month() + "/" +
                        model.getSOS_year() + " " + model.getSOS_hour() + ":" + model.getSOS_minute());
                my_SOS_price.setText("\u20BD" + model.getSOS_price());
                my_SOS_key.setText(model.getSOS_key());
            }
        };

        SOS_F_list_view.setAdapter(adapter);

        View footer = new View(getActivity());
        footer.setLayoutParams( new AbsListView.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 180 ));
        SOS_F_list_view.addFooterView(footer, null, false);

        SOS_F_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout relativeLayout = (RelativeLayout) view;
                String key = "Null";

                View child = relativeLayout.getChildAt(relativeLayout.getChildCount() - 1);
                if (child instanceof TextView) {
                    key = ((TextView) child).getText().toString();
                }

                Intent intent = new Intent(getActivity(), EditSOS.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void downloadImage(final ImageView img) {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(user.getPhoneNumber(), 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img);
            }
        });
    }

    private void createDialogWindow(){
        String title = "Что такое SOS?\n" +
                "Как это работает?";
        String message = "В разделе \"SOS\" вы можете разместить заявку о быстрой помощи. " +
                "Данный вид заявок используется для получения быстрой, единовременной помощи. " +
                "Например, вы не поняли тему на лекции или семинаре. " +
                "Разместите заявку! И кто-то из зарегистрированных репетиторов вам ответит. " +
                "Вы можете договориться о встрече в любое удобное для Вас время. " +
                "Цену пользователь назначает сам.\n\n" +
                "Для того, чтобы подробнее ознакомится с функционалом приложения, перейдите в раздел FAQ";
        String button1String = "Закрыть";

        Context context = getActivity();
        dialogWindow = new AlertDialog.Builder(context);

        dialogWindow.setTitle(title);
        dialogWindow.setMessage(message);

        dialogWindow.setPositiveButton(button1String, new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        dialogWindow.setCancelable(true);
        dialogWindow.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {}
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

                            myRefCurrentUserSOSAdsDelete = myRefCurrentUserSOSAds.child(key[0]);
                            myRefCurrentUserSOSAdsDelete.removeValue();

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

        myRefCurrentUserSOSAds.addValueEventListener(new ValueEventListener() {
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

                            myRefCurrentUserSOSAdsDelete = myRefCurrentUserSOSAds.child(key[0]);
                            myRefCurrentUserSOSAdsDelete.removeValue();

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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
