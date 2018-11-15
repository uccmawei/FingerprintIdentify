package com.wei.android.lib.fingerprintidentify.impl;

import android.content.Context;

import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassInvalidStateException;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Copyright (c) 2017 Awei
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * Created by Awei on 2017/2/9.
 */
public class SamsungFingerprint extends BaseFingerprint {

    private int mResultCode = -1;
    private SpassFingerprint mSpassFingerprint;

    public SamsungFingerprint(Context context, ExceptionListener exceptionListener) {
        super(context, exceptionListener);

        try {
            Spass spass = new Spass();
            spass.initialize(mContext);
            mSpassFingerprint = new SpassFingerprint(mContext);
            setHardwareEnable(spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT));
            setRegisteredFingerprint(mSpassFingerprint.hasRegisteredFinger());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        runOnUiThread(new Runnable() {
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

                                case SpassFingerprint.STATUS_SENSOR_FAILED:
                                case SpassFingerprint.STATUS_OPERATION_DENIED:
                                case SpassFingerprint.STATUS_TIMEOUT_FAILED:
                                case SpassFingerprint.STATUS_BUTTON_PRESSED:
                                case SpassFingerprint.STATUS_QUALITY_FAILED:
                                case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
                                    onNotMatch();
                                    break;

                                case SpassFingerprint.STATUS_USER_CANCELLED:
                                    // do nothing
                                    break;

                                default:
                                    onFailed(false);
                                    break;
                            }
                        }
                    });
                } catch (Throwable e) {
                    if (e instanceof SpassInvalidStateException) {
                        SpassInvalidStateException stateException = (SpassInvalidStateException) e;
                        if (stateException.getType() == 1) {
                            onFailed(true);
                        } else {
                            onCatchException(e);
                            onFailed(false);
                        }
                    } else {
                        onCatchException(e);
                        onFailed(false);
                    }
                }
            }
        });
    }

    @Override
    protected void doCancelIdentify() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSpassFingerprint != null) {
                        mSpassFingerprint.cancelIdentify();
                    }
                } catch (Throwable e) {
                    onCatchException(e);
                }
            }
        });
    }
}
