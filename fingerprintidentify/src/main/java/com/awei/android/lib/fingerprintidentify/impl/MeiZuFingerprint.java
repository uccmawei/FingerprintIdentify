package com.awei.android.lib.fingerprintidentify.impl;

import android.app.Activity;

import com.awei.android.lib.fingerprintidentify.base.BaseFingerprint;
import com.fingerprints.service.FingerprintManager;

/**
 * Created by Awei on 2017/2/9.
 */
public class MeiZuFingerprint extends BaseFingerprint {

    private int[] mMeiZuFingerprintIds;
    private FingerprintManager mMeiZuFingerprintManager;

    public MeiZuFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        super(activity, exceptionListener);

        try {
            mMeiZuFingerprintManager = FingerprintManager.open();
            if (mMeiZuFingerprintManager != null) {
                mMeiZuFingerprintIds = mMeiZuFingerprintManager.getIds();
                setHardwareEnable(mMeiZuFingerprintManager.isSurpport());
                setRegisteredFinger(mMeiZuFingerprintIds != null && mMeiZuFingerprintIds.length > 0);
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        try {
            mMeiZuFingerprintManager.startIdentify(new FingerprintManager.IdentifyCallback() {
                @Override
                public void onIdentified(int i, boolean b) {
                    onSucceed();
                }

                @Override
                public void onNoMatch() {
                    onNotMatch();
                }
            }, mMeiZuFingerprintIds);
        } catch (Throwable e) {
            onCatchException(e);
            onFailed();
        }
    }

    @Override
    protected void doCancelIdentify() {
        try {
            if (mMeiZuFingerprintManager != null) {
                mMeiZuFingerprintManager.release();
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }
}
