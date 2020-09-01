package cn.com.ths.device.manager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.github.ihsg.demo.IComponentApplication;
import cn.com.ths.trustmobi.safe.config.AppCache;
import cn.com.ths.trustmobi.safe.utils.http.ThsHttpClient;
import cn.com.ths.trustmobi.safe.utils.loc.GeoFenceManager;
import cn.com.ths.trustmobi.safe.utils.storage.PreferenceManager;

public class MainApplication extends Application {
    private static final String TAG = "Init";
    private static final String[] MODULESLIST =
            {"com.github.ihsg.demo.PatternLockApplication",
                    "com.moduleA.B"};
    private static MainApplication instante;
    public synchronized static MainApplication getInstance() {
        return instante;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instante = this;
        initCloudChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "ths";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "您有新的消息";
            // 用户可以看到的通知渠道的描述
            String description = "点击打开应用";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
        // App 缓存上下文对象
        AppCache.setContext(this);
        // 初始化首选项
        PreferenceManager.init(this);
        // 电子围栏初始化
        GeoFenceManager.getInstance().init(this);
        // 初始化httpClient
        ThsHttpClient.getInstance().init(this);
        // 加载依赖模块的App
        modulesApplicationInit();
    }
    /**
     * 初始化云推送通道,  移动推送的初始化必须在Application中，不能放到Activity中执行。移动推送在初始化过程中将启动后台进程channel，必须保证应用进程和channel进程都执行到推送初始化代码。
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
                // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
                HuaWeiRegister.register(MainApplication.this);
                // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
                MiPushRegister.register(MainApplication.this, "2882303761518355388", "5261835537388");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    private void modulesApplicationInit(){
        for (String moduleImpl : MODULESLIST){
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IComponentApplication){
                    ((IComponentApplication) obj).init(MainApplication.getInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
