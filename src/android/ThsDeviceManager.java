package cn.com.ths.device.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.baidu.location.BDLocation;
import com.github.ihsg.demo.ui.whole.WholePatternCheckingActivity;
import com.github.ihsg.demo.ui.whole.WholePatternSettingActivity;
import com.trustmobi.devicem.DeviceManger;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.ths.trustmobi.safe.activity.TestActivity;
import cn.com.ths.trustmobi.safe.config.AppCache;
import cn.com.ths.trustmobi.safe.config.Server;
import cn.com.ths.trustmobi.safe.push.service.MsgService;
import cn.com.ths.trustmobi.safe.utils.encrypt.AESUtils;
import cn.com.ths.trustmobi.safe.utils.file.FileUtil;
import cn.com.ths.trustmobi.safe.utils.file.ValidateSha1sum;
import cn.com.ths.trustmobi.safe.utils.http.ThsClient;
import cn.com.ths.trustmobi.safe.utils.json.JsonUtils;
import cn.com.ths.trustmobi.safe.utils.loc.LocationManager;
import cn.com.ths.trustmobi.safe.utils.log.LogUtil;
import cn.com.ths.trustmobi.safe.utils.sys.DeviceInfo;
import cn.com.ths.trustmobi.safe.utils.sys.DeviceInfoUtil;

/**
 * This class echoes a string called from JavaScript.
 */
public class ThsDeviceManager extends CordovaPlugin {
    private Context context;
    private String encryptFileKey = "solutionsolution"; //加密的key
    private DeviceManger deviceManger;
    private final String INIT_UPLOAD_DEVICE_INFO_KEY = "UPLOAD_DEVICE_INFO"; // 上传设备信息
    private final String INIT_UPLOAD_NOTICE_RECEIVE_KEY = "UPLOAD_NOTICE_RECEIVE"; // 上传设备远程控制指令下发状态服务地址
    private final String INIT_UPLOAD_LOCATION_KEY = "UPLOAD_LOCATION";// 上传设备位置信息
    private final String INIT_GET_STRATEGY_KEY = "GET_STRATEGY";// 获取策略信息
    private final String INIT_EQUIP_ACTIVE_KEY = "EQUIP_ACTIVE";// 上传设备管理器激活状态
    private final String INIT_UPLOAD_EVENT_KEY = "UPLOAD_EVENT";// 上传事件
    private final String INIT_EFENCECONFIG_EVENT_KEY = "EFENCECONFIG_EVENT";// 获取地理围栏信息
    private final String INIT_VALIDATE_APP_CODE_KEY = "VALIDATE_APP_CODE";// 验证App 是否完整
    private final String INIT_UPLOAD_EFENCETRIGGER_INFO_KEY = "UPLOAD_EFENCETRIGGER_INFO";// 触发围栏报警信息到服务器端
    private final String INIT_QR_CODE_LOGIN_KEY = "QR_CODE_LOGIN";// 扫描二维码登录
    private final String INIT_LOGINNAME_KEY = "loginName";// 用户名
    private final String INIT_PASSWORD_KEY = "password";// 密码

