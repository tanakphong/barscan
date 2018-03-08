package com.deverdie.barscan;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.Size;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageView mImgFlash;
    private ImageView mImg1D;
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
            Log.i(TAG, "savedInstanceState IS_PORTRAIT: "+isPortrait);
        }

        barcodeScannerView =  findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

//        barcodeSurface = (BarcodeView) findViewById(R.id.zxing_barcode_surface);
//        barcodeSurface.setFramingRectSize(new Size(400,100));
//        if (is1D) {
//            barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(400, 100));
//        } else {
//            barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(300, 300));
//        }


        mImgFlash =  findViewById(R.id.imgFlash);
        mImgFlash.setImageResource(R.drawable.flash_on);
        mImgFlash.setTag("on");

        mImg1D =  findViewById(R.id.img1D);
        mImg1D.setImageResource(R.drawable.flash_on);
        mImg1D.setTag("1d");
        mImg1D.setVisibility(View.GONE);

        Log.i(TAG, "onCreate: "+isPortrait);
        mBtnChangeOrientation = findViewById(R.id.change_orientation);
        if (isPortrait) {
            barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(400, 100));
            mBtnChangeOrientation.setTag("landscape");
            mBtnChangeOrientation.setText(getResources().getString(R.string.landscape));
        }else{
            barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(300, 300));
            mBtnChangeOrientation.setTag("portrait");
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

        mImg1D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(mImg1D.getTag()).equals("1d")) {
//                    barcodeSurface.setFramingRectSize(new Size(400,100));
//                    barcodeScannerView.getBarcodeView().removeAllViews();
//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    int width = metrics.widthPixels;
//                    int height = metrics.heightPixels;
//                    barcodeScannerView.getBarcodeView().setFramingRectSize(new Size((int) (width*0.9f), (int) (height*0.25f)));
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//                    layoutParams.setMargins(0, (int) (-1*(height*0.2)), 0, 0);
//                    barcodeScannerView.setLayoutParams(layoutParams);

//                    barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(400,100));
//                    CameraSettings cameraSettings = barcodeScannerView.getBarcodeView().getCameraSettings();
//                    //cameraSettings.setBarcodeSceneModeEnabled(true);
//                    cameraSettings.setContinuousFocusEnabled(true);
//                    cameraSettings.setAutoFocusEnabled(true);
//                    barcodeScannerView.getBarcodeView().setCameraSettings(cameraSettings);
//                    is1D = true;
//                    mImg1D.setTag("2d");
//                    mImg1D.setImageResource(R.drawable.flash_off);
                    Toast.makeText(getApplicationContext(), "1D", Toast.LENGTH_SHORT).show();
                } else {
//                    barcodeSurface.setFramingRectSize(new Size(200,200));
//                    barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(300,300));
//                    barcodeScannerView.getBarcodeView().refreshDrawableState();
//                    mImg1D.setTag("1d");
//                    mImg1D.setImageResource(R.drawable.flash_off);
//                    is1D = false;
                    Toast.makeText(getApplicationContext(), "2D", Toast.LENGTH_SHORT).show();
                }
                finish();

                startActivity(getIntent());
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
//                if (String.valueOf(mBtnChangeOrientation.getTag()).equals("portrait")) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    isPortrait = true;
//                } else if (String.valueOf(mBtnChangeOrientation.getTag()).equals("landscape")) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    isPortrait = false;
//                }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
        outState.putBoolean(IS_PORTRAIT, isPortrait);
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
