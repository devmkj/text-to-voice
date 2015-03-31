package ttm.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import in.mathakharap.ttm.R;
import ttm.activity.MainActivity;
import ttm.application.TTSApplication;
import ttm.engine.TTS;

public class TTSService extends Service {

    //tts binder.
    private TTsIBinder ttsBinder;

    //tts class.
    private TTS tts;

    /**
     * System call this method the binder is ready to deploy the service.
     *
     * @param intent the intent.
     * @return the service binder stub class.
     */
    @Override
    public IBinder onBind(Intent intent) {
        if (ttsBinder != null) {
            return ttsBinder;
        } else {
            return null;
        }
    }


    /**
     * System call this method when the Service is created for the first time.
     */
    @Override
    public void onCreate() {
        ttsBinder = new TTsIBinder(this);
        tts = ((TTSApplication) getApplication()).ttsEngine;
    }


    /**
     * Speak the given text.
     * @param text the text to be spoken.
     */
    public void speak(String text) {
        if (tts == null) {
            tts = ((TTSApplication)getApplication()).ttsEngine;
        }
        //update the text before play the speak() method.
        tts.updateLanguage();
        tts.speak(text);
    }


    /**
     * Stop the tts engine.
     */
    public void stop() {
        if (tts != null)
            tts.stop();
    }

    /**
     * Check if the tts engine is running or not.
     * @return result true if tts engine is running, false otherwise.
     */
    public boolean isSpeaking() {
        return tts != null && tts.textToSpeech != null && tts.textToSpeech.isSpeaking();
    }


    //private notification object to show the foreground notification.
    private Notification notification;


    /**
     * Set the foreground notification.
     */
    public void setUpNotification() {
        if (notification == null) {
            notification = new Notification();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.icon = R.drawable.ic_notification;
            notification.when = System.currentTimeMillis();
            notification.setLatestEventInfo(this, "RTM is Running.",
                    "Press here to listen the text. ", getPendingIntent());
        }
    }


    /**
     * Get the notification intent.
     *
     * @return PendingIntent.
     */
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }


    /**
     * System call back this method when any application components send any start command to this
     * service class.
     *
     * @param intent  the intent which the application component has sent.
     * @param flag    the flag with the intent.
     * @param startId the start id.
     * @return the return code {@see android.app.Service.}
     */
    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        super.onStartCommand(intent, flag, startId);

        try {
            if (intent == null)
                return START_STICKY;

            int command = intent.getIntExtra(Key.COMMAND_TYPE, Key.INTENT_STOP_FOREGROUND);
            if (command == Key.INTENT_START_FOREGROUND) {
                startForeground();

            } else if (command == Key.INTENT_STOP_FOREGROUND) {
                stopForeground();

            } else if (command == Key.INTENT_PLAY) {
                speak(TTSApplication.pureTtsText);

            } else if (command == Key.INTENT_STOP) {
                stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Service.START_STICKY;
    }


    /**
     * Start the service as foreground service.
     */
    public void startForeground() {
        setUpNotification();
        startForeground(44444, notification);
        //set the application global variable that the service is now running foreground.
        ((TTSApplication) getApplication()).isServiceRunning = true;
    }


    /**
     * Stop the service from going to foreground.
     */
    public void stopForeground() {
        stopForeground(true);
        //set the application global variable that the service is now not running as foreground.
        ((TTSApplication) getApplication()).isServiceRunning = false;
    }

    /**
     * The public Key class the provide the necessary key to service commands.
     */
    public static class Key {
        public static final String COMMAND_TYPE = "COMMAND_TYPE";
        public static final int INTENT_START_FOREGROUND = 1;
        public static final int INTENT_STOP_FOREGROUND = 2;

        public static final int INTENT_PLAY = 23;
        public static final int INTENT_STOP = 24;
    }
}
