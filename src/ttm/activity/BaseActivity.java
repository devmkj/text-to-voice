package ttm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import in.mathakharap.ttm.R;

/**
 * Base Activity is the bass class.
 * Created by shibaprasad on 3/18/2015.
 */
public abstract class BaseActivity extends Activity {

    protected Context context;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setUpActionBar();
        context = getApplicationContext();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Make the text view.
     *
     * @param view  the text view
     * @param style the style name of the type font.
     * @param text  the text
     */
    protected void makeTextView(View view, char style, String text) {
        if (style == 'r')
            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf"));
        else if (style == 'm')
            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), "font/Roboto-Medium.ttf"));
        else if (style == 'b')
            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), "font/Roboto-Bold.ttf"));

        if (text != null)
            ((TextView) view).setText(text);
    }


    /**
     * Get Version information of the app.
     *
     * @return version code & name.
     */
    public String[] getVersionInfo() {
        try {
            return new String[]{
                    "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode,
                    "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName
            };
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get string resource from that given id.
     *
     * @param id the resource id.
     * @return the string.
     */
    public String getStringResource(int id) {
        return getResources().getString(id);
    }


    /**
     * Call back this method to set up the action bar title. and other
     * properties of action bar.
     */
    protected abstract void setUpActionBar();
}
