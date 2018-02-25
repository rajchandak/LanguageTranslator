package com.rajchandak.languagetranslator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rajchandak.languagetranslator.fragments.TextToTextFragment;
import com.rajchandak.languagetranslator.fragments.TextToVoiceFragment;
import com.rajchandak.languagetranslator.fragments.VoiceToTextFragment;
import com.rajchandak.languagetranslator.fragments.VoiceToVoiceFragment;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

//
// Created by Raj Chandak on 23-02-2018.
//

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private View speechRateDialog;
    private View pitchRateDialog;
    private SeekBar speechRateSeekbar;
    private SeekBar pitchRateSeekbar;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    int speechRate;
    int pitchRate;
    TextView speechProgressText;
    TextView pitchProgressText;

    //Declare 4 viewpager fragments
    VoiceToVoiceFragment frag1 = new VoiceToVoiceFragment();
    VoiceToTextFragment frag2 = new VoiceToTextFragment();
    TextToVoiceFragment frag3 = new TextToVoiceFragment();
    TextToTextFragment frag4 = new TextToTextFragment();
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.viewpagerMain);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        fragmentManager = getSupportFragmentManager();
        initMenuFragment();

    }

    //store speechRate value retrieved from speechRateSeekbar
    private void setupSpeechSharedPreferences(){
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("speechrate", speechRate);
        editor.apply();
    }

    //store speechRate value retrieved from pitchRateSeekbar
    private void setupPitchSharedPreferences(){
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("pitchrate", pitchRate);
        editor.apply();
    }

    //retrieve speechRate value stored in key "speechrate"
    private int retrieveSpeechSharedPreferences(){
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        speechRate= prefs.getInt("speechrate", 200); //200 is the default value.

        return speechRate;
    }

    //retrieve pitchRate value stored in key "pitchrate"
    private int retrievePitchSharedPreferences(){
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        pitchRate= prefs.getInt("pitchrate", 200); //200 is the default value.

        return pitchRate;
    }

    // Setup menu. Library used - https://github.com/Yalantis/Context-Menu.Android
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);

    }

    // Populate the menu
    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_arrow_left_white_24dp);
        close.setBgColor(getResources().getColor(R.color.colorPrimary));
        close.setDividerColor(R.color.white);

        MenuObject rate = new MenuObject("Set Speech Rate");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_text_to_speech_white_24dp);
        rate.setBitmap(b);
        rate.setBgColor(getResources().getColor(R.color.colorPrimary));
        rate.setDividerColor(R.color.white);


        MenuObject pitch = new MenuObject("Set Pitch");
        Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_volume_high_white_24dp);
        pitch.setBitmap(b1);
        pitch.setBgColor(getResources().getColor(R.color.colorPrimary));
        pitch.setDividerColor(R.color.white);

        MenuObject libs = new MenuObject("About App");
        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_information_white_24dp);
        libs.setBitmap(b2);
        libs.setBgColor(getResources().getColor(R.color.colorPrimary));
        libs.setDividerColor(R.color.white);

        menuObjects.add(close);
        menuObjects.add(rate);
        menuObjects.add(pitch);
        menuObjects.add(libs);

        return menuObjects;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if(position==1)
        {

            final LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (speechRateDialog != null) {
                ViewGroup parent = (ViewGroup) speechRateDialog.getParent();
                if (parent != null) {
                    parent.removeView(speechRateDialog);
                }
            }
            try {
                assert inflater1 != null;
                speechRateDialog = inflater1.inflate(R.layout.change_speech_rate, null);

            } catch (InflateException ignored) {

            }
            assert speechRateDialog != null;
            speechRateSeekbar = speechRateDialog.findViewById(R.id.speech_rate_seekbar);
            speechProgressText = speechRateDialog.findViewById(R.id.speech_progress_text);
            speechRateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress==0) //setSpeechRate() method does not accept 0 as input.
                        progress = 1;
                    speechProgressText.setText((float)(progress)/50 + "x");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            if(retrieveSpeechSharedPreferences()==200)
            {
                speechRateSeekbar.setProgress(Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_RATE, 100)/2); //Get default speech rate. Default value is 100. We display it as 1.0x at seekbar value 50
            }
            else
            {
                speechRateSeekbar.setProgress(retrieveSpeechSharedPreferences());
            }
            // Create dialog with seekbar to store speechRate. Library used - https://github.com/javiersantos/MaterialStyledDialogs
            final MaterialStyledDialog.Builder dialogHeader = new MaterialStyledDialog.Builder(this)
                    .setHeaderDrawable(R.drawable.header)
                    .setIcon(R.drawable.ic_text_to_speech_white_24dp)
                    .withDialogAnimation(false)
                    .setCancelable(false)
                    .setTitle("Set Speech Rate")
                    .setCustomView(speechRateDialog)
                    .setPositiveText("Set")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            speechRate = speechRateSeekbar.getProgress();
                            if(speechRate == 0)
                            {
                                speechRate = speechRate + 1;
                            }
                            setupSpeechSharedPreferences();
                        }
                    })
                    .setNegativeText("Default")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            speechRate = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_RATE, 100)/2); //Default System value
                            setupSpeechSharedPreferences();
                        }
                    })
                    .setNeutralText("Cancel");

            dialogHeader.show();
        }

        else if(position==2)
        {
            final LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (pitchRateDialog != null) {
                ViewGroup parent = (ViewGroup) pitchRateDialog.getParent();
                if (parent != null) {
                    parent.removeView(pitchRateDialog);
                }
            }
            try {
                assert inflater2 != null;
                pitchRateDialog = inflater2.inflate(R.layout.change_pitch_rate, null);

            } catch (InflateException ignored) {

            }
            assert pitchRateDialog != null;
            pitchRateSeekbar = pitchRateDialog.findViewById(R.id.pitch_rate_seekbar);
            pitchProgressText = pitchRateDialog.findViewById(R.id.pitch_progress_text);
            pitchRateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress==0)
                        progress = 1;
                    pitchProgressText.setText((float)(progress)/50 + "x");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            if(retrievePitchSharedPreferences()==200)
            {
                pitchRateSeekbar.setProgress(Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_PITCH, 100)/2); //Get default speech rate. Default value is 100. We display it as 1.0x at seekbar value 50
            }
            else
            {
                pitchRateSeekbar.setProgress(retrievePitchSharedPreferences());
            }
            // Create dialog with seekbar to store pitchRate. Library used - https://github.com/javiersantos/MaterialStyledDialogs
            final MaterialStyledDialog.Builder dialogHeader = new MaterialStyledDialog.Builder(this)
                    .setHeaderDrawable(R.drawable.header)
                    .setIcon(R.drawable.ic_volume_high_white_24dp)
                    .withDialogAnimation(false)
                    .setCancelable(false)
                    .setTitle("Set Pitch")
                    .setCustomView(pitchRateDialog)
                    .setPositiveText("Set")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            pitchRate = pitchRateSeekbar.getProgress();
                            if(pitchRate == 0)
                            {
                                pitchRate = pitchRate + 1;
                            }
                            setupPitchSharedPreferences();
                        }
                    })
                    .setNegativeText("Default")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            pitchRate = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_PITCH, 100)/2); //Default System value
                            setupPitchSharedPreferences();
                        }
                    })
                    .setNeutralText("Cancel");

            dialogHeader.show();
        }
        else if(position==3)
        {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);

        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
    }

    // Setup viewpager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(frag1, "Voice To Voice");
        adapter.addFragment(frag2, "Voice To Text");
        adapter.addFragment(frag3, "Text To Voice");
        adapter.addFragment(frag4, "Text To Text");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        }
        if(frag1.sweetSheet!=null && frag1.sweetSheetShowing())
            frag1.dismissSweetSheet();

        else if(frag2.sweetSheet!=null && frag2.sweetSheetShowing())
            frag2.dismissSweetSheet();

        else if(frag3.sweetSheet!=null && frag3.sweetSheetShowing())
            frag3.dismissSweetSheet();

        else if(frag4.sweetSheet!=null && frag4.sweetSheetShowing())
            frag4.dismissSweetSheet();

        else
            super.onBackPressed();

    }
}
