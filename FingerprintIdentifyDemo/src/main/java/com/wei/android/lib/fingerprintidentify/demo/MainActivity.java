package com.wei.android.lib.fingerprintidentify.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTips;
    private boolean mIsCalledStartIdentify = false;
    private FingerprintIdentify mFingerprintIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTips = (TextView) findViewById(R.id.mTvTips);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsCalledStartIdentify) {
            mTvTips.append("\nresume identify if needed");
            mFingerprintIdentify.resumeIdentify();
            return;
        }

        mIsCalledStartIdentify = true;
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                mTvTips.append("\n" + exception.getLocalizedMessage());
            }
        });

        mTvTips.append("create fingerprintIdentify");
        mTvTips.append("\nisHardwareEnable: " + mFingerprintIdentify.isHardwareEnable());
        mTvTips.append("\nisRegisteredFingerprint: " + mFingerprintIdentify.isRegisteredFingerprint());
        mTvTips.append("\nisFingerprintEnable: " + mFingerprintIdentify.isFingerprintEnable());

        if (!mFingerprintIdentify.isFingerprintEnable()) {
            mTvTips.append("\nSorry →_→");
            return;
        }

        mTvTips.append("\nstart identify\nput your finger on the sensor");
        mFingerprintIdentify.resumeIdentify();
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                mTvTips.append("\nonSucceed");
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mTvTips.append("\nonNotMatch, " + availableTimes + " chances left");
            }

            @Override
            public void onFailed() {
                mTvTips.append("\nonFailed");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mTvTips.append("\nrelease");
        mFingerprintIdentify.cancelIdentify();
    }

    public void release(View view) {
        mTvTips.append("\nrelease by click");
        mFingerprintIdentify.cancelIdentify();
    }
}
