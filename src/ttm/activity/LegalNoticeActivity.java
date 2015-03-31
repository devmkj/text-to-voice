package ttm.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import in.mathakharap.ttm.R;
import ttm.activity.BaseActivity;

/**
 * Legal notice Activity is responsible for showing the legal information
 * about the app.
 * Created by shibaprasad on 3/15/2015.
 */
public class LegalNoticeActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //set the activity screen.
        setContentView(R.layout.layout_legal_notice);

        ((TextView) findViewById(R.id.android_asset_studio))
                .setText(Html.fromHtml(androidAssetStudio));
        ((TextView) findViewById(R.id.android_asset_studio)).
                setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    /**
     * Set up the action bar.
     */
    @Override
    protected void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Legal Notice");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private String androidAssetStudio =
            "RTM(Read To Me) is built with some graphical assets that were created by " +
                    "the <b>Android Asset Studio</b>." +
                    "<br>" +
                    "<a href=\"http://romannurik.github.io/AndroidAssetStudio/\">Android Asset Studio</a> " +
                    "is licensed under <a href=\"http://creativecommons.org/licenses/by/3.0/\">CC BY 3.0</a>" +
                    "<br>";

}
