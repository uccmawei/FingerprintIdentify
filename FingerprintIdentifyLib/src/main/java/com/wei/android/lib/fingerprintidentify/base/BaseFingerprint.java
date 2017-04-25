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

    private int mNotMatchTimes = 0;                         // 已经验证失败次数
    private int mMaxAvailableTimes = 3;                     // 最大可验证次数
    private boolean mIsHardwareEnable = false;              // 硬件可用
    private boolean mIsRegisteredFingerprint = false;       // 已经录入了指纹
    private boolean mIsCanceledIdentify = false;            // 已经关闭了指纹
    private boolean mIsCalledStartIdentify = false;         // 已经开始了指纹，暂停后才能恢复

    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        mActivity = activity;
        mExceptionListener = exceptionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCalledStartIdentify = true;
        mIdentifyListener = listener;
        mIsCanceledIdentify = false;
        mNotMatchTimes = 0;

        doIdentify();
    }

    /**
     * 比如在验证指纹时来电话了，就暂时关闭了指纹识别，打完电话后再打开APP时可以继续上次未完成的操作，继续验证指纹。
     */
    public void resumeIdentify() {
        if (mIsCalledStartIdentify && mIdentifyListener != null && mNotMatchTimes < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    /**
     * 关闭指纹识别，释放硬件
     */
    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    /**
     * 识别失败后是否需要重新调用接口来继续下一次的识别
     *
     * @return 是否需要重新调用
     */
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    /**
     * 各种指纹API报错的时候统一回调
     *
     * @param exception 错误信息
     */
    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    /**
     * 各个实现类需要实现的方法：开始指纹验证
     */
    protected abstract void doIdentify();

    /**
     * 各个实现类需要实现的方法：结束指纹验证，释放硬件
     */
    protected abstract void doCancelIdentify();

    /**
     * 指纹验证成功，会自动关闭指纹，并释放硬件
     */
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

    /**
     * 指纹验证未通过，根据错误次数自动判断是否继续下次验证
     */
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

    /**
     * 指纹验证失败，全部机会消耗完毕，会自动关闭指纹，并释放硬件
     */
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

    /**
     * 指纹硬件可用并且已经录入指纹
     *
     * @return 指纹功能是否可用
     */
    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFingerprint;
    }

    /**
     * 指纹识别硬件是否可用
     *
     * @return 是否可用
     */
    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    /**
     * 保存硬件是否可用状态
     *
     * @param hardwareEnable 是否可用
     */
    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    /**
     * 是否已经录入了指纹
     *
     * @return 是否已经录入了指纹
     */
    public boolean isRegisteredFingerprint() {
        return mIsRegisteredFingerprint;
    }

    /**
     * 保存是否已经录入了指纹
     *
     * @param registeredFingerprint 是否已经录入
     */
    protected void setRegisteredFingerprint(boolean registeredFingerprint) {
        mIsRegisteredFingerprint = registeredFingerprint;
    }

    /**
     * 验证指纹的时候的回调接口
     */
    public interface FingerprintIdentifyListener {
        void onSucceed();

        void onNotMatch(int availableTimes);

        void onFailed();
    }

    /**
     * 使用指纹库时产生的错误回调接口
     */
    public interface FingerprintIdentifyExceptionListener {
        void onCatchException(Throwable exception);
    }
}