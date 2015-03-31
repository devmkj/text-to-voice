package ttm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import in.mathakharap.ttm.R;
import ttm.application.TTSApplication;
import ttm.service.TTSService;

/**
 * Main Activity.
 * @author shiba prasad.
 */
public class MainActivity extends Activity {

    //the edit text input
    private EditText editText;
    private TextView paragraph;
    private ScrollView scrollView;

    //the activity context.
    private Context context;

    private MainPopupWindow menuPopupWindow;

    //application reference.
    private TTSApplication ttsApplication;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get a reference of this activity.
        context = MainActivity.this;
        ttsApplication = (TTSApplication)getApplication();

        //set the activity layout.
        setContentView(R.layout.layout_main_screen);

        //set the exit text.
        editText = (EditText) findViewById(R.id.edit_text);
        paragraph = (TextView) findViewById(R.id.paragraph);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        //init all the view
        initTextView();
    }

    @Override
    public void onDestroy() {
        ttsApplication.visibleText = editText.getText().toString();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.setText(ttsApplication.visibleText);
        if (ttsApplication.ttsEngine.textToSpeech.isSpeaking()) {
            editText.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            paragraph.setText(editText.getText().toString());
        }
    }

    /**
     * System calls this method for inflating the action bar button menu.
     *
     * @param menu the menu which to be inflated on.
     * @return result of the inflating operation.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set the action bar buttons.
        setActionButton(menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Android os calls this onOptionItemSelected() method, when an
     * action menu button is selected from the activity's action bar.
     *
     * @param menuItem action menu_item button.
     * @return true or false depending of the result.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        exitActivity();
    }

    /**
     * Create the menu of the action and inflated it.
     *
     * @param menu menu
     */
    private void setActionButton(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_bar_menu, menu);
    }

    /**
     * The back press status int.
     */
    private int isBackPress = 0;

    /**
     * This method exits this activity for double back press.
     */
    private void exitActivity() {
        if (isBackPress == 0) {
            Toast.makeText(context, "Press back once more to exit", Toast.LENGTH_SHORT).show();
            isBackPress = 1;
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long time) {
                }

                @Override
                public void onFinish() {
                    isBackPress = 0;
                }
            }.start();
        } else if (isBackPress == 1) {
            isBackPress = 0;
            finish();
        }
    }

    /**
     * Make the text view.
     *
     * @param view  the text view
     * @param style the style name of the type font.
     * @param text  the text
     */
    private void makeTextView(View view, char style, String text) {
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
     * Initialize all the text view.
     */
    private void initTextView() {
        makeTextView(findViewById(R.id.play), 'm', null);
        makeTextView(findViewById(R.id.stop), 'm', null);
        makeTextView(findViewById(R.id.clear), 'm', null);

        makeTextView(findViewById(R.id.edit_text), 'm', null);
        makeTextView(findViewById(R.id.paragraph), 'm', null);
    }

    /**
     * This method will be called when the user will press the play button.
     *
     * @param view the play button.
     */
    public void onPlay(View view) {
        String text = editText.getText().toString();

        if (text.length() > 1) {
            if (ttsApplication.ttsEngine.textToSpeech.isSpeaking()) {
                Toast.makeText(context, "TTS is running..", Toast.LENGTH_SHORT).show();

            } else {
                String pureText = editText.getText().toString().replace('\n', ' ');

                //close the input method manager.
                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                //speak the pure text.
                Intent intent = new Intent(context, TTSService.class);
                intent.setAction("INTENT_START_SERVICE");
                intent.putExtra(TTSService.Key.COMMAND_TYPE, TTSService.Key.INTENT_PLAY);
                TTSApplication.pureTtsText = pureText;
                startService(intent);

                //ui staff.
                editText.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                paragraph.setText(text);

                //save the text to application.
                ttsApplication.visibleText = editText.getText().toString();
            }
        } else {

            //vibrate and make a text.
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(20);
            Toast.makeText(context, "Write something to here", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will be called when the user will press the stop button.
     * * @param view the stop button.
     */
    public void onStop(View view) {
        //stop the tts.
        Intent intent = new Intent(context, TTSService.class);
        intent.setAction("INTENT_START_SERVICE");
        intent.putExtra(TTSService.Key.COMMAND_TYPE, TTSService.Key.INTENT_STOP);
        startService(intent);

        editText.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        paragraph.setText("");
        //save the text to application.
        ttsApplication.visibleText = editText.getText().toString();
    }

    /**
     * This method will be called when the user will press the clear button.
     *
     * @param view the clear button.
     */
    public void onClear(View view) {
        //stop the tts.
        Intent intent = new Intent(context, TTSService.class);
        intent.setAction("INTENT_START_SERVICE");
        intent.putExtra(TTSService.Key.COMMAND_TYPE, TTSService.Key.INTENT_STOP);
        startService(intent);

        this.editText.setText("");

        editText.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        paragraph.setText("");

        //save the text to application.
        ttsApplication.visibleText = editText.getText().toString();

    }

    /**
     * Show the menu option pop up.
     *
     * @param menuItem menu Item.
     */
    public void onOptionMenu(MenuItem menuItem) {
        if (menuPopupWindow == null)
            menuPopupWindow = new MainPopupWindow(context) {
                @Override
                public void initViews(View[] views) {
                    for (View v : views) {
                        makeTextView(v, 'r', null);
                    }
                }
            };

        menuPopupWindow.show(findViewById(R.id.option_menu));
    }

    /**
     * This method is called when user activate or deactivate the {@link ttm.service.TTSService} class.
     *
     * @param item the item menu.
     */
    public void onServiceActivate(MenuItem item) {
        Intent intent = new Intent(context, TTSService.class);
        intent.setAction("INTENT_START_SERVICE");
        if (((TTSApplication)getApplication()).isServiceRunning) { //service is running.
            intent.putExtra(TTSService.Key.COMMAND_TYPE, TTSService.Key.INTENT_STOP_FOREGROUND );
            startService(intent);
        } else {
            intent.putExtra(TTSService.Key.COMMAND_TYPE, TTSService.Key.INTENT_START_FOREGROUND );
            startService(intent);
        }

    }
}
