package com.example.myapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.helpers.ImageHelper;
import com.example.myapp.models.InstagramPhoto;
import com.example.myapp.myinstagram.R;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by kemo on 9/13/14.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //take the data source (ie. 0)

        InstagramPhoto photo = getItem(position);

        // get the data item

        // chck if we are using a recycle view

        if (convertView == null) {
            // don't attach until it is told
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvProfile = (TextView) convertView.findViewById(R.id.tvProfile);
        TextView tvProfileName = (TextView) convertView.findViewById(R.id.tvProfileName);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgProfile);

        // lookup the subview within the template

        tvCaption.setText(photo.caption);
        String heart = "&#9825";
        //tvLikes.setText(Html.fromHtml(heart+ String.valueOf(photo.likes_count) + " likes"));
        tvLikes.setText(Html.fromHtml(heart) + String.valueOf(photo.likes_count) + " likes");
        tvProfile.setText(photo.username);
        tvProfileName.setText(photo.username);


        // set the image height before loading
        imgPhoto.getLayoutParams().height = photo.imageHeight;

        // remove the old and wait the new photo
        // reset the image from the recycled view

        imgPhoto.setImageResource(0);

        // Ask for the photot to be added to the imageView based on the photo url
        // Background: send a network request to the url, download the image bytes, convert into the bitmap
        // maybe resize the image

        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
        if (null != photo.profile_picture) {
            imgProfile.setImageResource(0);
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(0)
                    .cornerRadiusDp(30)
                    .oval(false)
                    .build();

            Picasso.with(getContext())
                    .load(photo.profile_picture)
                    .transform(transformation)
                    .into(imgProfile);
        }
        return convertView;

        //Populate the subviews (textfields, imageview ) with the correct data



        // return the view for that data item
    }

    // getView method (int position)
    // Default, taks the model (InstagramPhoto) toString()

}
