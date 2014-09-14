package com.example.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.models.InstagramPhoto;
import com.example.myapp.myinstagram.R;
import com.squareup.picasso.Picasso;

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
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);

        // lookup the subview within the template

        tvCaption.setText(photo.caption);

        // set the image height before loading
        imgPhoto.getLayoutParams().height = photo.imageHeight;

        // remove the old and wait the new photo
        // reset the image from the recycled view

        imgPhoto.setImageResource(0);

        // Ask for the photot to be added to the imageView based on the photo url
        // Background: send a network request to the url, download the image bytes, convert into the bitmap
        // maybe resize the image

        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
        return convertView;

        //Populate the subviews (textfields, imageview ) with the correct data



        // return the view for that data item
    }

    // getView method (int position)
    // Default, taks the model (InstagramPhoto) toString()

}
