package com.rajchandak.languagetranslator;

import android.os.Bundle;
import android.view.View;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.LibsConfiguration;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.LibsActivity;
import com.thefinestartist.finestwebview.FinestWebView;

//
// Created by Raj Chandak on 25-02-2018.
//


public class AboutActivity extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Setup Libraries. Library used - https://github.com/mikepenz/AboutLibraries.
        //This library automatically adds all google and third party libraries used in the app and displays it in the form of a list of cards.
        setIntent(new LibsBuilder()
                .withAutoDetect(true)
                .withVersionShown(true)
                .withLicenseShown(true)
                .withLicenseDialog(false)
                .withActivityTitle("About App")
                .withFields(R.string.class.getFields())
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withListener(libsListener)
                .withExcludedLibraries("CardView","Constraint Layout","Joda-Time","discreteseekbar","fastadapter","guava","jackson")
                .withAboutAppName("Language Translator")
                .withAboutDescription("Language Translator is an Open-Source App that allows users to translate to and from 24 different languages with inter-conversions between voice and text.\n\nThe following Open-Source Libraries were used to develop this app:")
                .intent(this));
        super.onCreate(savedInstanceState);
    }

    //    Listener for licence activity clicks - Begin
    LibsConfiguration.LibsListener libsListener = new LibsConfiguration.LibsListener() {
        @Override
        public void onIconClicked(View v) {
    //        Toast.makeText(v.getContext(), "We are able to track this now ;)", Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onLibraryAuthorClicked(View v, Library library) {

            authorWebView(library);
            return true;
        }

        @Override
        public boolean onLibraryContentClicked(View v, Library library) {

            contentWebView(library);
            return true;
        }

        @Override
        public boolean onLibraryBottomClicked(View v, Library library) {

            licenceWebView(library);
            return true;
        }

        @Override
        public boolean onExtraClicked(View v, Libs.SpecialButton specialButton) {
            return false;
        }

        @Override
        public boolean onIconLongClicked(View v) {
            return false;
        }

        @Override
        public boolean onLibraryAuthorLongClicked(View v, Library library) {

            authorWebView(library);
            return true;
        }

        @Override
        public boolean onLibraryContentLongClicked(View v, Library library) {

            contentWebView(library);
            return true;
        }

        @Override
        public boolean onLibraryBottomLongClicked(View v, Library library) {

            licenceWebView(library);
            return true;
        }
    };
    //    Listener for licence activity clicks - End

    // Setup webview for author's link. This will open a new webview activity.
    // Library used - https://github.com/TheFinestArtist/FinestWebView-Android.
    public void authorWebView(Library library){
        new FinestWebView.Builder(getBaseContext())
                .titleDefault("Language Translator")
                .theme(R.style.WebviewBlueTheme)
                .toolbarScrollFlags(0)
                .progressBarColorRes(R.color.colorAccent)
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .showSwipeRefreshLayout(false)
                .swipeRefreshColorRes(R.color.colorPrimaryDark)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(library.getAuthorWebsite()); // Retrieve library's author's website.
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    // Setup webview for library's link. This will open a new webview activity.
    // Library used - https://github.com/TheFinestArtist/FinestWebView-Android.
    public void contentWebView(Library library){
        new FinestWebView.Builder(getBaseContext())
                .titleDefault("Language Translator")
                .theme(R.style.WebviewBlueTheme)
                .toolbarScrollFlags(0)
                .progressBarColorRes(R.color.colorAccent)
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .showSwipeRefreshLayout(false)
                .swipeRefreshColorRes(R.color.colorPrimaryDark)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(library.getLibraryWebsite()); // Retrieve library's github link.
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    // Setup webview for library's link. This will open a new webview activity.
    // Library used - https://github.com/TheFinestArtist/FinestWebView-Android.
    public void licenceWebView(Library library){
        new FinestWebView.Builder(getBaseContext())
                .titleDefault("Language Translator")
                .theme(R.style.WebviewBlueTheme)
                .toolbarScrollFlags(0)
                .progressBarColorRes(R.color.colorAccent)
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .showSwipeRefreshLayout(false)
                .swipeRefreshColorRes(R.color.colorPrimaryDark)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(library.getLicense().getLicenseWebsite()); // Retrieve library's licence link.
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit); //Closing activity with animation.
    }
}
