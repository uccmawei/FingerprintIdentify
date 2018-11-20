# FingerprintIdentify

This is an expandable Android fingerprint api compatible lib, which also combine these api:

Android API：minimum support for Android **6.0** [(more detail)](https://developer.android.com/reference/android/support/v4/hardware/fingerprint/FingerprintManagerCompat.html)

SamSung SDK：minimum support for Android **4.2** [(more detail)](http://developer.samsung.com/galaxy/pass#)

MeiZu SDK：minimum support for Android **5.1** [(more detail)](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI)

Api priority level：Android > Samsung > MeiZu

[中文版介绍 -- 传送门](https://github.com/uccmawei/FingerprintIdentify/blob/master/other/README_ZH.md)

[![](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/QRCode_en.png)](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/demo.apk)

**1. Gradle**

    compile 'com.wei.android.lib:fingerprintidentify:1.2.5'

**2. AndroidManifest**

    <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
    <uses-permission android:name="com.fingerprints.service.ACCESS_FINGERPRINT_MANAGER"/>
    <uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"/>

**3. FingerprintIdentify api**

    mFingerprintIdentify = new FingerprintIdentify(this);       // create object
    mFingerprintIdentify.setSupportAndroidL(true);              // support android L
    mFingerprintIdentify.setExceptionListener(listener);        // exception callback for develop
    mFingerprintIdentify.init();                                // init

    mFingerprintIdentify.isFingerprintEnable();                 // is fingerprint usable
    mFingerprintIdentify.isHardwareEnable();                    // is hardware usable
    mFingerprintIdentify.isRegisteredFingerprint();             // is fingerprint collected
    mFingerprintIdentify.startIdentify(maxTimes, listener);     // start identify
    mFingerprintIdentify.cancelIdentify();                      // cancel identify
    mFingerprintIdentify.resumeIdentify();                      // resume identify

**4. startIdentify method**

    mFingerprintIdentify.startIdentify(3, new BaseFingerprint.IdentifyListener() {
        @Override
        public void onSucceed() {
            // succeed, release hardware automatically
        }

        @Override
        public void onNotMatch(int availableTimes) {
            // not match, try again automatically
        }

        @Override
        public void onFailed(boolean isDeviceLocked) {
            // failed, release hardware automatically
            // isDeviceLocked: is device locked temporarily
        }

        @Override
        public void onStartFailedByDeviceLocked() {
            // the first start failed because the device was locked temporarily
        }
    });

**5. Proguard**

    -ignorewarnings

    # MeiZuFingerprint
    -keep class com.fingerprints.service.** { *; }
    
    # SmsungFingerprint
    -keep class com.samsung.android.sdk.** { *; }

**6. Notice**

    1. Usually, device will be locked temporarily when read incorrect fingerprints continuously 5 times.
       And it need to takes about 30 seconds to get back to normal.
       But it's not standardized, MeiZu SDK has no limit to this.

    2. About 'com.android.support:appcompat-v7:25.3.1' version
       The class FingerprintManagerCompatApi23 will check the feature FEATURE_FINGERPRINT from version 25.
       More info：https://code.google.com/p/android/issues/detail?id=231939

    3. Some manufacturers will transplant standard fingerprint API to the device
       which version less than Android M, such as OPPO.
       But the API will be modified sometimes and cause calling crash, such as VIVO.

    4. We need to check the manufacturers because Meizu's sdk is available on some other devices.

    5. MeiZu's SDK runs abnormally on MeiLan Note3 sometimes, it can't switch to mback mode event called release。

**7. Version Update**

**v1.2.5**　`2018.11.20`　minSdkVersion 21 -> 14.

**v1.2.4**　`2018.11.16`　cancelIdentify will not call onFailed.

**v1.2.3**　`2018.11.15`　Migrate to AndroidX. Support Android L. Support custom identify implementation.

**v1.2.1**　`2017.07.25`　Add new callback onStartFailedByDeviceLocked.

**v1.2.0**　`2017.07.10`　Add android M limit. Add new callback parameter to notice is that device locked temporarily。

**v1.1.5**　`2017.06.14`　~~FingerprintIdentify(Activity)~~ --> FingerprintIdentify(Context).

**v1.1.4**　`2017.05.24`　Remove android M limit, add MeiZu manufacturer verify. See [ISSUE#12](https://github.com/uccmawei/FingerprintIdentify/issues/12)

**v1.1.3**　`2017.05.22`　Update the method getFingerprintManager. See [ISSUE#11](https://github.com/uccmawei/FingerprintIdentify/issues/11)

**v1.1.2**　`2017.04.25`　Modify AOSP's code，avoid the PackageManager.FEATURE_FINGERPRINT limit.

**v1.1.1**　`2017.03.20`　Modify gradle AppCompat lib version.

**v1.1.0**　`2017.03.16`　Modify package name and bug fixed.

**v1.0.2**　`2017.02.17`　Add exception callback.

**v1.0.1**　`2017.02.15`　Bug fixed.

**v1.0.0**　`2017.02.10`　Release v1.

## License ##

Licensed under the MIT License, see the [LICENSE](https://github.com/uccmawei/FingerprintIdentify/blob/master/LICENSE) for copying permission.