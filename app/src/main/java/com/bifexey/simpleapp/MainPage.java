package com.bifexey.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bifexey.simpleapp.ui.SOS.SOS_Fragment;
import com.bifexey.simpleapp.ui.account.AccountStudentFragment;
import com.bifexey.simpleapp.ui.chat.ChatFragment;
import com.bifexey.simpleapp.ui.search.SearchFragment;
import com.bifexey.simpleapp.ui.teacherAccount.AccountTeacherFragment;
import com.bifexey.simpleapp.ui.teacherFilterSearch.TeacherSearchFilterFragment;
import com.bifexey.simpleapp.ui.teacherSearch.TeacherSearchFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefUsers = myRef.child("users");
    DatabaseReference myRefCurrentUser;

    List<AuthUI.IdpConfig> providers;

    private static final int MY_REQUEST_CODE = 7117;
    int accountId, SOSId, SearchId, ChatId;
    boolean is_filtered = false;
    String is_user = "";
    String activity;

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);

        if (user == null) {
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build()
            );

            showSignInOptions();
        }
        else {
            accountId = navView.getMenu().getItem(3).getItemId();
            SOSId = navView.getMenu().getItem(0).getItemId();
            SearchId = navView.getMenu().getItem(1).getItemId();
            ChatId = navView.getMenu().getItem(2).getItemId();

            myRefCurrentUser = myRefUsers.child(user.getPhoneNumber());

            myRefUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equals(user.getPhoneNumber())){
                                is_user = user.getPhoneNumber();
                            }
                        }
                        if (is_user.equals("")) {
                            Intent intent = new Intent(MainPage.this, Registration.class);
                            intent.putExtra("user_phone_number", user.getPhoneNumber());
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(MainPage.this, Registration.class);
                        intent.putExtra("user_phone_number", user.getPhoneNumber());
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            myRefCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals("activity")){
                            if (ds.getValue().toString().equals("teacher")){
                                MenuItem sos = navView.getMenu().getItem(0);
                                sos.setVisible(false);
                                activity = "teacher";
                                loadFragment(new AccountTeacherFragment());
                            }
                            else if (ds.getValue().toString().equals("student")){
                                MenuItem sos = navView.getMenu().getItem(0);
                                sos.setVisible(true);
                                activity = "student";
                                loadFragment(new AccountStudentFragment());
                            }
                        }
                    }

                    if (getIntent().getStringExtra("back_from_chat") != null) {
                        if (getIntent().getStringExtra("back_from_chat").equals("True")) {
                            navView.setSelectedItemId(ChatId);
                            getIntent().removeExtra("back_from_chat");
                        }
                    }
                    else if (activity != null) {
                        if (!Integer.toString(navView.getSelectedItemId()).equals(Integer.toString(accountId))) {
                            if (activity.equals("student")) {
                                navView.setSelectedItemId(SOSId);
                            } else if (activity.equals("teacher")) {
                                navView.setSelectedItemId(SearchId);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (getIntent().getStringExtra("subject") != null && !getIntent().getStringExtra("subject").equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("subject", getIntent().getStringExtra("subject"));
                bundle.putBoolean("to_me", getIntent().getBooleanExtra("to_me", false));
                bundle.putBoolean("to_student", getIntent().getBooleanExtra("to_student", false));
                bundle.putBoolean("online", getIntent().getBooleanExtra("online", false));
                bundle.putBoolean("university", getIntent().getBooleanExtra("university", false));
                bundle.putBoolean("cafe", getIntent().getBooleanExtra("cafe", false));
                bundle.putBoolean("library", getIntent().getBooleanExtra("library", false));
                bundle.putBoolean("co_working", getIntent().getBooleanExtra("co_working", false));
                bundle.putString("price_from", getIntent().getStringExtra("price_from"));
                bundle.putString("price_to", getIntent().getStringExtra("price_to"));
                TeacherSearchFilterFragment tsf = new TeacherSearchFilterFragment();
                tsf.setArguments(bundle);

                is_filtered = true;
            }
        }
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();

            return true;
        }
        return false;
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        if (activity != null) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_SOS:
                    fragment = new SOS_Fragment();
                    break;
                case R.id.navigation_search:
                    if (activity.equals("teacher"))
                        if (is_filtered)
                            fragment = new TeacherSearchFilterFragment();
                        else
                            fragment = new TeacherSearchFragment();
                    else if (activity.equals("student"))
                        fragment = new SearchFragment();
                    break;
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    break;
                case R.id.navigation_account:
                    if (activity.equals("teacher"))
                        fragment = new AccountTeacherFragment();
                    if (activity.equals("student"))
                        fragment = new AccountStudentFragment();
                    break;
            }
        }

        return loadFragment(fragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE){
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
