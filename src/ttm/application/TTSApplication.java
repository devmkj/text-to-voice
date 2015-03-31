package ttm.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import ttm.engine.TTS;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <p><b>TTMApplication</b> is the main sub class of Application object.<br>
 * It is used for global variable controls.
 * </p>
 *
 * @since 1.3.15
 * Created by shibaprasad on 3/1/2015.
 */
public class TTSApplication extends Application {

    /**
     * The Visible text of paragraph that user can see. This text may be consists of many unlikely
     * char that the TTS engine can not handle. We replace the original text to a pure text that
     * TTS engine can happily handle.
     * And this Visible Text is the original Text.
     */
    public String visibleText = null;

    /**
     * This is the pure TTS engine text.
     */
    public static String pureTtsText = null;

    /**
     * The base class that abstracts all the functions or methods necessary of the TTS engine
     * and give a realistic set of method that we can use to operate the TTS engine.
     *
     * It's a global class.
     */
    public TTS ttsEngine;


    /**
     * The array of supported {@link java.util.Locale} of the device.
     */
    public ArrayList<Locale> supportedLocaleList = null;

    /**
     * The default and running locale of the application.
     */
    public Locale defaultLocale;

    /**
     * Shared preference of the application.
     */
    public SharedPreferences sharedPreferences;

    /**
     * System calls this method when the app is about to open.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //initializing the TTS class.
        ttsEngine = new TTS(this);
        //init the preference class.
        sharedPreferences = getSharedPreferences("TTS", Context.MODE_PRIVATE);

        //store the default  language code name to preference.
        String defaultLocaleValue = sharedPreferences.getString("DefaultLocale", "en");
        defaultLocale = new Locale(defaultLocaleValue);
    }

    /**
     * This boolean variable tell us or in one word inform us that, is the TTSService class
     * running in foreground or not.
     */
    public boolean isServiceRunning = false;
}
