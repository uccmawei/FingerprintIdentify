package com.awei.android.lib.fingerprintidentify.base;

import android.app.Activity;

/**
 * Created by Awei on 2017/2/9.
 */
public abstract class BaseFingerprint {

    protected Activity mActivity;
    private FingerprintIdentifyListener mIdentifyListener;
    private FingerprintIdentifyExceptionListener mExceptionListener;

    private int mNotMatchTimes = 0;                     // 已经验证失败次数
    private int mMaxAvailableTimes = 3;                 // 最大可验证次数
    private boolean mIsHardwareEnable = false;          // 硬件可用
    private boolean mIsRegisteredFinger = false;        // 注册了指纹
    private boolean mIsCanceledIdentify = false;        // 已经关闭了指纹

    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        mActivity = activity;
        mExceptionListener = exceptionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCanceledIdentify = false;
        mIdentifyListener = listener;
        mNotMatchTimes = 0;

        doIdentify();
    }

    public void resumeIdentify() {
        if (mIdentifyListener != null && mNotMatchTimes < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    protected abstract void doIdentify();

    protected abstract void doCancelIdentify();

    protected void onSucceed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNotMatchTimes = mMaxAvailableTimes;

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

    protected void onNotMatch() {
        if (mIsCanceledIdentify) {
            return;
        }

        if (++mNotMatchTimes < mMaxAvailableTimes) {
            if (mIdentifyListener != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIdentifyListener.onNotMatch(mMaxAvailableTimes - mNotMatchTimes);
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

    protected void onFailed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNotMatchTimes = mMaxAvailableTimes;

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

    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFinger;
    }

    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    public boolean isRegisteredFinger() {
        return mIsRegisteredFinger;
    }

    protected void setRegisteredFinger(boolean registeredFinger) {
        mIsRegisteredFinger = registeredFinger;
    }

    public interface FingerprintIdentifyListener {
        void onSucceed();

        void onNotMatch(int availableTimes);

        void onFailed();
    }

    public interface FingerprintIdentifyExceptionListener {
        void onCatchException(Throwable exception);
    }
}