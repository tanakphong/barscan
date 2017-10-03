package com.deverdie.barscan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtValue;
    private Button mBtnScan;
    private LinearLayout mLayControl;
    private Button mBtnCopyToClipboard;
    private Button mBtnLinkTo;
    private AdView mAdView;
    private Button mBtnGoTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MobileAds.initialize(this, "ca-app-pub-3971225854562108/5590847389");
//        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
//
//        AdRequest request = new AdRequest.Builder().build();
//        adView.loadAd(request);

//        AdRequest.Builder adBuilder = new AdRequest.Builder();
//        AdRequest adRequest = adBuilder.build();
//        AdView adView = (AdView)findViewById(R.id.adView);
//        adView.loadAd(adRequest);

//        AdView adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        adView.loadAd(adRequest);

//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

//        MobileAds.initialize(getApplicationContext(),
//                "ca-app-pub-3971225854562108/5590847389");
//
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        // Initialize the Mobile Ads SDK.ca-app-pub-3971225854562108~5106343033
        MobileAds.initialize(this, "ca-app-pub-3971225854562108~9382930342");
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.title_activity_main));
//        toolbar.setTitleTextColor(Color.WHITE);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }

        mTxtValue = (TextView) findViewById(R.id.txtValue);
        mBtnScan = (Button) findViewById(R.id.btnScan);
        mLayControl = (LinearLayout) findViewById(R.id.layControl);
        mLayControl.setVisibility(View.INVISIBLE);
        mBtnCopyToClipboard = (Button) findViewById(R.id.btnCopyToClipboard);
        mBtnLinkTo = (Button) findViewById(R.id.btnLinkto);
        mBtnGoTo = (Button) findViewById(R.id.btnGoto);

        mBtnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = mTxtValue.getText().toString();
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", val);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "`" + val + "` "+getResources().getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
            }
        });

        mBtnLinkTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = mTxtValue.getText().toString();
                Uri uri = Uri.parse("http://www.google.com/#q=" + val);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "Link to", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = mTxtValue.getText().toString();
                Uri uri = Uri.parse(val);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "Link to", Toast.LENGTH_SHORT).show();
            }
        });


        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtValue.setText("");
                mLayControl.setVisibility(View.INVISIBLE);
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.setPrompt(getResources().getString(R.string.scan_help));
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, getResources().getString(R.string.cancelled), Toast.LENGTH_LONG).show();
            } else {
                mTxtValue.setText(result.getContents().toString());
                mLayControl.setVisibility(View.VISIBLE);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("mTxtValue", mTxtValue.getText().toString());
        savedInstanceState.putInt("mLayControl", mLayControl.getVisibility());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTxtValue.setText(savedInstanceState.getString("mTxtValue"));
        int visibilityStage = savedInstanceState.getInt("mLayControl");
        if (visibilityStage==0){
            mLayControl.setVisibility(View.VISIBLE);
        }else{
            mLayControl.setVisibility(View.GONE);
        }
    }


    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
