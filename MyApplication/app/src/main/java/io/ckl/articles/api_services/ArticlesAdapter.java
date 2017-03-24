package io.ckl.articles.api_services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.ckl.articles.R;
import io.ckl.articles.models.Articles;

/**
 * Created by Endy on 18/03/2017.
 */

public class ArticlesAdapter extends ArraySwipeAdapter<Articles> {

    private Context context;
    private ArrayList<Articles> data;

    private static LayoutInflater inflater;

    public ArticlesAdapter(Context context, ArrayList<Articles> d)
    {
        super(context, 0, d);

        this.data    = d;
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);

            SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            TextView titleView          = (TextView) convertView.findViewById(R.id.title);
            TextView webView            = (TextView) convertView.findViewById(R.id.website);
            TextView dateView           = (TextView) convertView.findViewById(R.id.date);
            ImageView thumbImageView    = (ImageView)convertView.findViewById(R.id.article_image);
            CheckBox  checkView         = (CheckBox) convertView.findViewById(R.id.check_query);
            LinearLayout llView         = (LinearLayout) convertView.findViewById(R.id.bottomWrapper);

    // This can be modified when develop the DataBase
            // Setting all values in listview
            titleView.setText(this.data.get(position).getTitle());
            dateView.setText(this.data.get(position).getDate());
            webView.setText(this.data.get(position).getWebsite());
            Picasso.with(context).load(this.data.get(position).getImageUrl()).fit().centerInside()
                    .into(thumbImageView);

            llView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkView.setChecked(!checkView.isChecked());
                }
            });

            checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        titleView.setTextColor(Color.GRAY);
                        webView.setTextColor(Color.GRAY);
                        dateView.setTextColor(Color.GRAY);

                        // Apply grayscale filter
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        thumbImageView.setColorFilter(filter);
                        thumbImageView.setImageAlpha(128);
                    }
                    else
                    {
                        titleView.setTextColor(Color.BLACK);
                        webView.setTextColor(Color.RED);
                        dateView.setTextColor(Color.BLACK);
                        thumbImageView.setColorFilter(null);
                        thumbImageView.setImageAlpha(255);
                    }

                }
            });
        }

        return convertView;
    }

    //return the SwipeLayout resource id in the layout.
    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipeLayout;
    }


}