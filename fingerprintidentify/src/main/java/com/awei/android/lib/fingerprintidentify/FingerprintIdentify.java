package com.awei.android.lib.fingerprintidentify;

import android.app.Activity;

import com.awei.android.lib.fingerprintidentify.base.BaseFingerprint;
import com.awei.android.lib.fingerprintidentify.base.BaseFingerprint.FingerprintIdentifyExceptionListener;
import com.awei.android.lib.fingerprintidentify.impl.AndroidFingerprint;
import com.awei.android.lib.fingerprintidentify.impl.MeiZuFingerprint;
import com.awei.android.lib.fingerprintidentify.impl.SamsungFingerprint;

/**
 * Created by Awei on 2017/2/8.
 */

public class FingerprintIdentify {

    private BaseFingerprint mFingerprint;
    private BaseFingerprint mSubFingerprint;

    public FingerprintIdentify(Activity activity) {
        this(activity, null);
    }

    public FingerprintIdentify(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        AndroidFingerprint androidFingerprint = new AndroidFingerprint(activity, exceptionListener);
        if (androidFingerprint.isHardwareEnable()) {
            mSubFingerprint = androidFingerprint;
            if (androidFingerprint.isRegisteredFinger()) {
                mFingerprint = androidFingerprint;
                return;
            }
        }

        SamsungFingerprint samsungFingerprint = new SamsungFingerprint(activity, exceptionListener);
        if (samsungFingerprint.isHardwareEnable()) {
            if (mSubFingerprint != null) {
                mSubFingerprint = samsungFingerprint;
            }
            if (samsungFingerprint.isRegisteredFinger()) {
                mFingerprint = samsungFingerprint;
                return;
            }
        }

        MeiZuFingerprint meiZuFingerprint = new MeiZuFingerprint(activity, exceptionListener);
        if (meiZuFingerprint.isHardwareEnable()) {
            if (mSubFingerprint != null) {
                mSubFingerprint = meiZuFingerprint;
            }
            if (meiZuFingerprint.isRegisteredFinger()) {
                mFingerprint = meiZuFingerprint;
            }
        }
    }

    public void startIdentify(int maxAvailableTimes, BaseFingerprint.FingerprintIdentifyListener listener) {
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.startIdentify(maxAvailableTimes, listener);
    }

    public void cancelIdentify() {
        if (mFingerprint != null) {
            mFingerprint.cancelIdentify();
        }
    }

    public void resumeIdentify() {
        if (!isFingerprintEnable()) {
            return;
        }

        mFingerprint.resumeIdentify();
    }

    public boolean isFingerprintEnable() {
        return mFingerprint != null && mFingerprint.isEnable();
    }

    public boolean isHardwareEnable() {
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isHardwareEnable());
    }

    public boolean isRegisteredFinger() {
        return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isRegisteredFinger());
    }
}