    /**
     * 初始化插件
     *
     * @param cordova
     * @param webView
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.context = cordova.getActivity();
        deviceManger = new DeviceManger(context);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("init")) {// 初始化配置，主要是信息上报地址等
            String configStr = args.getString(0);
            this.init(configStr, callbackContext);
            return true;
        } else if(action.equals("setUser")){ // 设置用户信息
            String user = args.getString(0);
            this.setUser(user,callbackContext);
            return true;
        } else if (action.equals("startService")) { // 启动服务
            Intent i = new Intent(this.context, MsgService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.context.startForegroundService(i);
            } else {
                this.context.startService(i);
            }
            return true;
        } else if (action.equals("getDeviceInfo")) { // 获取设备信息
            DeviceInfo deviceInfo = DeviceInfoUtil.getInstance(context).getDeviceTotalInfo();
            callbackContext.success(JsonUtils.toJson(deviceInfo));
            return true;
        } else if (action.equals("verifyApp")) { // 验证app
            ValidateSha1sum validateSha1sum = new ValidateSha1sum((Activity) context);
            validateSha1sum.validateSha1sum();
            return true;
        } else if (action.equals("startLoc")) { // 获取位置
            LocationManager locationManager = LocationManager.getInstance(context, "gcj02", 1000);
            // 开始定位
            locationManager.startLocation(true);
            locationManager.setLocationCallBack(new LocationManager.LocationCallback() {
                @Override
                public void getLocation(BDLocation location) {
//                    Log.e("locationManager", location.toString());
                    callbackContext.success(location.toString());
                }
            });
            return true;
        } else if (action.equals("encryptFile")) { // 加密文件
            // 待加密的文件地址
            String encryptFilePath = args.getString(0);
            // 加密的后的文件地址
            String decryptFilePath = args.getString(1);
            byte[] fileByteContent = FileUtil.bigFile2Bytes(encryptFilePath);
            byte[] encryptFileByte = AESUtils.encryptData(encryptFileKey, fileByteContent);
            //将加密后的数据，存储到新的路径
            FileUtil.bytes2File(encryptFileByte, decryptFilePath);
            callbackContext.success(decryptFilePath);
            return true;
        } else if (action.equals("decryptionFile")) { // 解密文件
            // 加密的后的文件地址
            String decryptFilePath = args.getString(0);
            // 解密后的文件地址
            String filePath = args.getString(1);

            byte[] fileByteContent = FileUtil.bigFile2Bytes(decryptFilePath);
            byte[] decryptFileByte = AESUtils.decryptData(encryptFileKey, fileByteContent);
            // 将解密后的文件，恢复到原来的路径
            FileUtil.bytes2File(decryptFileByte, filePath);
            callbackContext.success(filePath);
            return true;
        } else if (action.equals("enableDeviceManager")) { // 激活设备管理器
            deviceManger.enableDeviceManager();
            return true;
        } else if (action.equals("disableDeviceManager")) { // 取消设备管理器
            deviceManger.disableDeviceManager();
            return true;
        } else if (action.equals("setPwd")) { // 设置手势密码
            context.startActivity(new Intent(context, WholePatternSettingActivity.class));
            return true;
        } else if (action.equals("veryPwd")) { // 验证手势密码
            context.startActivity(new Intent(context, WholePatternCheckingActivity.class));
            return true;
        } else if (action.equals("qrCodeLogin")) { // 二维码验证登录
            String loginName = args.getString(0); // 用户
            String password = args.getString(1); // 密码
            String token = args.getString(2); // 二维码token
            if (loginName != null && loginName.length() > 0&&password != null && password.length() > 0&&token != null && token.length() > 0) {
                ThsClient.getInstance().qrCodeLogin(loginName, password, token, new ThsClient.ClientCallBack() {
                    @Override
                    public void getHttpRes(String response) {
                        callbackContext.success(response);
                    }
                });

            } else {
                callbackContext.error("Expected one non-empty string argument.");
            }


        }
        return false;
    }

    /**
     *配置app 所需要的服务地址信息，该方法需要在app启动最开始就调用
     * @param configStr 配置信息JSON字符串
     * {
     * 	"UPLOAD_DEVICE_INFO":"上传设备信息地址",
     * 	"UPLOAD_NOTICE_RECEIVE":"上传设备远程控制质量下发状态服务地址",
     * 	"UPLOAD_LOCATION":"上传设备位置信息地址",
     * 	"GET_STRATEGY":"获取设备策略信息地址",
     * 	"EQUIP_ACTIVE":"上传设备管理器激活状态地址",
     * 	"UPLOAD_EVENT":"上传事件地址",
     * 	"EFENCECONFIG_EVENT":"获取地理围栏信息地址",
     * 	"VALIDATE_APP_CODE":"验证App 是否完整地址",
     * 	"UPLOAD_EFENCETRIGGER_INFO":"触发围栏报警信息到服务器端地址",
     * 	"QR_CODE_LOGIN":"扫描二维码登录",
     *        }
     * @param callbackContext 回调
     */
    private void init(String configStr, CallbackContext callbackContext) {
        if (configStr != null && configStr.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(configStr);
                // 上传设备信息地址
                Server.UPLOAD_DEVICE_INFO = jsonObject.getString(INIT_UPLOAD_DEVICE_INFO_KEY);
                //  上传设备远程控制指令下发状态服务地址
                Server.UPLOAD_NOTICE_RECEIVE = jsonObject.getString(INIT_UPLOAD_NOTICE_RECEIVE_KEY);
                // 上传设备位置信息
                Server.UPLOAD_LOCATION = jsonObject.getString(INIT_UPLOAD_LOCATION_KEY);
                //  获取策略信息
                Server.GET_STRATEGY = jsonObject.getString(INIT_GET_STRATEGY_KEY);
                //  上传设备管理器激活状态
                Server.EQUIP_ACTIVE = jsonObject.getString(INIT_EQUIP_ACTIVE_KEY);
                // 上传事件
                Server.UPLOAD_EVENT = jsonObject.getString(INIT_UPLOAD_EVENT_KEY);
                // 获取地理围栏信息
                Server.EFENCECONFIG_EVENT = jsonObject.getString(INIT_EFENCECONFIG_EVENT_KEY);
                // 验证App 是否完整
                Server.VALIDATE_APP_CODE = jsonObject.getString(INIT_VALIDATE_APP_CODE_KEY);
                // 触发围栏报警信息到服务器端
                Server.UPLOAD_EFENCETRIGGER_INFO = jsonObject.getString(INIT_UPLOAD_EFENCETRIGGER_INFO_KEY);
                //扫描二维码登录
                Server.QR_CODE_LOGIN = jsonObject.getString(INIT_QR_CODE_LOGIN_KEY);
//                Server.UPLOAD_DEVICE_INFO = "http://192.168.0.101:8084/ths-move/Equipment/api/login.vm";
//                Server.UPLOAD_NOTICE_RECEIVE = "http://192.168.0.101:8084/ths-move/Equipment/api/uploadNoticeReceive.vm";
//                Server.UPLOAD_LOCATION = "http://192.168.0.101:8084/ths-move/app/upLocInfo.vm";
//                Server.GET_STRATEGY = "http://192.168.0.101:8084/ths-move/Equipment/api/strategy.vm";
//                Server.EQUIP_ACTIVE = "http://192.168.0.101:8084/ths-move/Equipment/api/equipActive.vm";
//                Server.UPLOAD_EVENT = "http://192.168.0.101:8084/ths-move/warning/api/uploadEvent.vm";
//                Server.EFENCECONFIG_EVENT = "http://192.168.0.101:8084/ths-move/Equipment/api/getEfenceConfig.vm";
//                Server.VALIDATE_APP_CODE = "http://192.168.0.101:8084/ths-move/app/validateAppCode.vm";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            callbackContext.success(configStr);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    /**
     * 设置用户信息
     * @param userStr 用户信息JSON
     * @param callbackContext 回调
     */
    private void setUser(String userStr, CallbackContext callbackContext){
        if (userStr != null && userStr.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(userStr);
                AppCache.loginName = jsonObject.getString(INIT_LOGINNAME_KEY);
                AppCache.password = jsonObject.getString(INIT_PASSWORD_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackContext.success(userStr);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
