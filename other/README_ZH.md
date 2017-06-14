# FingerprintIdentify

这是一个可拓展的Android指纹识别API兼容库，目前集成了 安卓标准API 以及 [三星](http://developer.samsung.com/galaxy/pass#) 和 [魅族](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI) 的指纹SDK。

重点！重点！重点！本库对安卓系统版本没有任何限制！安卓6.0以下的系统也能正常使用！

API调用优先级：安卓原生 > 三星SDK > 魅族SDK

[![](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/QRCode_zh.png)](https://github.com/uccmawei/FingerprintIdentify/raw/master/other/demo.apk)

**1. Gradle 添加引用**

    compile 'com.wei.android.lib:fingerprintidentify:1.1.5'

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

    1. 关于 'com.android.support:appcompat-v7:25.3.1' 版本问题
       从25版本开始 FingerprintManagerCompatApi23 类需要检查 FEATURE_FINGERPRINT 特性。
       具体可以参考：https://code.google.com/p/android/issues/detail?id=231939

    2. 国内个别手机产商会移植安卓6.0的标准指纹API到6.0以下的系统中使用，比如OPPO。

    3. 由于魅族指纹SDK可能会在某些系统上可以运行，所以需要加个设备判断。

**7. 更新记录**

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