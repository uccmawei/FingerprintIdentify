/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.wei.android.lib.fingerprintidentify.aosp;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.CancellationSignal;

import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.Mac;

public final class FingerprintManagerCompat {

    private Context mContext;

    public static FingerprintManagerCompat from(Context context) {
        return new FingerprintManagerCompat(context);
    }

    private FingerprintManagerCompat(Context context) {
        mContext = context;
    }

    private static final FingerprintManagerCompatImpl IMPL;

    static {
        IMPL = new Api23FingerprintManagerCompatImpl();
    }

    public boolean hasEnrolledFingerprints() {
        return IMPL.hasEnrolledFingerprints(mContext);
    }

    public boolean isHardwareDetected() {
        return IMPL.isHardwareDetected(mContext);
    }

    public void authenticate(@Nullable CryptoObject crypto, int flags,
                             @Nullable CancellationSignal cancel, @NonNull AuthenticationCallback callback,
                             @Nullable Handler handler) {
        IMPL.authenticate(mContext, crypto, flags, cancel, callback, handler);
    }

    public static class CryptoObject {

        private final Signature mSignature;
        private final Cipher mCipher;
        private final Mac mMac;

        public CryptoObject(Signature signature) {
            mSignature = signature;
            mCipher = null;
            mMac = null;

        }

        public CryptoObject(Cipher cipher) {
            mCipher = cipher;
            mSignature = null;
            mMac = null;
        }

        public CryptoObject(Mac mac) {
            mMac = mac;
            mCipher = null;
            mSignature = null;
        }

        public Signature getSignature() {
            return mSignature;
        }

        public Cipher getCipher() {
            return mCipher;
        }

        public Mac getMac() {
            return mMac;
        }
    }

    public static final class AuthenticationResult {
        private CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject crypto) {
            mCryptoObject = crypto;
        }

        public CryptoObject getCryptoObject() {
            return mCryptoObject;
        }
    }

    public static abstract class AuthenticationCallback {

        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed() {
        }
    }

    private interface FingerprintManagerCompatImpl {
        boolean hasEnrolledFingerprints(Context context);

        boolean isHardwareDetected(Context context);

        void authenticate(Context context, CryptoObject crypto, int flags, CancellationSignal cancel, AuthenticationCallback callback, Handler handler);
    }

    private static class Api23FingerprintManagerCompatImpl implements FingerprintManagerCompatImpl {

        public Api23FingerprintManagerCompatImpl() {
        }

        @Override
        public boolean hasEnrolledFingerprints(Context context) {
            return FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }

        @Override
        public boolean isHardwareDetected(Context context) {
            return FingerprintManagerCompatApi23.isHardwareDetected(context);
        }

        @Override
        public void authenticate(Context context, CryptoObject crypto, int flags, CancellationSignal cancel,
                                 AuthenticationCallback callback, Handler handler) {
            FingerprintManagerCompatApi23.authenticate(context, wrapCryptoObject(crypto), flags,
                    cancel != null ? cancel.getCancellationSignalObject() : null, wrapCallback(callback), handler);
        }

        private static FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            } else if (cryptoObject.getCipher() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getCipher());
            } else if (cryptoObject.getSignature() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getSignature());
            } else if (cryptoObject.getMac() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getMac());
            } else {
                return null;
            }
        }

        static CryptoObject unwrapCryptoObject(FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            } else if (cryptoObject.getCipher() != null) {
                return new CryptoObject(cryptoObject.getCipher());
            } else if (cryptoObject.getSignature() != null) {
                return new CryptoObject(cryptoObject.getSignature());
            } else if (cryptoObject.getMac() != null) {
                return new CryptoObject(cryptoObject.getMac());
            } else {
                return null;
            }
        }

        private static FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final AuthenticationCallback callback) {
            return new FingerprintManagerCompatApi23.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    callback.onAuthenticationError(errMsgId, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    callback.onAuthenticationHelp(helpMsgId, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal result) {
                    callback.onAuthenticationSucceeded(new AuthenticationResult(unwrapCryptoObject(result.getCryptoObject())));
                }

                @Override
                public void onAuthenticationFailed() {
                    callback.onAuthenticationFailed();
                }
            };
        }
    }
}
