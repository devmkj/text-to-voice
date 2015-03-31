package ttm.engine;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import ttm.application.TTSApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * TTS : text to speech.
 * Created by shibaprasad on 2/3/2015.
 */
public class TTS {

    //text to speech engine.
    public TextToSpeech textToSpeech;

    //the queue number.
    private int queueNumber = 0;
    private HashMap<Integer, String> speakMap;

    //the context.
    private Context context;

    /**
     * Public constructor for the TTS.
     * @param context the service context.
     */
    public TTS(final Context context) {
        this.context = context;

        speakMap = new HashMap<>();

        //init the text to speech.
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            //called back after finishing the initializing of text to speech.
            @Override
            public void onInit(int status) {
                //initializing was not failed.
                if (status != TextToSpeech.ERROR) {
                    try {
                        TTSApplication ttsApplication = (TTSApplication)context;
                        ttsApplication.supportedLocaleList = getSupportedLanguage();

                        //set utterance progress listener on tts engine.
                        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                                String id = utteranceId.split(":")[1];

                                String text = speakMap.get(Integer.parseInt(id));
                                Log.d(getClass().getName(), "Spoken ID : " + utteranceId  + "\n" + text );
                            }

                            @Override
                            public void onDone(String utteranceId) {

                            }

                            @Override
                            public void onError(String utteranceId) {

                            }
                        });
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });



    }

    /**
     * Speak the text.
     *
     * @param textInput the text to be spoken.
     */
    @SuppressWarnings("ConstantConditions")
    public void speakRun(String textInput) {
        //reset the queue number.
        queueNumber = 0;
        speakMap.clear();

        //the hash map for mapping.
        HashMap<String, String> map = new HashMap<>();

        if (this.textToSpeech != null) {

            //the input text is less than 200 chars.
            if (textInput.length() <= 200) {

                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ID:" + queueNumber);
                speakMap.put(queueNumber, textInput);

                int result = textToSpeech.speak(textInput, TextToSpeech.QUEUE_FLUSH, map);

                if (result == TextToSpeech.SUCCESS) {
                    this.queueNumber ++;
                }

                Log.i(getClass().getName(), "SPEAK QUEUE : " + queueNumber);

            } else {
                if (queueNumber != 0) {
                    queueNumber = 0;
                }

                String tmpText = textInput;
                while (tmpText != null && tmpText.length() > 1) {
                    String readingText = tmpText.substring(0, tmpText.indexOf('.') + 1);

                    if (readingText == null || readingText.length() < 1) {
                        readingText = tmpText.substring(0, (tmpText.length() > 200 ?
                                200 : tmpText.length()));
                    }

                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ID:" + queueNumber);
                    speakMap.put(queueNumber, readingText);

                    int result = this.textToSpeech.speak(readingText, TextToSpeech.QUEUE_ADD, map);

                    if (result == TextToSpeech.SUCCESS) {
                        this.queueNumber++;
                    }

                    Log.i(getClass().getName(), "SPEAK QUEUE : " + queueNumber + "\n" +
                            readingText);

                    try {
                        String x = tmpText.substring(readingText.length());
                        if (x.length() > 1) {
                            tmpText = x;
                        } else {
                            tmpText = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpText = null;
                    }
                }
            }
        }
    }


    public Thread thread;

    public void speak(String text) {
        thread = getSpeakThread(text);
        thread.start();
    }

    private Thread getSpeakThread(final String text) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                speakRun(text);
            }
        });
    }

    public void stop() {
        try {
            if (textToSpeech.isSpeaking()) {
                while (this.queueNumber > 0) {
                    textToSpeech.stop();
                    queueNumber--;
                    Log.i(getClass().getName(), "QUEUE NUMBER : " + queueNumber);
                }
            } else {
                this.queueNumber = 0;
                Log.i(getClass().getName(), "QUEUE NUMBER : " + queueNumber);
            }


        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    /**
     * Get the support languages by the text-to-speech engine.
     * @return the array of supported locales.
     */
    public ArrayList<Locale> getSupportedLanguage() {
        ArrayList<Locale> localeArrayList = new ArrayList<Locale>();

        Locale[] allLocales = Locale.getAvailableLocales();
        for (Locale locale : allLocales) {
            try {
                int responseCode = textToSpeech.isLanguageAvailable(locale);

                boolean hasVariant = (null != locale.getVariant() && locale.getVariant().length() > 0);
                boolean hasCountry = (null != locale.getCountry() && locale.getCountry().length() > 0);

                boolean isLocaleSupported =
                        !hasVariant && !hasCountry && responseCode == TextToSpeech.LANG_AVAILABLE ||
                                !hasVariant && hasCountry && responseCode == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                                responseCode == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;

                if (isLocaleSupported) {
                    localeArrayList.add(locale);
                }
            } catch (Exception ignored) {
            }
        }
        return localeArrayList;
    }

    /**
     * Update the language of the Text to Speech engine.
     */
    public void updateLanguage() {
        textToSpeech.setLanguage(((TTSApplication)context).defaultLocale);
    }
}
