package io.ckl.articles.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.ckl.articles.R;

/**
 * Created by Endy on 18/03/2017.
 */

public class ArticlesAdapter extends ArrayAdapter<Articles> {

    private Context context;
    ArrayList<Articles> data;

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
        }

        TextView titleView          = (TextView)convertView.findViewById(R.id.title);
        TextView webView            = (TextView)convertView.findViewById(R.id.website);
        TextView dateView           = (TextView)convertView.findViewById(R.id.date);
        ImageView thumb_imageView   = (ImageView)convertView.findViewById(R.id.article_image);

        // Setting all values in listview
        titleView.setText(this.data.get(0).title);
        webView.setText(this.data.get(0).getWebsite());
        dateView.setText(this.data.get(0).getDate());
        thumb_imageView.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }
}