package com.bifexey.simpleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class EditSOS extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefCurrentUserSOSAds = myRefCurrentUser.child("SOSAds");
    DatabaseReference myRefCurrentUserCurrentSOSAd;
    StorageReference mStorageRef;
    DatabaseReference myRefCurrentUserSOSAdsDelete;

    AlertDialog.Builder dialogWindow;

    String key;
    String first_name, second_name;
    String year, month, day, hour, minute;

    ImageView check_SOS_image, btn_back, check_SOS_btn_delete;
    TextView check_SOS_name, check_SOS_subjectFromSpinner, check_SOS_locationFromSpinner, check_SOS_subjectName,
            check_SOS_subjectTopic, check_SOS_date_time, check_SOS_comment, check_SOS_price;
    Button check_SOS_btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sos);

        check_SOS_image = findViewById(R.id.check_SOS_image);
        check_SOS_name = findViewById(R.id.check_SOS_name);
        check_SOS_subjectFromSpinner = findViewById(R.id.check_SOS_subjectFromSpinner);
        check_SOS_locationFromSpinner = findViewById(R.id.check_SOS_locationFromSpinner);
        check_SOS_subjectName = findViewById(R.id.check_SOS_subjectName);
        check_SOS_subjectTopic = findViewById(R.id.check_SOS_subjectTopic);
        check_SOS_date_time = findViewById(R.id.check_SOS_date_time);
        check_SOS_comment = findViewById(R.id.check_SOS_comment);
        check_SOS_price = findViewById(R.id.check_SOS_price);
        check_SOS_btn_delete = findViewById(R.id.check_SOS_btn_delete);
        check_SOS_btn_edit = findViewById(R.id.check_SOS_btn_edit);
        btn_back = findViewById(R.id.btn_back);

        check_SOS_btn_delete.setOnClickListener(this);
        check_SOS_btn_edit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        key = getIntent().getStringExtra("key");
        myRefCurrentUserCurrentSOSAd = myRefCurrentUserSOSAds.child(key);

        downloadImage(check_SOS_image);

        myRefCurrentUserCurrentSOSAd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    switch (ds.getKey()) {
                        case "sos_first_name":
                            first_name = ds.getValue().toString();
                            break;
                        case "sos_second_name":
                            second_name = ds.getValue().toString();
                            break;
                        case "sos_subjectFromSpinner":
                            check_SOS_subjectFromSpinner.setText(ds.getValue().toString());
                            break;
                        case "sos_locationFromSpinner":
                            check_SOS_locationFromSpinner.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectName":
                            check_SOS_subjectName.setText(ds.getValue().toString());
                            break;
                        case "sos_subjectTopic":
                            check_SOS_subjectTopic.setText(ds.getValue().toString());
                            break;
                        case "sos_year":
                            year = ds.getValue().toString();
                            break;
                        case "sos_month":
                            month = ds.getValue().toString();
                            break;
                        case "sos_day":
                            day = ds.getValue().toString();
                            break;
                        case "sos_hour":
                            hour = ds.getValue().toString();
                            break;
                        case "sos_minute":
                            minute = ds.getValue().toString();
                            break;
                        case "sos_comment":
                            if (!ds.getValue().toString().equals(""))
                                check_SOS_comment.setText(ds.getValue().toString());
                            else
                                check_SOS_comment.setText("Без комментария");
                            break;
                        case "sos_price":
                            check_SOS_price.setText("Цена: " + ds.getValue().toString());
                            break;
                    }
                }

                check_SOS_name.setText(first_name + " " + second_name);
                check_SOS_date_time.setText(day + "/" + month + "/" + year + " " + hour + ":" + minute);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_SOS_btn_delete:
                createDialogWindow();
                dialogWindow.show();
                break;
            case R.id.check_SOS_btn_edit:
                Intent intent = new Intent(EditSOS.this, CreateSOS.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
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
        final Context context = EditSOS.this;

        String title = "Удалить объявление?";
        String button1String = "Да";
        String button2String = "Нет";

        dialogWindow = new AlertDialog.Builder(context);
        dialogWindow.setTitle(title);

        dialogWindow.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                myRefCurrentUserSOSAdsDelete = myRefCurrentUserSOSAds.child(key);
                myRefCurrentUserSOSAdsDelete.removeValue();

                myRefCurrentUserSOSAdsDelete = myRef.child("AllSOSAds").child(key);
                myRefCurrentUserSOSAdsDelete.removeValue();

                Toast.makeText(EditSOS.this, "Удалено", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialogWindow.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        dialogWindow.setCancelable(true);
        dialogWindow.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {}
        });
    }
}
