package io.ckl.articles.modules.read;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ckl.articles.R;
import io.ckl.articles.modules.base.BaseActivity;

public class ReadActivity extends BaseActivity implements ReadInterfaces.View {

    ReadInterfaces.Presenter readPresenter = new ReadPresenter(this);

    @BindView(R.id.readTitle)
    TextView readTitle;
    @BindView(R.id.readImage)
    ImageView readImage;

    @BindView(R.id.readDate)
    TextView readDate;
    @BindView(R.id.readWebsite)
    TextView readWebsite;
    @BindView(R.id.readAuthor)
    TextView readAuthor;

    @BindView(R.id.readContent)
    TextView readContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);

        setTitle(getIntent().getStringExtra("Label"));
        readTitle.setText(getIntent().getStringExtra("Title"));

        Picasso.with(this).load(getIntent().getStringExtra("Image")).fit().centerInside()
                .into(readImage);

        readDate.setText(getIntent().getStringExtra("Date"));
        readAuthor.setText(getIntent().getStringExtra("Author"));
        readWebsite.setText(getIntent().getStringExtra("Website"));

        readContent.setText(getIntent().getStringExtra("Content"));

        readPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        if (readPresenter != null) {
          readPresenter.onDestroy();
        }
        readPresenter = null;

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    //region ReadInterfaces.View


    //end region


    //region click listeners


    //end region
}
