package com.bifexey.simpleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountSettings extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());
    DatabaseReference myRefLocations = myRef.child("locations");
    StorageReference mStorageRef;
    StorageReference mDownStorageRef = FirebaseStorage.getInstance().getReference();

    Uri imgUri;

    ArrayAdapter<String> adapter;

    List<AuthUI.IdpConfig> providers;
    List<String> locations = new ArrayList<>();

    private static final int MY_REQUEST_CODE = 7117;
    String TAG = "lol";
    String t_l;
    Boolean image_changed = false;

    Spinner location_spinner;
    EditText edit_first_name, edit_second_name;
    LinearLayout btn_accept_changes, btn_signOut;
    ImageView change_photo_im, btn_back;
    LinearLayout lay_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

//        edit_first_name = findViewById(R.id.edit_first_name);
//        edit_second_name = findViewById(R.id.edit_second_name);
//        btn_accept_changes = findViewById(R.id.btn_accept_changes);
//        location_spinner = findViewById(R.id.location_spinner);
//        change_photo_im = findViewById(R.id.change_photo_im);
//        btn_back = findViewById(R.id.btn_back);
//        btn_signOut = findViewById(R.id.btn_signOut);
//        lay_image = findViewById(R.id.lay_image);

        lay_image.setOnClickListener(this);
        btn_accept_changes.setOnClickListener(this);
        btn_signOut.setOnClickListener(this);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        downloadImage();

        myRefCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("first_name")){
                        edit_first_name.setText(ds.getValue().toString());
                    }
                    if (ds.getKey().equals("second_name")){
                        edit_second_name.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    locations.add(ds.getValue().toString());

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location_spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        location_spinner.setPrompt("Location");
        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView t_v = (TextView) view;
                t_l = (String) t_v.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.lay_image:
//                FileChooser();
//                break;
//            case R.id.btn_accept_changes:
//                String first_name_text = edit_first_name.getText().toString().trim();
//                String second_name_text = edit_second_name.getText().toString().trim();
//
//                myRefCurrentUser.child("first_name").setValue(first_name_text);
//                myRefCurrentUser.child("second_name").setValue(second_name_text);
//
//                myRefCurrentUser.child("location").setValue(t_l);
//
//                if (image_changed)
//                    FileUploader();
//                else {
//                    finish();
//                }
//                break;
//            case R.id.btn_signOut:
//                AuthUI.getInstance()
//                        .signOut(AccountSettings.this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                btn_signOut.setEnabled(false);
//                                showSignInOptions();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AccountSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//        }
    }

    private void downloadImage() {

        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(user.getPhoneNumber(), 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(change_photo_im);
            }
        });
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void FileChooser(){
        CropImage.activity().start(AccountSettings.this);
    }

    private void FileUploader(){
        if(imgUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mDownStorageRef.child("Images/" + removeCharAt(user.getPhoneNumber(), 0));

            ref.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE){
            btn_signOut.setEnabled(true);

            if (user == null || user.getPhoneNumber() == null) {
                showSignInOptions();
            }

            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                imgUri = result.getUri();
                Picasso.get().load(imgUri).fit().centerCrop().into(change_photo_im);

                image_changed = true;
            }
            else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Подтвердите изменения", Toast.LENGTH_SHORT).show();
    }
}

