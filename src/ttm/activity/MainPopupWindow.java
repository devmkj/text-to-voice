package ttm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import in.mathakharap.ttm.R;
import ttm.application.TTSApplication;
import ttm.adapter.LanguageListAdapter;

public abstract class MainPopupWindow implements View.OnClickListener {

    private View view;
    private PopupWindow popupWindow;
    private TextView export, language, setting, about_us;
    private Context context;

    /**
     * Public constructor.
     *
     * @param context the activity context.
     */
    public MainPopupWindow(Context context) {
        this.context = context;

        if (view == null) {
            LayoutInflater layout_inflater = LayoutInflater.from(context);
            view = layout_inflater.inflate(R.layout.layout_action_overflow, null);
        }

        //Show the popup window in the search button.
        export = (TextView) view.findViewById(R.id.export_audio);
        language = (TextView) view.findViewById(R.id.language);
        setting = (TextView) view.findViewById(R.id.setting);
        about_us = (TextView) view.findViewById(R.id.about_us);

        initOnClickListener();
        initViews(new View[]{export, language, setting, about_us});

    }

    private void initOnClickListener() {
        export.setOnClickListener(this);
        language.setOnClickListener(this);
        setting.setOnClickListener(this);
        about_us.setOnClickListener(this);
    }

    public abstract void initViews(View[] views);

    public void show(View dropView) {
        if (popupWindow == null)
            popupWindow = new PopupWindow(context);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(this.view);
        popupWindow.showAsDropDown(dropView);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == export.getId())
            exportOnClick();

        else if (id == language.getId())
            languageOnClick();

        else if (id == setting.getId())
            settingOnClick();

        else if (id == about_us.getId())
            aboutUsOnClick();
    }

    private void exportOnClick() {

    }


    /**
     * Show user the available language and give him/her the choice to select a specific language
     * for tts.
     */
    private void languageOnClick() {
        //saving the main activity context.
        final MainActivity mainActivity = (MainActivity) context;

        //show the current language of the TTS engine.
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.layout_default_language_dialog, null);
        builder.setView(view);

        //language info text view.
        final TextView languageInfo = (TextView) view.findViewById(R.id.info);
        //get the application reference object.
        final TTSApplication ttsApplication = (TTSApplication) mainActivity.getApplication();

        //get the language name and it's code.
        String name = ttsApplication.defaultLocale.getDisplayLanguage();
        String code = ttsApplication.defaultLocale.toString();
        String accent = ttsApplication.defaultLocale.getDisplayCountry();

        //set the information to the text view.
        languageInfo.setText("Language : " + name + "\n" +
                ((accent.length() > 1) ? "Language Accent/Country : " + accent + "\n" : "") +
                "Language Code : " + code);

        //button : another language.
        Button anotherLanguageButton = (Button) view.findViewById(R.id.more_language);
        anotherLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Language dialog.
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                View view = LayoutInflater.from(context).inflate(R.layout.layout_language_selcet_dialog, null);
                builder.setView(view);

                final AlertDialog dialog = builder.create();

                //the list view to show the language.
                ListView listView = (ListView) view.findViewById(R.id.list_view);
                final LanguageListAdapter languageListAdapter = new LanguageListAdapter(context, ttsApplication.supportedLocaleList);
                listView.setAdapter(languageListAdapter);

                //list view item on-click listener.
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss(); //close the dialog.
                        //save the language info to the application reference object.
                        TTSApplication ttsApplication = (TTSApplication) mainActivity.getApplication();
                        ttsApplication.defaultLocale = languageListAdapter.getData().get(position);
                        languageInfo.setText("Language : " + ttsApplication.defaultLocale.getDisplayLanguage() + "\n" +
                                ((ttsApplication.defaultLocale.getDisplayCountry().length() > 1) ? "Language Accent/Country : "
                                        + ttsApplication.defaultLocale.getDisplayCountry() + "\n" : "") +
                                "Language Code : " + ttsApplication.defaultLocale.toString());
                        ttsApplication.sharedPreferences.edit().putString("DefaultLocale",
                                ttsApplication.defaultLocale.toString()).apply();
                    }
                });

                dialog.show();
            }
        });

        final AlertDialog parentDialog = builder.create();
        parentDialog.show();
    }

    private void settingOnClick() {

    }

    private void aboutUsOnClick() {
        context.startActivity(new Intent(context, AboutActivity.class));
        ((Activity) context).overridePendingTransition(R.anim.enter, R.anim.out);
    }


}
