# cordova-plugin-ths-devicemanager
移动管理平台设备管理cordova插件，实现对设备的远程控制，终端安全管控。
## 支持平台

Android

## 安装插件

```
# 通过npm 安装插件
cordova plugin add cordova-plugin-ths-devicemanager --variable BD_AK=cU0kobAWaMKvarnVFYaDq --variable HMS_APPID=1212121 --variable ALIBABA_APPKEY=1212121 --variable ALIBABA_APPSECRET=cccsasas
# 通过github安装
cordova plugin add https://github.com/THS-FE/cordova-plugin-ths-devicemanager  --variable BD_AK=cU0kobAWaMKvarnVFYaDq --variable HMS_APPID=1212121 --variable ALIBABA_APPKEY=1212121 --variable ALIBABA_APPSECRET=cccsasas
# 通过本地文件路径安装
cordova plugin add 文件路径
```

参数说明：

1. BD_AK 百度定位安卓SDK 注册的AK

   [BD_AK申请](http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/create-project/key)

2. HMS_APPID 华为云注册的appID,用于华为推送

   需要注册华为开发者联盟账号（生产环境下，使用公司统一账号，加入开发团队的方式自行注册应用）

3. ALIBABA_APPKEY 阿里云推送 APP KEY

   需要注册阿里云推送开发者后获取

4. ALIBABA_APPSECRET  阿里云推送  应用密钥

   需要注册阿里云推送开发者后获取

**说明： ionic 项目命令前加上ionic，即ionic cordova plugin xxxxx**

## 配置文件修改

在config.xml文件中**platform name="android"**节点下添加以下配置

````xml
<edit-config file="app/src/main/AndroidManifest.xml" mode="merge" target="/manifest/application">
            <application android:name="cn.com.ths.device.manager.MainApplication" />
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

##### 初始化配置

```javascript
    const baseUrl = 'http://192.168.0.101:8084/ths-move/'; // 移动应用平台基础地址
    cordova.plugins.thsdevicemanager.init(baseUrl, (res) => {
      console.log('成功');
      alert(res);
    }, (err) => {
      console.log(err);
    });
```

##### 设置用户信息

```javascript
const user = {
      loginName: 'lijxa',
      password: '1'
    };
    cordova.plugins.thsdevicemanager.setUser(user, (res) => {
      console.log('成功');
      alert(res);
    }, (err) => {
      console.log(err);
    });
```

##### 启动服务

```javascript
cordova.plugins.thsdevicemanager.startService((res) => {
      console.log('成功');
      alert(res);
    }, (err) => {
      console.log(err);
 });
```

##### 启用设备管理器

```javascript
cordova.plugins.thsdevicemanager.enableDeviceManager((res) => {
      console.log('成功');
      alert(res);
 // 上报设备管理器状态     cordova.plugins.thsdevicemanager.updateDeviceActiveStatus((success) => {
        console.log(success);
      }, (error) => {
        console.log(error);
      });
    }, (err) => {
      console.log(err);
    });
```

##### 取消设备管理器

```javascript
cordova.plugins.thsdevicemanager.disableDeviceManager((res) => {
      console.log('成功');
      alert(res);
  // 上报设备管理器状态       cordova.plugins.thsdevicemanager.updateDeviceActiveStatus((success) => {
        console.log(success);
      }, (error) => {
        console.log(error);
      });
    }, (err) => {
      console.log(err);
    });
```

##### 获取设备信息

```javascript
cordova.plugins.thsdevicemanager.getDeviceInfo((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
    });
```

##### 验证app完整性

```javascript
cordova.plugins.thsdevicemanager.verifyApp((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
});
```

##### 定位

```javascript
cordova.plugins.thsdevicemanager.startLoc((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
});
```

##### 设置手势密码

```java
cordova.plugins.thsdevicemanager.setPwd((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
});
```

##### 验证手势密码

```java
cordova.plugins.thsdevicemanager.veryPwd((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
 });
```

##### 监测手势密码设置成功(一般放在页面初始化位置)

```javascript
document.addEventListener('thsDeviceManager.onVeryPwdReceiver', data => {
      console.log(data);
      alert(JSON.stringify(data));
}, false);
```

##### 监听手势密码验证成功（一般放在页面初始化位置）

```javascript
document.addEventListener('thsDeviceManager.onSetPwdReceiver', data => {
      console.log(data);
      alert(JSON.stringify(data));
}, false);
```

##### 上传设备信息

```javascript
 cordova.plugins.thsdevicemanager.upLoadDeviceInfo((success) => {
      console.log(success);
    }, (error) => {
      console.log(error);
    });
```

##### 加密文件

TODO

##### 解密文件

TODO

##### 二维码登录

TODO

**说明：使用ts 进行开发时，需要在文件上变声明下declare const cordova，不然会报错;**

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

### 打包时，出现androidx和android v4包冲突，导致报错

```bash
# 安装以下插件，重新build项目即可。出现这个错误是因为，目前一些新的插件已采用Androidx来统一依赖库，包括本插件
cordova plugin add cordova-plugin-androidx
cordova plugin add cordova-plugin-androidx-adapter
```



### 与百度定位插件冲突

如果已有项目已经安装有百度定位插件或者相关百度定位的功能，会出现引入依赖库重复的问题。

这里只介绍百度定位插件冲突解决。

冲突错误如下：

```
Duplicate class com.baidu.location.f found in modules jetified-BEaiduLBS_ Android.jar (BaiduLBS_Aandroid.jr) and jetified-torap-runtime. jar
```

解决：

1. 移除百度定位插件

2. 修改百度定位插件方法navigator.baidulocation.get为cordova.plugins.thsdevicemanager.startLoc

3. 为保证该插件安装配置成功，重新安装该插件

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

