package com.bifexey.simpleapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SOSAdAdapter extends ArrayAdapter<SOSAd> {

    StorageReference mStorageRef;

    Context myContext;
    int myResource;

    public SOSAdAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SOSAd> objects) {
        super(context, resource, objects);

        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String SOS_subjectFromSpinner = getItem(position).getSOS_subjectFromSpinner();
        String SOS_locationFromSpinner = getItem(position).getSOS_locationFromSpinner();
        String SOS_subjectName = getItem(position).getSOS_subjectName();
        String SOS_subjectTopic = getItem(position).getSOS_subjectTopic();
        String SOS_year = getItem(position).getSOS_year();
        String SOS_month = getItem(position).getSOS_month();
        String SOS_day = getItem(position).getSOS_day();
        String SOS_hour = getItem(position).getSOS_hour();
        String SOS_minute = getItem(position).getSOS_minute();
        String SOS_price = getItem(position).getSOS_price();
        String SOS_phone_number = getItem(position).getSOS_phone_number();
        String SOS_key = getItem(position).getSOS_key();

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        ImageView tv_SOS_image = convertView.findViewById(R.id.my_SOS_image);
        TextView tv_SOS_subjectFromSpinner = convertView.findViewById(R.id.my_SOS_subjectFromSpinner);
        TextView tv_SOS_locationFromSpinner = convertView.findViewById(R.id.my_SOS_locationFromSpinner);
        TextView tv_SOS_subjectName = convertView.findViewById(R.id.my_SOS_subjectName);
        TextView tv_SOS_subjectTopic = convertView.findViewById(R.id.my_SOS_subjectTopic);
        TextView tv_SOS_date_time = convertView.findViewById(R.id.my_SOS_date_time);
        TextView tv_SOS_price = convertView.findViewById(R.id.my_SOS_price);
        TextView tv_SOS_key = convertView.findViewById(R.id.my_SOS_key);

        downloadImage(tv_SOS_image, SOS_phone_number);
        tv_SOS_subjectFromSpinner.setText(SOS_subjectFromSpinner);
        tv_SOS_locationFromSpinner.setText(SOS_locationFromSpinner);
        tv_SOS_subjectName.setText(SOS_subjectName);
        tv_SOS_subjectTopic.setText(SOS_subjectTopic);
        tv_SOS_date_time.setText(SOS_year + "/" + SOS_month + "/" + SOS_day + " " + SOS_hour + ":" + SOS_minute);
        tv_SOS_price.setText(SOS_price);
        tv_SOS_key.setText(SOS_key);

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

