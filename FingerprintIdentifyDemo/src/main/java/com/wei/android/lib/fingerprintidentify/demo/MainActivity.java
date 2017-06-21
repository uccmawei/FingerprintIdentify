package com.wei.android.lib.fingerprintidentify.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTips;
    private FingerprintIdentify mFingerprintIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvTips = (TextView) findViewById(R.id.mTvTips);

        long time = System.currentTimeMillis();
        mTvTips.append("new FingerprintIdentify() ");
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                mTvTips.append("\n\nException：" + exception.getLocalizedMessage() + "\n");
            }
        });

        mTvTips.append("\n" + getString(R.string.time) + (System.currentTimeMillis() - time) + "ms");
        mTvTips.append("\nisHardwareEnable() " + mFingerprintIdentify.isHardwareEnable());
        mTvTips.append("\nisRegisteredFingerprint() " + mFingerprintIdentify.isRegisteredFingerprint());
        mTvTips.append("\nisFingerprintEnable() " + mFingerprintIdentify.isFingerprintEnable());

        if (!mFingerprintIdentify.isFingerprintEnable()) {
            mTvTips.append("\n" + getString(R.string.not_support));
            return;
        }

        mTvTips.append("\n" + getString(R.string.start));
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                mTvTips.append("\n" + getString(R.string.succeed));
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mTvTips.append("\n" + getString(R.string.not_match, availableTimes));
            }

            @Override
            public void onFailed() {
                mTvTips.append("\n" + getString(R.string.failed));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFingerprintIdentify.cancelIdentify();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFingerprintIdentify.cancelIdentify();
    }
}
