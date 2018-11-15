package com.wei.android.lib.fingerprintidentify.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTips;
    private ScrollView mScrollView;
    private FingerprintIdentify mFingerprintIdentify;

    private static final int MAX_AVAILABLE_TIMES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvTips = findViewById(R.id.mTvTips);
        mScrollView = findViewById(R.id.mScrollView);

        long time = System.currentTimeMillis();
        append("new FingerprintIdentify().init() ");
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                append("\nException：" + exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();

        append("\n" + getString(R.string.time) + (System.currentTimeMillis() - time) + "ms");
        append("\nisHardwareEnable() " + mFingerprintIdentify.isHardwareEnable());
        append("\nisRegisteredFingerprint() " + mFingerprintIdentify.isRegisteredFingerprint());
        append("\nisFingerprintEnable() " + mFingerprintIdentify.isFingerprintEnable());

        if (!mFingerprintIdentify.isFingerprintEnable()) {
            append("\n" + getString(R.string.not_support));
            return;
        }

        append("\n" + getString(R.string.click_to_start));
    }

    private void append(String msg) {
        mTvTips.append(msg);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void start(View view) {
        append("\n" + getString(R.string.start));
        mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                append("\n" + getString(R.string.succeed));
            }

            @Override
            public void onNotMatch(int availableTimes) {
                append("\n" + getString(R.string.not_match, availableTimes));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                append("\n" + getString(R.string.failed) + " " + isDeviceLocked);
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                append("\n" + getString(R.string.start_failed));
            }
        });
    }

    public void copy(View view) {
        String text = mTvTips.getText().toString();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(text, text));
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFingerprintIdentify.cancelIdentify();
        append("\n" + getString(R.string.stopped_by_life_callback));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFingerprintIdentify.cancelIdentify();
    }
}
