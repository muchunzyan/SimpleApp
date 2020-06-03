package com.bifexey.simpleapp;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatFormArrayAdapter extends ArrayAdapter<ChatForm> {

    StorageReference mStorageRef;

    Context myContext;
    int myResource;

    public ChatFormArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatForm> objects) {
        super(context, resource, objects);

        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String phone_number = getItem(position).getPhone_number();
        String activity = getItem(position).getActivity();

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        //ImageView user_img = convertView.findViewById(R.id.user_img);
        //TextView tv_name = convertView.findViewById(R.id.name);
        //TextView tv_phone_number = convertView.findViewById(R.id.phone_number);
        //TextView tv_activity = convertView.findViewById(R.id.activity);

//        downloadImage(user_img, phone_number);
//        tv_name.setText(name);
//        tv_phone_number.setText(phone_number);
//        tv_activity.setText(activity);

        return convertView;
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    private void downloadImage(final ImageView img, String o_user_phone_number) {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/");
        StorageReference refUserIm = mStorageRef.child(removeCharAt(o_user_phone_number, 0));

        refUserIm.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img);
            }
        });
    }
}
