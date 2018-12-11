# FingerprintIdentify

这是一个可拓展的Android指纹识别API兼容库，目前集成了以下API：

安卓API：最低支持安卓**6.0**系统 [(查看详细介绍)](https://developer.android.com/reference/android/support/v4/hardware/fingerprint/FingerprintManagerCompat.html)

三星SDK：最低支持安卓**4.2**系统 [(查看详细介绍)](http://developer.samsung.com/galaxy/pass#)

魅族SDK：最低支持安卓**5.1**系统 [(查看详细介绍)](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI)

API调用优先级：安卓API > 三星SDK > 魅族SDK

[![](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/QRCode_zh.png)](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/demo.apk)

**1. Gradle 添加引用**

    compile 'com.wei.android.lib:fingerprintidentify:1.2.6'

**2. AndroidManifest 添加权限**

    <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
    <uses-permission android:name="com.fingerprints.service.ACCESS_FINGERPRINT_MANAGER"/>
    <uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"/>

**3. FingerprintIdentify 方法解释**

    mFingerprintIdentify = new FingerprintIdentify(this);       // 构造对象
    mFingerprintIdentify.setSupportAndroidL(true);              // 支持安卓L及以下系统
    mFingerprintIdentify.setExceptionListener(listener);        // 错误回调（错误仅供开发使用）
    mFingerprintIdentify.init();                                // 初始化，必须调用

    mFingerprintIdentify.isFingerprintEnable();                 // 指纹硬件可用并已经录入指纹
    mFingerprintIdentify.isHardwareEnable();                    // 指纹硬件是否可用
    mFingerprintIdentify.isRegisteredFingerprint();             // 是否已经录入指纹
    mFingerprintIdentify.startIdentify(maxTimes, listener);     // 开始验证指纹识别
    mFingerprintIdentify.cancelIdentify();                      // 关闭指纹识别
    mFingerprintIdentify.resumeIdentify();                      // 恢复指纹识别并保证错误次数不变

**4. startIdentify 方法解析**

    mFingerprintIdentify.startIdentify(3, new BaseFingerprint.IdentifyListener() {
        @Override
        public void onSucceed() {
            // 验证成功，自动结束指纹识别
        }

        @Override
        public void onNotMatch(int availableTimes) {
            // 指纹不匹配，并返回可用剩余次数并自动继续验证
        }

        @Override
        public void onFailed(boolean isDeviceLocked) {
            // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
            // isDeviceLocked 表示指纹硬件是否被暂时锁定
        }

        @Override
        public void onStartFailedByDeviceLocked() {
            // 第一次调用startIdentify失败，因为设备被暂时锁定
        }
    });

**5. 混淆设置**

    -ignorewarnings

    # MeiZuFingerprint
    -keep class com.fingerprints.service.** { *; }
    
    # SmsungFingerprint
    -keep class com.samsung.android.sdk.** { *; }

**6. 相关资料**

    1. 通常指纹硬件在连续识别错误5次后，就会暂时锁定硬件，需要等30s左右才能再次恢复使用。
       这个因设备而异，比如魅族的SDK就完全没有次数限制。

    2. 关于 'com.android.support:appcompat-v7:25.3.1' 版本问题
       从25版本开始 FingerprintManagerCompatApi23 类需要检查 FEATURE_FINGERPRINT 特性。
       具体可以参考：https://code.google.com/p/android/issues/detail?id=231939

    3. 国内个别手机产商会移植安卓6.0的标准指纹API到6.0以下的系统中使用，比如OPPO。
       但是移植后的API存在被修改的可能，可能会导致程序奔溃，比如VIVO。

    4. 由于魅族指纹SDK可能会在某些系统上可以正常调用部分API，所以需要加个设备判断。

    5. 魅族的指纹SDK在魅蓝NOTE3上也可能出现功能异常，比如调用release后也不能恢复mback模式。

**7. 更新记录**

**v1.2.6**　`2018.12.11`　支持 AndroidX android.enableJetifier=true.

**v1.2.5**　`2018.11.20`　minSdkVersion 21 -> 14.

**v1.2.4**　`2018.11.16`　调用 cancelIdentify 关闭指纹识别不回调 onFailed。

**v1.2.3**　`2018.11.15`　适配AndroidX。支持AndroidL及以下系统。支持自定义实现指纹识别。

**v1.2.1**　`2017.07.25`　新增回调接口：设备被暂时锁定后，初次调用startIdentify会回调此接口。

**v1.2.0**　`2017.07.10`　恢复安卓6.0的版本限制；新增连续多次识别错误导致设备指纹功能暂时锁定回调参数。

**v1.1.5**　`2017.06.14`　~~FingerprintIdentify(Activity)~~ --> FingerprintIdentify(Context).

**v1.1.4**　`2017.05.24`　移除安卓6.0的版本限制，添加魅族设备验证，因为 [ISSUE#12](https://github.com/uccmawei/FingerprintIdentify/issues/12)

**v1.1.3**　`2017.05.22`　更新方法 getFingerprintManager，因为 [ISSUE#11](https://github.com/uccmawei/FingerprintIdentify/issues/11)

**v1.1.2**　`2017.04.25`　修改AOSP源码，避开 PackageManager.FEATURE_FINGERPRINT 的限制

**v1.1.1**　`2017.03.20`　AppCompat支持库从25.2.0降级到23.4.0

**v1.1.0**　`2017.03.16`　调整包名，BUG FIXED

**v1.0.2**　`2017.02.17`　新增异常回调接口

**v1.0.1**　`2017.02.15`　BUG FIXED

**v1.0.0**　`2017.02.10`　发布第一版本

## License ##

Licensed under the MIT License, see the [LICENSE](https://github.com/uccmawei/FingerprintIdentify/blob/master/LICENSE) for copying permission.