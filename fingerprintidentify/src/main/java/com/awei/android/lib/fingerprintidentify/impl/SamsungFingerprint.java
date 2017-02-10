package com.awei.android.lib.fingerprintidentify.impl;

import android.app.Activity;

import com.awei.android.lib.fingerprintidentify.base.BaseFingerprint;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

/**
 * Created by Awei on 2017/2/9.
 */
public class SamsungFingerprint extends BaseFingerprint {

    private int mResultCode = -1;
    private SpassFingerprint mSpassFingerprint;

    public SamsungFingerprint(Activity activity) {
        super(activity);

        try {
            Spass spass = new Spass();
            spass.initialize(mActivity);
            mSpassFingerprint = new SpassFingerprint(activity);
            setHardwareEnable(spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT));
            setRegisteredFinger(mSpassFingerprint.hasRegisteredFinger());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doIdentify() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSpassFingerprint.startIdentify(new SpassFingerprint.IdentifyListener() {
                        @Override
                        public void onFinished(int i) {
                            mResultCode = i;
                        }

                        @Override
                        public void onReady() {

                        }

                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onCompleted() {
                            switch (mResultCode) {
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS:
                                    onSucceed();
                                    break;

                                case SpassFingerprint.STATUS_TIMEOUT_FAILED:
                                case SpassFingerprint.STATUS_BUTTON_PRESSED:
                                case SpassFingerprint.STATUS_QUALITY_FAILED:
                                case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
                                    onNotMatch();
                                    break;

                                default:
                                    onFailed();
                                    break;
                            }
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                    onFailed();
                }
            }
        });
    }

    @Override
    protected void doCancelIdentify() {
        try {
            if (mSpassFingerprint != null) {
                mSpassFingerprint.cancelIdentify();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
