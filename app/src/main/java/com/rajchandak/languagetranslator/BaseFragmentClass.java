package com.rajchandak.languagetranslator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.SweetSheet;
import com.mingle.sweetpick.ViewPagerDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

//
// Created by Raj Chandak on 23-02-2018.
//

// This class creates a base fragment that will be inherited by all fragments of the viewpager.

public class BaseFragmentClass extends Fragment implements TextToSpeech.OnInitListener{

    protected SweetSheet sweetSheet;
    protected RelativeLayout relativeLayout;
    protected Button translateButton;
    protected ImageView convertFrom;
    protected ImageView convertTo;
    protected int positionFrom = 0;
    protected int positionTo = 1;
    protected TextView convertFromText;
    protected TextView convertToText;
    protected int imageClicked = 0; // 0 : convertFrom ; 1 : convertTo
    protected int images[] = {R.drawable.india,R.drawable.usa,R.drawable.england,R.drawable.germany,R.drawable.france,R.drawable.spain,R.drawable.italy,R.drawable.china,R.drawable.japan,R.drawable.korea,R.drawable.australia,R.drawable.portugal,R.drawable.russia,R.drawable.denmark,R.drawable.netherlands,R.drawable.indonesia,R.drawable.greece,R.drawable.bangladesh,R.drawable.hungary,R.drawable.turkey,R.drawable.sweden,R.drawable.vietnam,R.drawable.czech,R.drawable.thailand};
    protected String spokenText = "";
    // ToDO : Enter Your Cloud Translate API Key Here.
    protected static final String API_KEY = "-------YOUR API KEY-------"; // Google Translate API Key. Get yours at https://cloud.google.com/translate/
    protected TextToSpeech t1;
    protected Locale languages[] = {new Locale("hi"), Locale.US, Locale.UK,Locale.GERMAN,Locale.FRENCH,new Locale("es"),Locale.ITALIAN,Locale.CHINESE,Locale.JAPANESE,Locale.KOREAN,new Locale("en-au"),new Locale("pt"),new Locale("ru"),new Locale("da"),new Locale("nl"),new Locale("id"),new Locale("el"),new Locale("bn"),new Locale("hu"),new Locale("tr"),new Locale("sv"),new Locale("vi"),new Locale("cs"),new Locale("th")};
    protected String languageCodes[] = {"hi","en","en","de","fr","es","it","zh-TW","ja","ko","en","pt","ru","da","nl","id","el","bn","hu","tr","sv","vi","cs","th"};
    protected ProgressDialog pDialog;
    protected final int REQ_CODE_SPEECH_INPUT = 100;
    protected String translatedToLanguage = "English";
    protected String translatedFromLanguage = "Hindi";
    protected Locale[] locales;
    protected String[] languageArray;
    protected List<String> languagesList;
    int speechRate;
    protected int pitchrate;
    SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.base_fragment, container, false);
        relativeLayout = view.findViewById(R.id.rl);

        // Getting available languages on user's device.
        locales = Locale.getAvailableLocales();
        languageArray = new String[locales.length];
        for(int i=0; i<locales.length ;i++)
        {
            languageArray[i] = locales[i].toString();
        }

        languagesList = Arrays.asList(languageArray);

        // Setup TTS
        t1 = new TextToSpeech(getActivity().getApplicationContext(), this);

        prefs = getActivity().getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);

        convertFromText = view.findViewById(R.id.convert_from_text);
        convertToText = view.findViewById(R.id.convert_to_text);

        // Setup translate button
        translateButton = view.findViewById(R.id.voice_to_voice_translate_button);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    setupImages(view);
                }
            });
        }
        else
        {
            setupImages(view);
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setupImages(View view){
        // Setup convert from image
        convertFrom = view.findViewById(R.id.convert_from_image);
        convertFrom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                convertFromImageClicked();
            }
        });

        // Setup convert to image
        convertTo = view.findViewById(R.id.convert_to_image);
        convertTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                convertToImageClicked();
            }
        });

    }

    // "Convert To" image is clicked
    public void convertToImageClicked() {
        convertFrom.setClickable(false);
        convertTo.setClickable(false);
        sweetSheet = new SweetSheet(relativeLayout); //Create new bottomsheet. Library used - https://github.com/zzz40500/AndroidSweetSheet
        imageClicked = 1;
        setupViewpager(); //Setup bottomsheet
        sweetSheet.toggle();
    }

    // "Convert From" image is clicked
    public void convertFromImageClicked() {
        convertFrom.setClickable(false);
        convertTo.setClickable(false);
        sweetSheet = new SweetSheet(relativeLayout); //Create new bottomsheet. Library used - https://github.com/zzz40500/AndroidSweetSheet
        imageClicked = 0;
        setupViewpager();
        sweetSheet.toggle();
    }

    // setup TTS
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = t1.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("errortts", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    // This method speaks out the text sent to it in the set language.
    public void speakOut(String text) {
        t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    // Bottomsheet viewpager - 24 items currently included. Each item represents a language.
    public void setupViewpager() {

        sweetSheet.setMenuList(R.menu.menu_sweet); //see menu_sweet.xml for all the included languages.
        sweetSheet.setDelegate(new ViewPagerDelegate());
        sweetSheet.setBackgroundEffect(new BlurEffect(13));
        sweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity) {

                if (imageClicked == 0)     // "Convert From" image is clicked. Pro devs can use enums instead of the imageClicked integer.
                {
                    positionFrom = position;
                    convertFromText.setText(menuEntity.title.toString()); //Set language to the selected language
                    convertFrom.setImageResource(images[position]); //Set image to the selected country flag
                    translatedFromLanguage = menuEntity.title.toString(); //Store the value of the language selected
                }
                else if(imageClicked == 1)     // "Convert From" image is clicked.
                {
                    positionTo = position;
                    convertToText.setText(menuEntity.title.toString());
                    convertTo.setImageResource(images[position]);
                    translatedToLanguage = menuEntity.title.toString();
                }
                return true;
            }
        });
        //The sweetsheet library has a known bug. When the user taps on the image that opens the bottomsheet multiple times,
        //the app crashes due to this exception - Cannot start this animator on a detached view! To mitigate this, we disable the setClickable property of the images of 1500 miiseconds.
        // However, This does not eliminate the crash.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                convertFrom.setClickable(true);
                convertTo.setClickable(true);
            }
        }, 1500);


    }

    // Ask for user's speech input when "Speak" button is pressed
    public void askSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languages[positionFrom]);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ignored) {

        }
    }

    //Get user's spoken words in text
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    spokenText = result.get(0);
                    translateText();
                }
                break;
            }

        }
    }

    public void translateText(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    //This method is used when the translation is from Voice To Text or from Text To Text.
    @SuppressLint("StaticFieldLeak")
    public void translateToTextAsyncTask(final String textToBeTranslated){
        final Handler textViewHandler = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(API_KEY)
                        .build();
                Translate translate = options.getService();

                // Google Translate API used here
                final Translation translation =
                        translate.translate(textToBeTranslated,
                                Translate.TranslateOption.targetLanguage(languageCodes[positionTo])); //Set target language for translation.
                Log.d("textToBeTranslated", "doInBackground: " + textToBeTranslated);
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        final String finalTranslatedString = translation.getTranslatedText().replaceAll("&#39;","'");
                        // Create dialog that displays the text with three buttons - Copy, Listen and Ok. Library used - https://github.com/javiersantos/MaterialStyledDialogs
                        final MaterialStyledDialog.Builder dialogHeader = new MaterialStyledDialog.Builder(getActivity())
                                .setHeaderDrawable(R.drawable.header)
                                .setIcon(images[positionTo])
                                .withDialogAnimation(false)
                                .setTitle("Translated to " + translatedToLanguage)
                                .setDescription(finalTranslatedString)
                                .setPositiveText("Copy")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("copytext", finalTranslatedString);
                                        assert clipboard != null;
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(getActivity().getApplicationContext(),"Copied To Clipboard.",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeText("Listen")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (languagesList.contains(languageCodes[positionTo]))
                                        {
                                            speechRate = prefs.getInt("speechrate", 0); //0 is the default value. Retrieve value from stored preferences.
                                            pitchrate = prefs.getInt("pitchrate", 0); //0 is the default value. Retrieve value from stored preferences.
                                            t1.setSpeechRate((float)speechRate/50); // speechRate is stored in integer value from 1 to 100. setPitch() accepts value in terms of float. default is 1.0. In this app, max can be 2.0
                                            t1.setPitch((float)pitchrate/50); // pitchRate is stored in integer value from 1 to 100. setPitch() accepts value in terms of float. default is 1.0. In this app, max can be 2.0
                                            t1.setLanguage(new Locale(languageCodes[positionTo]));
                                            speakOut(finalTranslatedString);
                                        }
                                        else
                                            Toast.makeText(getActivity(),"This language is not supported by your device.",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNeutralText("Ok");
                        dialogHeader.show();

                    }
                });
                return null;
            }
        }.execute();
    }

    //This method is used when the translation is from Text To Voice or from Voice To Voice.
    @SuppressLint("StaticFieldLeak")
    public void translateToVoiceAsyncTask(final String textToBeTranslated){
        final Handler textViewHandler = new Handler();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(API_KEY)
                        .build();
                Translate translate = options.getService();

                // Google Translate API used here
                final Translation translation =
                        translate.translate(textToBeTranslated,
                                Translate.TranslateOption.targetLanguage(languageCodes[positionTo]));
                Log.d("textToBeTranslated", "doInBackground: " + textToBeTranslated);
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        final String finalTranslatedString = translation.getTranslatedText().replaceAll("&#39;","'");
                        speechRate = prefs.getInt("speechrate", 0); //0 is the default value. Retrieve value from stored preferences.
                        pitchrate = prefs.getInt("pitchrate", 0); //0 is the default value. Retrieve value from stored preferences.
                        t1.setSpeechRate((float)speechRate/50); // speechRate is stored in integer value from 1 to 100. setPitch() accepts value in terms of float. default is 1.0. In this app, max can be 2.0
                        t1.setPitch((float)pitchrate/50); // pitchRate is stored in integer value from 1 to 100. setPitch() accepts value in terms of float. default is 1.0. In this app, max can be 2.0
                        t1.setLanguage(new Locale(languageCodes[positionTo]));
                        speakOut(finalTranslatedString);
                    }
                });
                return null;
            }
        }.execute();
    }

    // Check if bottomsheet is showing
    public boolean sweetSheetShowing(){
        return sweetSheet.isShow();
    }

    // Dismiss bottomsheet
    public void dismissSweetSheet(){
        sweetSheet.dismiss();
    }

}
