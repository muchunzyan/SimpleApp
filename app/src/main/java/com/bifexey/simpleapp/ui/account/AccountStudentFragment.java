package com.bifexey.simpleapp.ui.account;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.AboutApp;
import com.bifexey.simpleapp.AccountSettings;
import com.bifexey.simpleapp.FAQList;
import com.bifexey.simpleapp.FlaggedTeachers;
import com.bifexey.simpleapp.R;
import com.bifexey.simpleapp.TeacherRegistration;
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

public class AccountStudentFragment extends Fragment implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());

    StorageReference mStorageRef;
    AlertDialog.Builder dialogWindow;

    String activity = "";
    Boolean possible = false;

    TextView text_name, text_location;
    ImageView img_account;
    LinearLayout btn_activity_student, btn_activity_teacher;
    ImageView btn_account_help;
    TextView chosen_teachers_btn, FAQ_btn, about_app_btn, settings_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teacher_account, null);

        text_name = v.findViewById(R.id.text_name);
        text_location = v.findViewById(R.id.text_location);
        btn_activity_student = v.findViewById(R.id.btn_activity_student);
        btn_activity_teacher = v.findViewById(R.id.btn_activity_teacher);
        img_account = v.findViewById(R.id.img_account);
        btn_account_help = v.findViewById(R.id.btn_account_help);
        chosen_teachers_btn = v.findViewById(R.id.chosen_teachers_btn);
        FAQ_btn = v.findViewById(R.id.FAQ_btn);
        about_app_btn = v.findViewById(R.id.about_app_btn);
        settings_btn = v.findViewById(R.id.settings_btn);

        btn_activity_student.setOnClickListener(this);
        btn_activity_teacher.setOnClickListener(this);
        btn_account_help.setOnClickListener(this);
        chosen_teachers_btn.setOnClickListener(this);
        FAQ_btn.setOnClickListener(this);
        about_app_btn.setOnClickListener(this);
        settings_btn.setOnClickListener(this);

        downloadImage();

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("about_teacher")){
                        possible = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    String first_name = "", second_name = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        switch (ds.getKey()) {
                            case "first_name":
                                first_name = ds.getValue().toString();
                                break;
                            case "second_name":
                                second_name = ds.getValue().toString();
                                break;
                            case "location":
                                text_location.setText(ds.getValue().toString());
                                break;
                            case "activity":
                                activity = ds.getValue().toString();
                                break;
                        }
                    }
                    text_name.setText(first_name + " " + second_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_student:
                myRefCurrentUser.child("activity").setValue("student");
                break;
            case R.id.btn_activity_teacher:
                if (possible) {
                    myRefCurrentUser.child("activity").setValue("teacher");
                }
                else {
                    Intent intent = new Intent(getContext(), TeacherRegistration.class);
                    startActivity(intent);
                    if(getActivity() != null) {
                        getActivity().finish();
                    }
                }
                break;
            case R.id.btn_account_help:
                createDialogWindow();
                dialogWindow.show();
                break;
            case R.id.chosen_teachers_btn:
                Intent intent4 = new Intent(getActivity(), FlaggedTeachers.class);
                startActivity(intent4);
                break;
            case R.id.FAQ_btn:
                Intent intent1 = new Intent(getActivity(), FAQList.class);
                startActivity(intent1);
                break;
            case R.id.about_app_btn:
                Intent intent2 = new Intent(getActivity(), AboutApp.class);
                startActivity(intent2);
                break;
            case R.id.settings_btn:
                Intent intent3 = new Intent(getActivity(), AccountSettings.class);
                startActivity(intent3);
                break;
        }
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void downloadImage() {

        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(user.getPhoneNumber(), 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img_account);
            }
        });
    }

    private void createDialogWindow(){
        String title = "Хочу, могу, учу!";
        String message = "Кнопка \"учитель\" дает вам возможность стать репетитором и оказывать долговременную или моментальную помощь другим пользователям за денежное вознаграждение. Статус репетитора доступен только после оформления подписки, при этом вы всегда сможете переключиться обратно на статус \"ученика\" и сами получать помощь от других репетиторов\n" +
                "Для того, чтобы более подробно ознакомится с функционалом приложения, перейдите в разлел FAQ";
        String button1String = "Закрыть";

        Context context = getActivity();
        dialogWindow = new AlertDialog.Builder(context);

        dialogWindow.setTitle(title);
        dialogWindow.setMessage(message);

        dialogWindow.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        dialogWindow.setCancelable(true);
        dialogWindow.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {}
        });
    }
}