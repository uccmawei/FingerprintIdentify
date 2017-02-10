package com.awei.android.lib.fingerprint;

import android.content.ClipData;
import android.content.ClipboardManager;
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
    private ClipboardManager mClipboardManager;
    private FingerprintIdentify mFingerprintIdentify;

    private boolean mHasInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo = (TextView) findViewById(R.id.mTvInfo);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        mFingerprintIdentify = new FingerprintIdentify(this);

        tag("指纹功能：" + mFingerprintIdentify.isFingerprintEnable());
        tag("指纹硬件：" + mFingerprintIdentify.isHardwareEnable());
        tag("已录指纹：" + mFingerprintIdentify.isRegisteredFinger());
    }

    public void start(View view) {
        mHasInit = true;
        tag("用户 - 开始验证，给你3次机会，请交出你的手指吧");
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                tag("验证成功" + "\n" + " *****************************");
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
        tag("用户 - 取消验证");
        mFingerprintIdentify.cancelIdentify();
    }

    public void clear(View view) {
        mTvInfo.setText("");
    }

    public void copy(View view) {
        mClipboardManager.setPrimaryClip(ClipData.newPlainText("bug", mTvInfo.getText().toString()));
        tag("复制到剪切板了，发给开发者吧");
    }

    private void tag(String msg) {
        mTvInfo.append(msg + "\n");
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasInit) {
            tag("尝试恢复验证如果还没完成验证操作");
            mFingerprintIdentify.resumeIdentify();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHasInit) {
            tag("暂停指纹验证 onPause");
            mFingerprintIdentify.cancelIdentify();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHasInit) {
            tag("暂停指纹验证 onStop");
            mFingerprintIdentify.cancelIdentify();
        }
    }
}
