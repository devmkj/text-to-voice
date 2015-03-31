package ttm.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import in.mathakharap.ttm.R;

/**
 * About us activity.
 * Created by shibaprasad on 3/18/2015.
 */
public class AboutActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //set the activity screen.
        setContentView(R.layout.layout_about_us);
        setUpVersionName();

        //set custom font.
        makeTextView(findViewById(R.id.group_name), 'm', null);
        makeTextView(findViewById(R.id.legal_notice), 'm', null);
        makeTextView(findViewById(R.id.show_legal_info), 'm', null);
        ((TextView) findViewById(R.id.feedback)).
                setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    /**
     * Set up the version name.
     */
    private void setUpVersionName() {
        TextView version = (TextView) findViewById(R.id.version);
        String versionInfo[] = getVersionInfo();
        version.setText(version.getText() + " " + versionInfo[1]);
        makeTextView(version, 'm', null);

        TextView version_code = (TextView) findViewById(R.id.version_code);
        version_code.setText(version_code.getText() + " " + versionInfo[0]);
        makeTextView(version_code, 'm', null);
    }

    /**
     * Set up the action bar.
     */
    @Override
    protected void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getStringResource(R.string.about_us));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Call back when Legal Info button will press.
     *
     * @param view the button view.
     */
    public void onLegalInfoButtonPress(View view) {
        startActivity(new Intent(AboutActivity.this, LegalNoticeActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.out);
    }
}
