package com.rajchandak.languagetranslator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rajchandak.languagetranslator.BaseFragmentClass;
import com.rajchandak.languagetranslator.R;

//
// Created by Raj Chandak on 23-02-2018.
//

public class TextToVoiceFragment extends BaseFragmentClass {
    View customView;
    TextInputEditText inputText;
    String text;
    public TextToVoiceFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        final LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        translateButton.setText("Type Text");
        translateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                if (customView != null) {
                    ViewGroup parent = (ViewGroup) customView.getParent();
                    if (parent != null) {
                        parent.removeView(customView);
                    }
                }
                try {
                    assert inflater1 != null;
                    customView = inflater1.inflate(R.layout.input_text_dialog, null);
                } catch (InflateException ignored) {

                }
                assert customView != null;
                inputText = customView.findViewById(R.id.text_to_voice_input_text);
                inputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                inputText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                showMaterialDialog();
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

    public void showMaterialDialog(){
        // Create dialog with textview to retrieve text typed by the user. Library used - https://github.com/javiersantos/MaterialStyledDialogs
        final MaterialStyledDialog.Builder dialogHeader = new MaterialStyledDialog.Builder(getActivity())
                .setHeaderDrawable(R.drawable.header)
                .setIcon(images[positionFrom])
                .withDialogAnimation(false)
                .setTitle("Translate from " + translatedFromLanguage)
                .setCustomView(customView)
                .setPositiveText("Translate")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            text = inputText.getText().toString();
                            translateText();
                    }
                })
                .setNegativeText("Cancel");
        dialogHeader.show();
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
    public void translateText() {
        super.translateText();
        translateToVoiceAsyncTask(text);
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
}
