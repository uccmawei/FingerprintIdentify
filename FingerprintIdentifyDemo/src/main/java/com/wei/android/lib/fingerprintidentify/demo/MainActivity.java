package com.wei.android.lib.fingerprintidentify.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                mTvTips.append("\n" + exception.getLocalizedMessage());
            }
        });

        mTvTips.append("初始化，获取状态：");
        mTvTips.append("\nisHardwareEnable: " + mFingerprintIdentify.isHardwareEnable());
        mTvTips.append("\nisRegisteredFingerprint: " + mFingerprintIdentify.isRegisteredFingerprint());
        mTvTips.append("\nisFingerprintEnable: " + mFingerprintIdentify.isFingerprintEnable());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTvTips.append("\n恢复指纹识别 ifNeeded");
        mFingerprintIdentify.resumeIdentify();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mTvTips.append("\n关闭指纹识别");
        mFingerprintIdentify.cancelIdentify();
    }

    public void start(View view) {
        mTvTips.append("\n开始验证，共3次机会，请放置指纹");
        mFingerprintIdentify.resumeIdentify();
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                mTvTips.append("\n验证通过，自动释放指纹");
            }

            @Override
            public void onNotMatch(int availableTimes) {
                mTvTips.append("\n指纹不匹配，剩余机会：" + availableTimes);
            }

            @Override
            public void onFailed() {
                mTvTips.append("\n验证失败，自动释放指纹");
            }
        });
    }

    public void release(View view) {
        mTvTips.append("\n手动关闭指纹识别");
        mFingerprintIdentify.cancelIdentify();
    }
}
