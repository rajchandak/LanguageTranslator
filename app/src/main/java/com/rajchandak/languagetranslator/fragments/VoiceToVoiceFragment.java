package com.rajchandak.languagetranslator.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.tutoshowcase.TutoShowcase;
import com.rajchandak.languagetranslator.BaseFragmentClass;
import com.rajchandak.languagetranslator.R;

//
// Created by Raj Chandak on 23-02-2018.
//

public class VoiceToVoiceFragment extends BaseFragmentClass {


    int flag=0;
    public VoiceToVoiceFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (checkAppStart()) {
            case NORMAL:

                break;
            case FIRST_TIME_VERSION:

                break;
            case FIRST_TIME: //App opened for the first time. We display the tutorial.
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayTutorial();
                    }
                }, 250);
                flag=1;
                break;
            default:
                break;
        }
    }



    @Override
    public void setupImages(View view) {
        super.setupImages(view);
        convertFrom = view.findViewById(R.id.convert_from_image);
        convertFrom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                convertFromImageClicked();
                if(flag==1){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayTutorialSweetsheet();
                        }
                    }, 1200);
                }

                flag=0;
            }
        });
    }


    // Tutorial for change of language. Library used - https://github.com/florent37/TutoShowcase
    protected void displayTutorial() {
        TutoShowcase.from(getActivity())
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        displayTutorialMore();
                    }
                })
                .setContentView(R.layout.intro_layout)
                .setFitsSystemWindows(false)
                .on(R.id.convert_from_image)
                .addCircle()
                .withBorder()
                .show();
    }

    // Tutorial for swipe for more when bottomsheet is displayed. Library used - https://github.com/florent37/TutoShowcase
    protected void displayTutorialSweetsheet(){
        TutoShowcase.from(getActivity())
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                    }
                })
                .setContentView(R.layout.intro_view_sweetsheet)
                .setFitsSystemWindows(false)
                .on(R.id.convert_to_cardview) //Since bottomsheet used is from a library, we do not have it's resource id. So, we used the underlying cardview's id.
                .displaySwipableLeft()
                .delayed(400)
                .animated(true)
                .show();
    }

    // Tutorial for more options. Library used - https://github.com/florent37/TutoShowcase
    protected  void displayTutorialMore(){
        TutoShowcase.from(getActivity())
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                    }
                })
                .setContentView(R.layout.intro_layout_more)
                .setFitsSystemWindows(false)
                .on(R.id.context_menu)
                .addCircle()
                .withBorder()
                .show();
    }

    @Override
    public void onInit(int status) {
        super.onInit(status);
    }

    @Override
    public void speakOut(String text) {
        super.speakOut(text);
    }

    @Override
    public void setupViewpager() {
        super.setupViewpager();
    }


    @Override
    public void askSpeechInput() {
        super.askSpeechInput();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void translateText() {
        super.translateText();
        translateToVoiceAsyncTask(spokenText);

    }

    @Override
    public void translateToVoiceAsyncTask(String textToBeTranslated) {
        super.translateToVoiceAsyncTask(textToBeTranslated);
    }



    @Override
    public boolean sweetSheetShowing() {
        return super.sweetSheetShowing();
    }

    @Override
    public void dismissSweetSheet() {
        super.dismissSweetSheet();
    }


    //To Check if app is opened for the first time - begin

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL
    }

    private static final String LAST_APP_VERSION = "last_app_version";

    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        AppStart appStart = AppStart.NORMAL;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).apply();
        } catch (PackageManager.NameNotFoundException e) {
//            Log.w(Constants.LOG,
//                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }

    //To Check if app is opened for the first time - end

}
