package io.ckl.articles.modules.read;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.ckl.articles.R;
import io.ckl.articles.api_services.ArticlesAdapter;
import io.ckl.articles.modules.base.BaseActivity;

/**
 * Created by Endy on 23/03/2017.
 */
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

    @BindView(R.id.readCheckBox)
    CheckBox readCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);

        // Set the result as First User to indicate that the "Mark as Read" was not clicked
        setResult(RESULT_FIRST_USER);

        // Set all the Widgets info
        setTitle(getIntent().getStringExtra(getResources().getString(R.string.extraLabel)));
        readTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.extraTitle)));

        // There's no need to retry to download the Image, so the OFFLINE option can be used
        Picasso.with(this).load(getIntent().getStringExtra(getResources().getString(R.string.extraImage)))
                .networkPolicy(NetworkPolicy.OFFLINE).fit().centerInside().into(readImage);

        readDate.setText(getIntent().getStringExtra(getResources().getString(R.string.extraDate)));
        readAuthor.setText(getIntent().getStringExtra(getResources().getString(R.string.extraAuthor)));
        readWebsite.setText(getIntent().getStringExtra(getResources().getString(R.string.extraWebsite)));

        readContent.setText(getIntent().getStringExtra(getResources().getString(R.string.extraContent)));
        readCheck.setChecked(getIntent().getBooleanExtra(getResources().getString(R.string.extraChecked), false));

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (Build.VERSION.SDK_INT > 21)
                    finishAfterTransition();
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region click listeners

    @OnCheckedChanged(R.id.readCheckBox)
    public void checkboxToggled (boolean isChecked) {
        // Set the info about the "Mark as Read" to be synced with the Main Screen

        // Set the checked state as an Extra to be used by the MainActivity
        Intent i = new Intent();
        i.putExtra(getResources().getString(R.string.extraChecked), isChecked);

        // Set the result as OK to indicate that the "Mark as Read" was clicked
        setResult(RESULT_OK, i);

        // Update the info at the Database
        ArticlesAdapter.updateReadAtDB(isChecked, readTitle.getText().toString());
    }

    //end region
}
