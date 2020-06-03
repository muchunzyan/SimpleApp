package com.bifexey.simpleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MySubjects extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentTeacherTeacherSubjects;
    StorageReference mStorageRef;

    FirebaseListAdapter adapter;

    AlertDialog.Builder dialogWindow;

    boolean from_teacher_profile = false;

    ListView list_my_subjects_account;
    TextView text_title;
    ImageView btn_back;
    ImageView btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sub);

        list_my_subjects_account = findViewById(R.id.list_my_subjects_account);
        text_title = findViewById(R.id.text_title);
        btn_back = findViewById(R.id.btn_back);
        btn_add = findViewById(R.id.btn_add);

        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        if (getIntent().getStringExtra("from_teacher_profile") != null) {
            from_teacher_profile = true;
            myRefCurrentTeacherTeacherSubjects = myRefUsers.child(getIntent().getStringExtra("from_teacher_profile")).child("teacher_info").child("subjects");
            text_title.setText("Все предметы учителя");
            btn_add.setVisibility(View.GONE);
        }else{
            myRefCurrentTeacherTeacherSubjects = myRefUsers.child(user.getPhoneNumber()).child("teacher_info").child("subjects");
            text_title.setText("Моя помощь");
        }

        displayTeacherSubjects();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add:
                Intent intent = new Intent(MySubjects.this, AddSubjectForm.class);
                startActivity(intent);
                break;
        }
    }

    private void displayTeacherSubjects(){
        FirebaseListOptions<SubjectFormItem> options = new FirebaseListOptions.Builder<SubjectFormItem>()
                .setQuery(myRefCurrentTeacherTeacherSubjects, SubjectFormItem.class)
                .setLayout(R.layout.subject_form_item)
                .build();

        adapter = new FirebaseListAdapter<SubjectFormItem>(options){
            @Override
            protected void populateView(View v, SubjectFormItem model, int position) {
                ImageView form_image = v.findViewById(R.id.form_image);
                TextView subject_subject = v.findViewById(R.id.subject_subject);
                TextView subject_special_subjects = v.findViewById(R.id.subject_special_subjects);
                TextView subject_hour_cost = v.findViewById(R.id.subject_hour_cost);
                TextView subject_comment = v.findViewById(R.id.subject_comment);
                TextView subject_key = v.findViewById(R.id.subject_key);

                if(from_teacher_profile)
                    downloadImage(form_image, getIntent().getStringExtra("from_teacher_profile"));
                else
                    downloadImage(form_image, user.getPhoneNumber());
                subject_subject.setText(model.getSubject());
                subject_special_subjects.setText(model.getSpecial_subject());
                subject_hour_cost.setText("\u20BD" + model.getPrice());
                subject_comment.setText(model.getComment());
                subject_key.setText(model.getKey());
            }
        };

        list_my_subjects_account.setAdapter(adapter);

        if (!from_teacher_profile) {
            list_my_subjects_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    RelativeLayout relativeLayout = (RelativeLayout) view;
                    String key = "Null";
                    String subject = "Null";

                    View childKey = relativeLayout.getChildAt(relativeLayout.getChildCount() - 1);
                    if (childKey instanceof TextView) {
                        key = ((TextView) childKey).getText().toString();
                    }

                    View childSubject = relativeLayout.getChildAt(0);
                    if (childSubject instanceof TextView) {
                        subject = ((TextView) childSubject).getText().toString();
                    }

                    createDialogWindow(key, subject);
                    dialogWindow.show();
                }
            });
        }
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

    private void createDialogWindow(final String key, final String subject){
        final Context context = MySubjects.this;

        String title = "Удалить данный предмет?";
        String button1String = "Да";
        String button2String = "Нет";

        dialogWindow = new AlertDialog.Builder(context);
        dialogWindow.setTitle(title);

        dialogWindow.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                DatabaseReference myRefCurrentUserTeacherSubjectsDelete = myRefCurrentTeacherTeacherSubjects.child("subjects").child(key);
                myRefCurrentUserTeacherSubjectsDelete.removeValue();

                DatabaseReference myRefAllTeacherSubjectsDelete = myRef.child("AllTeacherAds").child(subject).child(key);
                myRefAllTeacherSubjectsDelete.removeValue();

                Toast.makeText(MySubjects.this, "Удалено", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MySubjects.this, MySubjects.class);
                startActivity(intent);
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

