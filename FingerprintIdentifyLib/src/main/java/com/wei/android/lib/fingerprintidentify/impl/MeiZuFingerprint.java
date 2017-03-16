package com.wei.android.lib.fingerprintidentify.impl;

import android.app.Activity;

import com.fingerprints.service.FingerprintManager;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by Awei on 2017/2/9.
 */
public class MeiZuFingerprint extends BaseFingerprint {

    private FingerprintManager mMeiZuFingerprintManager;

    public MeiZuFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        super(activity, exceptionListener);

        try {
            mMeiZuFingerprintManager = FingerprintManager.open();
            if (mMeiZuFingerprintManager != null) {
                setHardwareEnable(true);
                int[] fingerprintIds = mMeiZuFingerprintManager.getIds();
                setRegisteredFingerprint(fingerprintIds != null && fingerprintIds.length > 0);
            }
        } catch (Throwable e) {
            onCatchException(e);
        }

        releaseMBack();
    }

    @Override
    protected void doIdentify() {
        try {
            mMeiZuFingerprintManager = FingerprintManager.open();
            mMeiZuFingerprintManager.startIdentify(new FingerprintManager.IdentifyCallback() {
                @Override
                public void onIdentified(int i, boolean b) {
                    onSucceed();
                }

                @Override
                public void onNoMatch() {
                    onNotMatch();
                }
            }, mMeiZuFingerprintManager.getIds());
        } catch (Throwable e) {
            onCatchException(e);
            onFailed();
        }
    }

    @Override
    protected void doCancelIdentify() {
        releaseMBack();
    }

    private void releaseMBack() {
        try {
            if (mMeiZuFingerprintManager != null) {
                mMeiZuFingerprintManager.release();
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }
}
