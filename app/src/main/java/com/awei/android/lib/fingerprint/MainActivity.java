package com.awei.android.lib.fingerprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.awei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.awei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class MainActivity extends AppCompatActivity {

    private TextView mTvInfo;
    private ScrollView mScrollView;
    private FingerprintIdentify mFingerprintIdentify;

    private boolean mNeedToRestartFingerprint = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo = (TextView) findViewById(R.id.mTvInfo);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                exception.printStackTrace();
            }
        });

        tag("指纹功能：" + mFingerprintIdentify.isFingerprintEnable());
        tag("指纹硬件：" + mFingerprintIdentify.isHardwareEnable());
        tag("已录指纹：" + mFingerprintIdentify.isRegisteredFinger());
    }

    public void start(View view) {
        mNeedToRestartFingerprint = true;
        tag("开始验证指纹，请放置你的手指到指纹传感器上");
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                tag("验证成功");
            }

            @Override
            public void onNotMatch(int availableTimes) {
                tag("指纹不匹配，可用次数剩余：" + availableTimes);
            }

            @Override
            public void onFailed() {
                tag("验证遇到错误！！！");
            }
        });
    }

    public void cancel(View view) {
        tag("取消验证");
        mFingerprintIdentify.cancelIdentify();
    }

    public void clear(View view) {
        mTvInfo.setText("");
    }

    private void tag(String msg) {
        mTvInfo.append(msg + "\n");
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNeedToRestartFingerprint) {
            tag("onResume 恢复指纹验证流程");
            mFingerprintIdentify.resumeIdentify();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNeedToRestartFingerprint) {
            tag("onPause 暂停指纹验证");
            mFingerprintIdentify.cancelIdentify();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNeedToRestartFingerprint) {
            tag("onStop 暂停指纹验证");
            mFingerprintIdentify.cancelIdentify();
        }
    }
}
