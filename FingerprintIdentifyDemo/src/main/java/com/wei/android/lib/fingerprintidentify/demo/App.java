package com.wei.android.lib.fingerprintidentify.demo;

import android.app.Application;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by Awei on 2017/6/14.
 */

public class App extends Application {

    public static StringBuilder sStringBuilder;
    public static FingerprintIdentify sFingerprintIdentify;

    @Override
    public void onCreate() {
        super.onCreate();

        sStringBuilder = new StringBuilder();
        long time = System.currentTimeMillis();

        sStringBuilder.append("new FingerprintIdentify() ");
        sFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                sStringBuilder.append("\nException：");
                sStringBuilder.append(exception.getLocalizedMessage());
                sStringBuilder.append("\n");
            }
        });
        sStringBuilder.append(getString(R.string.time));
        sStringBuilder.append(System.currentTimeMillis() - time);
        sStringBuilder.append("ms");
        sStringBuilder.append("\n");

        sStringBuilder.append("isHardwareEnable() ");
        sStringBuilder.append(sFingerprintIdentify.isHardwareEnable());
        sStringBuilder.append("\n");

        sStringBuilder.append("isRegisteredFingerprint() ");
        sStringBuilder.append(sFingerprintIdentify.isRegisteredFingerprint());
        sStringBuilder.append("\n");

        sStringBuilder.append("isFingerprintEnable() ");
        sStringBuilder.append(sFingerprintIdentify.isFingerprintEnable());
    }
}
