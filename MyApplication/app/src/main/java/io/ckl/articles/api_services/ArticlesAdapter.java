package io.ckl.articles.api_services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

/**
 * Created by Endy on 18/03/2017.
 */
// Articles Adapter to be used with ListView
public class ArticlesAdapter extends ArraySwipeAdapter<Articles> {

    private Context context;
    private ArrayList<Articles> data;

    private static LayoutInflater inflater;

    public ArticlesAdapter(Context context, ArrayList<Articles> d)
    {
        super(context, R.layout.list_row, d);

        this.data    = d;
        this.context = context;
    }

    public static class ArticlesViewHolder {
        SwipeLayout  swipeLayout;
        TextView     titleView;
        TextView     authorView;
        TextView     dateView;
        ImageView    thumbImageView;
        CheckBox     checkView;
        LinearLayout llView;
    }

// Function to creates the ListView and it's content and that is called when a item is clicked
    public View getView(int position, View convertView, ViewGroup parent) {

        final ArticlesViewHolder articlesItem;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);

            articlesItem = new ArticlesViewHolder();

            articlesItem.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            articlesItem.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            articlesItem.titleView      = (TextView) convertView.findViewById(R.id.listTitle);
            articlesItem.authorView     = (TextView) convertView.findViewById(R.id.ListAuthor);
            articlesItem.dateView       = (TextView) convertView.findViewById(R.id.listDate);
            articlesItem.thumbImageView = (ImageView)convertView.findViewById(R.id.listImage);
            articlesItem.checkView      = (CheckBox) convertView.findViewById(R.id.listCheckBox);
            articlesItem.llView         = (LinearLayout) convertView.findViewById(R.id.bottomWrapper);

            convertView.setTag(articlesItem);
        }
        else {
            articlesItem = (ArticlesViewHolder) convertView.getTag();
        }

        // Setting all widgets values in ListView
        articlesItem.titleView.setText(this.data.get(position).getTitle());
        articlesItem.dateView.setText(this.data.get(position).getDate());
        articlesItem.authorView.setText(this.data.get(position).getAuthors());
        Picasso.with(context).load(this.data.get(position).getImageUrl()).fit().centerInside()
                .into(articlesItem.thumbImageView);

        // Set the ClickListener for the entire Layout of the Read Checkbox to allow the user
        // to click outside the box and gets the right function
        articlesItem.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlesItem.checkView.setChecked(!articlesItem.checkView.isChecked());
            }
        });

        // The "checked listener" for the CheckBox
        articlesItem.checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the info at the Database
                updateReadAtDB(isChecked, articlesItem.titleView.getText().toString());

                if (isChecked)
                {
                    // If Read checked, turn the item gray
                    articlesItem.titleView.setTextColor(Color.GRAY);
                    articlesItem.authorView.setTextColor(Color.GRAY);
                    articlesItem.dateView.setTextColor(Color.GRAY);

                    // Apply grayscale filter to the Image
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    articlesItem.thumbImageView.setColorFilter(filter);
                    articlesItem.thumbImageView.setImageAlpha(128);
                }
                else
                {
                    // If Read unchecked, return the item normal colors
                    articlesItem.titleView.setTextColor(Color.BLACK);
                    articlesItem.authorView.setTextColor(Color.RED);
                    articlesItem.dateView.setTextColor(Color.BLACK);

                    // Return the Image color
                    articlesItem.thumbImageView.setColorFilter(null);
                    articlesItem.thumbImageView.setImageAlpha(255);
                }

                articlesItem.swipeLayout.close();
            }
        });

        // If the article was already marked as "Read", set checked to show as read (gray)
        if (this.data.get(position).getRead()) {
            articlesItem.checkView.setChecked(true);
        }

        return convertView;
    }


    public static void updateReadAtDB (boolean checked, String title)
    {
        // Get the Database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(realmConfiguration);

        // Update the info at the Database
        Articles clickedArticle = realm.where(Articles.class).equalTo("title", title).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                clickedArticle.setRead(checked);
            }
        });
        realm.close();
    }

    //return the SwipeLayout resource id in the layout. -- Default from the SwipeLayout library
    @Override
    public int getSwipeLayoutResourceId(int position){
        return R.id.swipeLayout;
    }
}