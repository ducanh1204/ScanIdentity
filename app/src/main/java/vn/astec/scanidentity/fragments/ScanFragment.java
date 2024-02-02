package vn.astec.scanidentity.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import vn.astec.scanidentity.R;
import vn.astec.scanidentity.activities.MainActivity;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private Button btnScan;
    private View view;
    private MainActivity mainActivity;
    private ImageView imgFront, imgBack;
    private boolean isAutoScan = true;
    private int scanPosition = 0;
    private ImageView btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        mainActivity = (MainActivity) getActivity();
        btnScan = view.findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        imgFront = view.findViewById(R.id.img_front);
        imgBack = view.findViewById(R.id.img_back);

        imgFront.setOnClickListener(this);
        imgFront.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        imgFront.getLayoutParams().width = mainActivity.getSizeWithScale(256);
        imgFront.getLayoutParams().height = mainActivity.getSizeWithScale(144);
        imgBack.setOnClickListener(this);
        imgBack.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        imgBack.getLayoutParams().width = mainActivity.getSizeWithScale(256);
        imgBack.getLayoutParams().height = mainActivity.getSizeWithScale(144);

        isAutoScan = true;
        scanPosition = 0;
        imgFront.setBackground(getResources().getDrawable(R.drawable.bg_img));
        imgBack.setBackground(null);

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        mainActivity.frontImageData = null;
        mainActivity.backImageData = null;
        btnNext.setColorFilter(ContextCompat.getColor(mainActivity, R.color.colorGrey), android.graphics.PorterDuff.Mode.SRC_IN);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                clickScanImage();
                break;
            case R.id.img_front:
                clickFrontImage();
                break;
            case R.id.img_back:
                clickBackImage();
                break;
            case R.id.btn_next:
                if (mainActivity.frontImageData != null && mainActivity.backImageData != null) {
                    mainActivity.goToFragment(new TakePhotoFragment());
                }
                break;
            default:
                break;
        }
    }

    private void clickScanImage() {
        if (isAutoScan) {
            if (scanPosition == 0) {
                scanFrontImage();
            } else if (scanPosition == 1) {
                scanBackImage();
            }
        } else {
            if (scanPosition == 0) {
                scanFrontImage();
            } else if (scanPosition == 1) {
                scanBackImage();
            }
        }
    }

    private void clickFrontImage() {
        isAutoScan = false;
        scanPosition = 0;
        imgFront.setBackground(getResources().getDrawable(R.drawable.bg_img));
        imgBack.setBackground(null);
    }

    private void clickBackImage() {
        isAutoScan = false;
        scanPosition = 1;
        imgBack.setBackground(getResources().getDrawable(R.drawable.bg_img));
        imgFront.setBackground(null);
    }

    // Thay code chụp mặt trước vào đây
    private void scanFrontImage() {
        // reset data và giao diện
        imgFront.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        mainActivity.frontImageData = null;

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
                        imgFront.setImageDrawable(getResources().getDrawable(R.drawable.front));
                        mainActivity.frontImageData = new byte[0];
                        if (isAutoScan) {
                            scanPosition = 1;
                            imgBack.setBackground(getResources().getDrawable(R.drawable.bg_img));
                            imgFront.setBackground(null);
                            checkEnableNext();
                        }
                    }
                });
            }
        }).start();
    }

    // Thay code chụp mặt sau vào đây
    private void scanBackImage() {
        // reset data và giao diện
        imgBack.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        mainActivity.backImageData = null;

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
                        imgBack.setImageDrawable(getResources().getDrawable(R.drawable.back));
                        mainActivity.backImageData = new byte[0];
                        checkEnableNext();
                    }
                });
            }
        }).start();
    }

    private void checkEnableNext() {
        if (mainActivity.frontImageData != null && mainActivity.backImageData != null) {
            btnNext.setColorFilter(ContextCompat.getColor(mainActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

}