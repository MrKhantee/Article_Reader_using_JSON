package io.ckl.articles.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.ckl.articles.R;

/**
 * Created by Endy on 18/03/2017.
 */

public class ArticlesAdapter extends ArraySwipeAdapter<Articles> {

    private Context context;
    private ArrayList<Articles> data;
    private static int counterList = 0;
    private static int sizeListView = 150;

    private static LayoutInflater inflater;

    public ArticlesAdapter(Context context, ArrayList<Articles> d)
    {
        super(context, 0, d);

        this.data = d;
        this.context        = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null, true);

            SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            TextView titleView          = (TextView) convertView.findViewById(R.id.title);
            TextView webView            = (TextView) convertView.findViewById(R.id.website);
            TextView dateView           = (TextView) convertView.findViewById(R.id.date);
            ImageView thumb_imageView   = (ImageView)convertView.findViewById(R.id.article_image);

            // Setting all values in listview
            titleView.setText(this.data.get(counterList).getTitle());
            webView.setText(this.data.get(counterList).getWebsite());
            dateView.setText(this.data.get(counterList).getDate());
            Log.d("PicassoURL", "Message : " + this.data.get(counterList).getImageUrl());
            Picasso.with(context).load(this.data.get(counterList).getImageUrl()).
                    resize(sizeListView, sizeListView).centerCrop().into(thumb_imageView);

            if (counterList < 5) {
                counterList++;
            }
        }

        return convertView;
    }

    //return the SwipeLayout resource id in the layout.
    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipe_layout;
    }
}