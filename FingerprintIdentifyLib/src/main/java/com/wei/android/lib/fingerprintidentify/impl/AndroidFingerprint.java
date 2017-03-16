package com.wei.android.lib.fingerprintidentify.impl;

import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by Awei on 2017/2/9.
 */
public class AndroidFingerprint extends BaseFingerprint {

    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat mFingerprintManagerCompat;

    public AndroidFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        super(activity, exceptionListener);

        try {
            mFingerprintManagerCompat = FingerprintManagerCompat.from(activity);
            setHardwareEnable(mFingerprintManagerCompat.isHardwareDetected());
            setRegisteredFingerprint(mFingerprintManagerCompat.hasEnrolledFingerprints());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        try {
            mCancellationSignal = new CancellationSignal();
            mFingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    onSucceed();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    onNotMatch();
                }

                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                    onFailed();
                }
            }, null);
        } catch (Throwable e) {
            onCatchException(e);
            onFailed();
        }
    }

    @Override
    protected void doCancelIdentify() {
        try {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return false;
    }
}