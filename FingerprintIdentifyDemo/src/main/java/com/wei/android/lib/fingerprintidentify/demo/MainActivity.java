package com.wei.android.lib.fingerprintidentify.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTips = (TextView) findViewById(R.id.mTvTips);

        mTvTips.setText(App.sStringBuilder.toString());

        if (!App.sFingerprintIdentify.isFingerprintEnable()) {
            mTvTips.append(getString(R.string.not_support));
            return;
        }

        mTvTips.append(getString(R.string.start));
        App.sFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                mTvTips.append(getString(R.string.succeed));
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mTvTips.append(getString(R.string.not_match, availableTimes));
            }

            @Override
            public void onFailed() {
                mTvTips.append(getString(R.string.failed));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.sFingerprintIdentify.cancelIdentify();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.sFingerprintIdentify.cancelIdentify();
    }
}
