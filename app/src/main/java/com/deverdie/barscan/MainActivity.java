package com.deverdie.barscan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtValue;
    private ImageView mBtnScan;
    private LinearLayout mLayControl;
    private Button mBtnCopyToClipboard;
    private Button mBtnLinkTo;
    private AdView mAdView;
    private Button mBtnGoTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-3971225854562108~9382930342");
        mAdView =  findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mTxtValue =  findViewById(R.id.txtValue);
        mBtnScan =  findViewById(R.id.imgScan);
        mLayControl =  findViewById(R.id.layControl);
        mLayControl.setVisibility(View.GONE);
        mBtnCopyToClipboard =  findViewById(R.id.btnCopyToClipboard);
        mBtnLinkTo =  findViewById(R.id.btnLinkto);
        mBtnGoTo =  findViewById(R.id.btnGoto);

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
                try{
                    String val = mTxtValue.getText().toString();
                    Uri uri = Uri.parse(val);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.format_invalid), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtValue.setText("");
                mLayControl.setVisibility(View.GONE);
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
                mTxtValue.setText(result.getContents());
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
