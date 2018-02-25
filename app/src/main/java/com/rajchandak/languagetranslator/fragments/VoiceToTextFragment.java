package com.rajchandak.languagetranslator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajchandak.languagetranslator.BaseFragmentClass;

//
// Created by Raj Chandak on 23-02-2018.
//

public class VoiceToTextFragment extends BaseFragmentClass {

    public VoiceToTextFragment(){}

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
    }

    @Override
    public void setupImages(View view) {
        super.setupImages(view);
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
        translateToTextAsyncTask(spokenText);
    }

    @Override
    public void translateToTextAsyncTask(String textToBeTranslated) {
        super.translateToTextAsyncTask(textToBeTranslated);
    }

    @Override
    public boolean sweetSheetShowing() {
        return super.sweetSheetShowing();
    }

    @Override
    public void dismissSweetSheet() {
        super.dismissSweetSheet();
    }
}
