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

import io.ckl.articles.R;
import io.ckl.articles.models.Articles;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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

// Function to creates the ListView and it's content and that is called when a item is clicked
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);

            SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            TextView     titleView      = (TextView) convertView.findViewById(R.id.title);
            TextView     authorView     = (TextView) convertView.findViewById(R.id.author);
            TextView     dateView       = (TextView) convertView.findViewById(R.id.date);
            ImageView    thumbImageView = (ImageView)convertView.findViewById(R.id.article_image);
            CheckBox     checkView      = (CheckBox) convertView.findViewById(R.id.check_query);
            LinearLayout llView         = (LinearLayout) convertView.findViewById(R.id.bottomWrapper);

            // Setting all widgets values in listview
            titleView.setText(this.data.get(position).getTitle());
            dateView.setText(this.data.get(position).getDate());
            authorView.setText(this.data.get(position).getAuthors());
            Picasso.with(context).setIndicatorsEnabled(true);
            Picasso.with(context).load(this.data.get(position).getImageUrl()).fit().centerInside()
                        .into(thumbImageView);


            // Set the ClickListener for the entire Layout of the Read Checkbox to allow the user
            // to click outside the box and gets the right function
            llView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkView.setChecked(!checkView.isChecked());
                }
            });

            // The CheckedListener for the
            checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
                    Realm realm = Realm.getInstance(realmConfiguration);

                    Articles clickedArticle = realm.where(Articles.class).
                            equalTo("title", titleView.getText().toString()).findFirst();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            clickedArticle.setRead(isChecked);
                        }
                    });
                    realm.close();

                    if (isChecked)
                    {
                        titleView.setTextColor(Color.GRAY);
                        authorView.setTextColor(Color.GRAY);
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
                        authorView.setTextColor(Color.RED);
                        dateView.setTextColor(Color.BLACK);
                        thumbImageView.setColorFilter(null);
                        thumbImageView.setImageAlpha(255);
                    }
                }
            });

            // If the article was marked as "Read", perform a click to the show as read (gray)
            // No duplicate calls realted in tests
            if (this.data.get(position).getRead()) {
                checkView.performClick();
            }
        }
        return convertView;
    }

    //return the SwipeLayout resource id in the layout. -- Default from the SwipeLayout library
    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipeLayout;
    }
}