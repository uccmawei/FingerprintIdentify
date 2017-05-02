# FingerprintIdentify

This is an expandable Android fingerprint api compatible lib, which also combine [Samsung](http://developer.samsung.com/galaxy/pass#) and [MeiZu](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI)'s official fingerprint api.

Samsung and MeiZu's fingerprint SDK supports most devices which system versions less than Android 6.0 .

Api priority level：Android > Samsung > MeiZu

[![](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/QRCode_en.png)](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/demo.apk)

[中文版](https://github.com/uccmawei/FingerprintIdentify/blob/master/other/README_ZH.md)

**1. Gradle**

    compile 'com.wei.android.lib:fingerprintidentify:1.1.2'

**2. AndroidManifest**

    <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
    <uses-permission android:name="com.fingerprints.service.ACCESS_FINGERPRINT_MANAGER"/>
    <uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"/>

**3. FingerprintIdentify api**

    mFingerprintIdentify = new FingerprintIdentify(this);                       // create object
    mFingerprintIdentify = new FingerprintIdentify(this, exceptionListener);    // create with error listener
    mFingerprintIdentify.isFingerprintEnable();                                 // is fingerprint usable
    mFingerprintIdentify.isHardwareEnable();                                    // is hardware usable
    mFingerprintIdentify.isRegisteredFingerprint();                             // is fingerprint collected
    mFingerprintIdentify.startIdentify(maxTimes, listener);                     // start identify
    mFingerprintIdentify.cancelIdentify();                                      // cancel identify
    mFingerprintIdentify.resumeIdentify();                                      // resume identify

**4. startIdentify method**

    mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
        @Override
        public void onSucceed() {
            // succeed, release hardware automatically
        }

        @Override
        public void onNotMatch(int availableTimes) {
            // not match, try again automatically
        }

        @Override
        public void onFailed() {
            // failed, release hardware automatically
        }
    });

**5. Proguard**

    # MeiZuFingerprint
    -keep class com.fingerprints.service.** { *; }
    
    # SmsungFingerprint
    -keep class com.samsung.android.sdk.** { *; }

**6. Notice**

https://code.google.com/p/android/issues/detail?id=231939

**7. Version Update**

**v1.1.2**　`2017.04.25`　Modify AOSP's code，avoid the PackageManager.FEATURE_FINGERPRINT limit.

**v1.1.1**　`2017.03.20`　Modify gradle AppCompat lib version.

**v1.1.0**　`2017.03.16`　Modify package name and bug fixed.

**v1.0.2**　`2017.02.17`　Add exception callback.

**v1.0.1**　`2017.02.15`　Bug fixed.

**v1.0.0**　`2017.02.10`　Release v1.


## License ##

Licensed under the MIT License, see the [LICENSE](https://github.com/uccmawei/FingerprintIdentify/blob/master/LICENSE) for copying permission.