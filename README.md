# FingerprintIdentify

Android指纹识别API兼容库，目前整合了安卓原生API以及三星和魅族的指纹SDK，支持继续拓展。

Usage
-----
**1. 添加引用**

    compile 'com.awei.android.lib:fingerprintidentify:1.0.0'

**2. 添加指纹识别权限**

    <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
    <uses-permission android:name="com.fingerprints.service.ACCESS_FINGERPRINT_MANAGER"/>
    <uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"/>

**3. FingerprintIdentify方法解释**

    mFingerprintIdentify = new FingerprintIdentify(this);			// 构造对象
	mFingerprintIdentify.isFingerprintEnable();						// 指纹硬件可用并已经录入指纹
	mFingerprintIdentify.isHardwareEnable();						// 指纹硬件是否可用
	mFingerprintIdentify.isRegisteredFinger();						// 是否已经录入指纹
	mFingerprintIdentify.startIdentify(maxTimes, listener);			// 开始验证指纹识别
	mFingerprintIdentify.cancelIdentify();							// 关闭指纹识别
	mFingerprintIdentify.resumeIdentify();							// 恢复指纹识别并保证错误次数不变

**4. startIdentify方法解析**

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
                // 错误次数达到上线或者API报错停止了验证，自动结束指纹识别
            }
    });

**5. 其他说明**

compile 'com.android.support:appcompat-v7:23.4.0'

这里如果使用25版本的兼容库，可能会导致部分即使是6.0系统的机型也不能正常使用指纹识别，具体请客参考：

[https://code.google.com/p/android/issues/detail?id=231939](https://code.google.com/p/android/issues/detail?id=231939 "code.google.com")

**6. 更新历史**

**v1.0.1** `2017.02.15` 修正三星指纹API调用（开始识别和关闭指纹都必须在主线程调用）

**v1.0.0** `2017.02.10` 发布第一版本