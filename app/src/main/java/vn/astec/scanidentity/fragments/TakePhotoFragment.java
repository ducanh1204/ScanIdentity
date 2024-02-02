package vn.astec.scanidentity.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import vn.astec.scanidentity.R;
import vn.astec.scanidentity.activities.MainActivity;

public class TakePhotoFragment extends Fragment implements View.OnClickListener {

    private Button btnScan;
    private View view;
    private MainActivity mainActivity;
    private ImageView img;
    private ImageView btnNext,btnPre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_take_photo, container, false);
        mainActivity = (MainActivity) getActivity();
        btnScan = view.findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        img = view.findViewById(R.id.img);

        img.setOnClickListener(this);
        img.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        img.getLayoutParams().width = mainActivity.getSizeWithScale(256);
        img.getLayoutParams().height = mainActivity.getSizeWithScale(144);
        img.setBackground(getResources().getDrawable(R.drawable.bg_img));

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        mainActivity.imageData = null;
        btnNext.setColorFilter(ContextCompat.getColor(mainActivity, R.color.colorGrey), android.graphics.PorterDuff.Mode.SRC_IN);

        btnPre = view.findViewById(R.id.btn_pre);
        btnPre.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                clickScanImage();
                break;
            case R.id.btn_next:
                if (mainActivity.imageData != null) {
                    mainActivity.goToFragment(new InformationFragment());
                }
                break;
            case R.id.btn_pre:
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }

    private void clickScanImage() {
        scanImage();
    }

    // Thay code chụp ảnh vào đây
    private void scanImage() {
        // reset data và giao diện
        img.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        mainActivity.imageData = null;

        // ví dụ hàm chụp ảnh trong 1 listener, đợi chụp xong sau 3 giây
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageDrawable(getResources().getDrawable(R.drawable.front));
                        mainActivity.imageData = new byte[0];
                        checkEnableNext();
                    }
                });
            }
        }).start();
    }


    private void checkEnableNext() {
        if (mainActivity.imageData != null ) {
            btnNext.setColorFilter(ContextCompat.getColor(mainActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}