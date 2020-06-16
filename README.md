# cordova-plugin-ths-devicemanager
移动管理平台设备管理cordova插件
## 支持平台

Android

## 安装插件

```
# 通过npm 安装插件
cordova plugin add cordova-plugin-ths-devicemanager --variable BD_AK='cU0kobAWaMKvarnVFYaDq' --variable HMS_APPID='1212121' --variable ALIBABA_APPKEY='1212121' --variable ALIBABA_APPSECRET='cccsasas'
# 通过github安装
cordova plugin add https://github.com/THS-FE/cordova-plugin-ths-devicemanager  --variable BD_AK='cU0kobAWaMKvarnVFYaDq' --variable HMS_APPID='1212121' --variable ALIBABA_APPKEY='1212121' --variable ALIBABA_APPSECRET='cccsasas'
# 通过本地文件路径安装
cordova plugin add 文件路径
```

**说明： ionic 项目命令前加上ionic，即ionic cordova plugin xxxxx**

## 配置文件修改

在config.xml文件中**platform name="android"**节点下添加以下配置

````xml
<edit-config file="app/src/main/AndroidManifest.xml" mode="merge" target="/manifest/application">
            <application android:name="cn.com.ths.patternlocker.MainApplication" />
</edit-config>
````

**注意：由于一个应用只能存在一个application，如果项目中其他插件使用到了application，需要修改该插件**

找到MainApplication.java文件，在下边代码中添加其他插件的或者aar中Application对应的类

```java
private static final String[] MODULESLIST =

    {"com.github.ihsg.demo.PatternLockApplication",

       "com.moduleA.B"};
```

## 使用方法

设置手势密码

```java
cordova.plugins.ThsPatternLocker.setPwd((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
});
```

验证手势密码

```java
cordova.plugins.ThsPatternLocker.veryPwd((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
 });
```

监测手势密码设置成功(一般放在页面初始化位置)

```javascript
document.addEventListener('thsPatternLocker.onVeryPwdReceiver', data => {
      console.log(data);
      alert(JSON.stringify(data));
}, false);
```

监听手势密码验证成功（一般放在页面初始化位置）

```javascript
document.addEventListener('thsPatternLocker.onSetPwdReceiver', data => {
      console.log(data);
      alert(JSON.stringify(data));
}, false);
```

**说明：使用ts 进行开发时，需要在文件上变声明下declare let cordova，不然会报错;**

```typescript
import { Component, OnInit, Input } from '@angular/core';
import { WebIntent } from '@ionic-native/web-intent/ngx';
declare let cordova;
@Component({
  selector: 'app-explore-container',
  templateUrl: './explore-container.component.html',
  styleUrls: ['./explore-container.component.scss'],
})
```

## 常见错误

### 打包报错  unbound prefix.

```
Execution failed for task ':app:mergeDebugResources'.
> java.util.concurrent.ExecutionException: com.android.builder.internal.aapt.v2.Aapt2Exception: Android resource compilation failed   
  D:\training\20200521\Test20200521\platforms\android\app\src\main\res\xml\config.xml:46: error: unbound prefix.
```

修改config.xml,添加 xmlns:android="http://schemas.android.com/apk/res/android"

```xml
<widget id="io.ionic.starter" version="0.0.1" xmlns="http://www.w3.org/ns/widgets" xmlns:android="http://schemas.android.com/apk/res/android" xmlns:cdv="http://cordova.apache.org/ns/1.0">
```
