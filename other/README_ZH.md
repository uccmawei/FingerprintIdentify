# FingerprintIdentify

这是一个可拓展的Android指纹识别API兼容库，目前集成了 安卓原生API 以及 [三星](http://developer.samsung.com/galaxy/pass#) 和 [魅族](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI) 的指纹SDK。

三星和魅族的指纹SDK支持大部分系统低于安卓6.0的设备。

API调用优先级：安卓原生 > 三星SDK > 魅族SDK

[![](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/QRCode_zh.png)](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/demo.apk)

**1. Gradle 添加引用**

    compile 'com.wei.android.lib:fingerprintidentify:1.1.2'

**2. AndroidManifest 添加权限**

    <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
    <uses-permission android:name="com.fingerprints.service.ACCESS_FINGERPRINT_MANAGER"/>
    <uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"/>

**3. FingerprintIdentify 方法解释**

    mFingerprintIdentify = new FingerprintIdentify(this);                       // 构造对象
    mFingerprintIdentify = new FingerprintIdentify(this, exceptionListener);    // 构造对象，并监听错误回调
    mFingerprintIdentify.isFingerprintEnable();                                 // 指纹硬件可用并已经录入指纹
    mFingerprintIdentify.isHardwareEnable();                                    // 指纹硬件是否可用
    mFingerprintIdentify.isRegisteredFingerprint();                             // 是否已经录入指纹
    mFingerprintIdentify.startIdentify(maxTimes, listener);                     // 开始验证指纹识别
    mFingerprintIdentify.cancelIdentify();                                      // 关闭指纹识别
    mFingerprintIdentify.resumeIdentify();                                      // 恢复指纹识别并保证错误次数不变

**4. startIdentify 方法解析**

    mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
        @Override
        public void onSucceed() {
            // 验证成功，自动结束指纹识别
        }

        @Override
        public void onNotMatch(int availableTimes) {
            // 指纹不匹配，并返回可用剩余次数并自动继续验证
        }

        @Override
        public void onFailed() {
            // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
        }
    });

**5. 混淆设置**

    # MeiZuFingerprint
    -keep class com.fingerprints.service.** { *; }
    
    # SmsungFingerprint
    -keep class com.samsung.android.sdk.** { *; }

**6. 相关资料**

https://code.google.com/p/android/issues/detail?id=231939

**7. 更新记录**

**v1.1.2**　`2017.04.25`　修改AOSP源码，避开 PackageManager.FEATURE_FINGERPRINT 的限制

**v1.1.1**　`2017.03.20`　AppCompat支持库从25.2.0降级到23.4.0

**v1.1.0**　`2017.03.16`　调整包名，BUG FIXED

**v1.0.2**　`2017.02.17`　新增异常回调接口

**v1.0.1**　`2017.02.15`　BUG FIXED

**v1.0.0**　`2017.02.10`　发布第一版本


## License ##

Licensed under the MIT License, see the [LICENSE](https://github.com/uccmawei/FingerprintIdentify/blob/master/LICENSE) for copying permission.