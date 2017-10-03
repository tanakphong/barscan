package com.deverdie.barscan;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchFlashlightButton;
    private ImageButton mImgFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);
        switchFlashlightButton.setVisibility(View.INVISIBLE);
        mImgFlash = (ImageButton) findViewById(R.id.imgFlash);
        mImgFlash.setImageResource(R.drawable.flash_on);
        mImgFlash.setTag("on");
        //mImgFlash.setVisibility(View.INVISIBLE);
        mImgFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CustomScannerActivity.this, String.valueOf(mImgFlash.getTag()), Toast.LENGTH_SHORT).show();
                if(String.valueOf(mImgFlash.getTag())=="on"){
                    barcodeScannerView.setTorchOn();
                    //Toast.makeText(CustomScannerActivity.this, "on", Toast.LENGTH_SHORT).show();
                }else{
                    barcodeScannerView.setTorchOff();
                    //Toast.makeText(CustomScannerActivity.this, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            //switchFlashlightButton.setVisibility(View.GONE);
            mImgFlash.setVisibility(View.GONE);
            //mImgFlash.setImageResource(R.drawable.flash_on);

//            if(mImgFlash.getVisibility()==View.VISIBLE){
//                mImgFlash.setVisibility(View.GONE);
//            }else{
//                mImgFlash.setVisibility(View.VISIBLE);
//            }
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }

    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.turn_off_flashlight);
        mImgFlash.setTag("off");
        mImgFlash.setImageResource(R.drawable.flash_off);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.turn_on_flashlight);
        mImgFlash.setTag("on");
        mImgFlash.setImageResource(R.drawable.flash_on);
    }
}
