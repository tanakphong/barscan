package com.deverdie.barscan;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageView mImgFlash;
    private Button mBtnChangeOrientation;
    private boolean isPortrait = true;
    private static final String IS_PORTRAIT = "IS_ID";
    private static final String TAG = "dlg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        if (savedInstanceState != null) {
            isPortrait = savedInstanceState.getBoolean(IS_PORTRAIT);
        }

        barcodeScannerView =  findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        mImgFlash =  findViewById(R.id.imgFlash);
        mImgFlash.setImageResource(R.drawable.flash_on);
        mImgFlash.setTag("on");

        mBtnChangeOrientation = findViewById(R.id.change_orientation);
        if (isPortrait) {
            mBtnChangeOrientation.setText(getResources().getString(R.string.landscape));
        }else{
            mBtnChangeOrientation.setText(getResources().getString(R.string.portrait));
        }

        mImgFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(mImgFlash.getTag()).equals("on")) {
                    barcodeScannerView.setTorchOn();
                } else {
                    barcodeScannerView.setTorchOff();
                }
            }
        });

        mBtnChangeOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPortrait = !isPortrait;
                if (isPortrait) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });

        if (!hasFlash()) {
            mImgFlash.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        isPortrait = savedInstanceState.getBoolean("IS_PORTRAIT");
//        Log.i(TAG, "onRestoreInstanceState: "+isPortrait);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
        outState.putBoolean(IS_PORTRAIT, isPortrait);
//        Log.i(TAG, "onSaveInstanceState: "+isPortrait);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        mImgFlash.setTag("off");
        mImgFlash.setImageResource(R.drawable.flash_off);
    }

    @Override
    public void onTorchOff() {
        mImgFlash.setTag("on");
        mImgFlash.setImageResource(R.drawable.flash_on);
    }
}
