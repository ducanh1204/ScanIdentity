package vn.astec.scanidentity.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import vn.astec.scanidentity.R;
import vn.astec.scanidentity.fragments.ScanFragment;

public class MainActivity extends AppCompatActivity {

    public byte[] frontImageData = null;
    public byte[] backImageData = null;
    public byte[] imageData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.activity_action_bar);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, new ScanFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void goToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.content_frame, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    private int screenWidth = 0;

    private int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private float scaleValue = 0;

    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / 1280;
        return scaleValue;
    }

    public int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

}