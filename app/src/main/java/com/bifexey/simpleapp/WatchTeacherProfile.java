package com.bifexey.simpleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

public class WatchTeacherProfile extends AppCompatActivity implements View.OnClickListener{
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefDeleteFlaggedTeacher;
    DatabaseReference myRefCurrentTeacher;
    StorageReference mStorageRef;

    AlertDialog.Builder dialogWindow;

    ArrayList<String> where = new ArrayList<>();

    String o_user_phone_number;
    String metro_station, other;

    ImageView W_T_image, btn_back, W_T_delete_teacher;
    TextView W_T_name, W_T_location, W_T_about_teacher;
    TextView W_T_teacher_subjects, W_T_chat_teacher;
    TextView W_T_where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_teacher_profile);

        o_user_phone_number = getIntent().getStringExtra("teacher_ph");

        myRefCurrentTeacher = myRefUsers.child(o_user_phone_number);
        myRefDeleteFlaggedTeacher = myRefUsers.child(user.getPhoneNumber()).child("flagged_teachers").child(o_user_phone_number);

//        W_T_image = findViewById(R.id.W_T_image);
//        W_T_name = findViewById(R.id.W_T_name);
//        W_T_location = findViewById(R.id.W_T_location);
//        W_T_about_teacher = findViewById(R.id.W_T_about_teacher);
//        W_T_teacher_subjects = findViewById(R.id.W_T_teacher_subjects);
//        W_T_delete_teacher = findViewById(R.id.W_T_delete_teacher);
//        W_T_chat_teacher = findViewById(R.id.W_T_chat_teacher);
//        btn_back = findViewById(R.id.btn_back);
//        W_T_where = findViewById(R.id.W_T_where);

        W_T_teacher_subjects.setOnClickListener(this);
        W_T_delete_teacher.setOnClickListener(this);
        W_T_chat_teacher.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        downloadImage(W_T_image, o_user_phone_number);

        myRefCurrentTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                W_T_name.setText(dataSnapshot.child("first_name").getValue().toString() + " " + dataSnapshot.child("second_name").getValue().toString());
                W_T_location.setText(dataSnapshot.child("location").getValue().toString());
                W_T_about_teacher.setText(dataSnapshot.child("about_teacher").getValue().toString());

                for (DataSnapshot ds : dataSnapshot.child("teacher_info").child("where").getChildren()){
                    String q = ds.getKey();
                    String k = ds.getValue().toString();
                    switch (q){
                        case "metro_station":
                            metro_station = k;
                            where.add("У меня (" + metro_station + ")");
                            break;
                        case "online":
                            where.add("Онлайн");
                            break;
                        case "to_student":
                            where.add("У студента");
                            break;
                        case "other":
                            other = k;
                            where.add("Другое место (" + other + ")");
                            break;
                    }
                }

                for(String i : where){
                    W_T_where.setText(W_T_where.getText() + "#" + i + "\n");
                }
                W_T_where.setText(W_T_where.getText().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.W_T_teacher_subjects:
//                Intent intent = new Intent(WatchTeacherProfile.this, MySubjects.class);
//                intent.putExtra("from_teacher_profile", o_user_phone_number);
//                startActivity(intent);
//                break;
//            case R.id.W_T_delete_teacher:
//                createDialogWindow();
//                dialogWindow.show();
//                break;
//            case R.id.W_T_chat_teacher:
//                Intent intent1 = new Intent(WatchTeacherProfile.this, Chat.class);
//                intent1.putExtra("o_user_phone_number", o_user_phone_number);
//                startActivity(intent1);
//                break;
//            case R.id.btn_back:
//                finish();
//                break;
//        }
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void downloadImage(final ImageView img, String phone_number) {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(phone_number, 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img);
            }
        });
    }

    private void createDialogWindow(){
        final Context context = WatchTeacherProfile.this;

        String title = "Удалить этого учителя из избранного?\n" +
                "Это действие нельзя будет отменить";
        String button1String = "Да";
        String button2String = "Нет";

        dialogWindow = new AlertDialog.Builder(context);
        dialogWindow.setTitle(title);

        dialogWindow.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                myRefDeleteFlaggedTeacher.removeValue();

                Toast.makeText(WatchTeacherProfile.this, "Удалено", Toast.LENGTH_SHORT).show();
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
