package com.wei.android.lib.fingerprintidentify.base;

import android.app.Activity;

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
public abstract class BaseFingerprint {

    protected Activity mActivity;
    private FingerprintIdentifyListener mIdentifyListener;
    private FingerprintIdentifyExceptionListener mExceptionListener;

    private int mNumberOfFailures = 0;                      // number of failures
    private int mMaxAvailableTimes = 3;                     // the most available times
    private boolean mIsHardwareEnable = false;              // if the phone equipped fingerprint hardware
    private boolean mIsRegisteredFingerprint = false;       // if the phone has any fingerprints
    private boolean mIsCanceledIdentify = false;            // if canceled identify
    private boolean mIsCalledStartIdentify = false;         // if started identify

    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        mActivity = activity;
        mExceptionListener = exceptionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCalledStartIdentify = true;
        mIdentifyListener = listener;
        mIsCanceledIdentify = false;
        mNumberOfFailures = 0;

        doIdentify();
    }

    /**
     * Continue to call fingerprint identify, keep the number of failures.
     */
    public void resumeIdentify() {
        if (mIsCalledStartIdentify && mIdentifyListener != null && mNumberOfFailures < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    /**
     * stop identify and release hardware
     */
    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    /**
     * is that need to recall doIdentify again when not match
     *
     * @return needed
     */
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    /**
     * catch the all exceptions
     *
     * @param exception exception
     */
    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    /**
     * do identify actually
     */
    protected abstract void doIdentify();

    /**
     * do cancel identify actually
     */
    protected abstract void doCancelIdentify();

    /**
     * verification passed
     */
    protected void onSucceed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onSucceed();
                }
            });
        }

        cancelIdentify();
    }

    /**
     * fingerprint not match
     */
    protected void onNotMatch() {
        if (mIsCanceledIdentify) {
            return;
        }

        if (++mNumberOfFailures < mMaxAvailableTimes) {
            if (mIdentifyListener != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIdentifyListener.onNotMatch(mMaxAvailableTimes - mNumberOfFailures);
                    }
                });
            }

            if (needToCallDoIdentifyAgainAfterNotMatch()) {
                doIdentify();
            }

            return;
        }

        onFailed();
    }

    /**
     * verification failed
     */
    protected void onFailed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onFailed();
                }
            });
        }

        cancelIdentify();
    }

    /**
     * is that hardware detected and has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFingerprint;
    }

    /**
     * is that hardware detected
     *
     * @return yes
     */
    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    /**
     * save the value of hardware detected
     *
     * @param hardwareEnable detected
     */
    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    /**
     * is that has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isRegisteredFingerprint() {
        return mIsRegisteredFingerprint;
    }

    /**
     * save the value of enrolled fingerprints
     *
     * @param registeredFingerprint enrolled
     */
    protected void setRegisteredFingerprint(boolean registeredFingerprint) {
        mIsRegisteredFingerprint = registeredFingerprint;
    }

    /**
     * identify callback
     */
    public interface FingerprintIdentifyListener {
        void onSucceed();

        void onNotMatch(int availableTimes);

        void onFailed();
    }

    /**
     * exception callback
     */
    public interface FingerprintIdentifyExceptionListener {
        void onCatchException(Throwable exception);
    }
}