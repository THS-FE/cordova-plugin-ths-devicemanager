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
import cn.com.ths.trustmobi.safe.config.Server;
import cn.com.ths.trustmobi.safe.push.service.MsgService;
import cn.com.ths.trustmobi.safe.utils.encrypt.AESUtils;
import cn.com.ths.trustmobi.safe.utils.file.FileUtil;
import cn.com.ths.trustmobi.safe.utils.file.ValidateSha1sum;
import cn.com.ths.trustmobi.safe.utils.json.JsonUtils;
import cn.com.ths.trustmobi.safe.utils.loc.LocationManager;
import cn.com.ths.trustmobi.safe.utils.sys.DeviceInfo;
import cn.com.ths.trustmobi.safe.utils.sys.DeviceInfoUtil;

/**
 * This class echoes a string called from JavaScript.
 */
public class ThsDeviceManager extends CordovaPlugin {
     private Context context;
    private String encryptFileKey = "solutionsolution"; //加密的key
    private DeviceManger deviceManger;
    /**
     * 初始化插件
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
        // 初始化配置，主要是信息上报地址，用户信息邓
        if (action.equals("init")) {
            String configStr = args.getString(0);
            this.init(configStr, callbackContext);
            return true;
        }else if(action.equals("startService")){ // 启动服务
            Intent i = new Intent(this.context, MsgService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.context.startForegroundService(i);
            } else {
                this.context.startService(i);
            }
            return true;
        }else if(action.equals("getDeviceInfo")){ // 获取设备信息
            DeviceInfo deviceInfo = DeviceInfoUtil.getInstance(context).getDeviceTotalInfo();
            callbackContext.success(JsonUtils.toJson(deviceInfo));
            return true;
        }else if(action.equals("verifyApp")){ // 验证app
            // String sha1sumInfoUrl = args.getString(0);
            // "https://www.ths.com.cn/Android/test/sha1.txt"
            ValidateSha1sum validateSha1sum = new ValidateSha1sum((Activity) context);
            validateSha1sum.validateSha1sum();
        }else if(action.equals("startLoc")){ // 获取位置
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
        }else if(action.equals("encryptFile")){ // 加密文件
            // 待加密的文件地址
            String encryptFilePath = args.getString(0);
            // 加密的后的文件地址
            String decryptFilePath = args.getString(1);
            byte[] fileByteContent = FileUtil.bigFile2Bytes(encryptFilePath);
            byte[] encryptFileByte = AESUtils.encryptData(encryptFileKey, fileByteContent);
            //将加密后的数据，存储到新的路径
            FileUtil.bytes2File(encryptFileByte, decryptFilePath);
            callbackContext.success(decryptFilePath);
        }else if(action.equals("decryptionFile")){ // 解密文件
            // 加密的后的文件地址
            String decryptFilePath = args.getString(0);
            // 解密后的文件地址
            String filePath = args.getString(1);

            byte[] fileByteContent = FileUtil.bigFile2Bytes(decryptFilePath);
            byte[] decryptFileByte = AESUtils.decryptData(encryptFileKey, fileByteContent);
            // 将解密后的文件，恢复到原来的路径
            FileUtil.bytes2File(decryptFileByte, filePath);
            callbackContext.success(filePath);
        }else if(action.equals("enableDeviceManager")){ // 激活设备管理器
            deviceManger.enableDeviceManager();
        }else if(action.equals("disableDeviceManager")){ // 取消设备管理器
            deviceManger.disableDeviceManager();
        }else if(action.equals("setPwd")){ // 设置手势密码
            context.startActivity(new Intent(context, WholePatternSettingActivity.class));
        }else if(action.equals("veryPwd")){ // 验证手势密码
            context.startActivity(new Intent(context, WholePatternCheckingActivity.class));
        }
        return false;
    }

    // 初始化配置
    private void init(String configStr, CallbackContext callbackContext) {
        if (configStr != null && configStr.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(configStr);
                Server.UPLOAD_DEVICE_INFO = "http://192.168.0.101:8084/ths-move/Equipment/api/login.vm";
                Server.UPLOAD_NOTICE_RECEIVE = "http://192.168.0.101:8084/ths-move/Equipment/api/uploadNoticeReceive.vm";
                Server.UPLOAD_LOCATION = "http://192.168.0.101:8084/ths-move/app/upLocInfo.vm";
                Server.GET_STRATEGY = "http://192.168.0.101:8084/ths-move/Equipment/api/strategy.vm";
                Server.EQUIP_ACTIVE = "http://192.168.0.101:8084/ths-move/Equipment/api/equipActive.vm";
                Server.UPLOAD_EVENT = "http://192.168.0.101:8084/ths-move/warning/api/uploadEvent.vm";
                Server.EFENCECONFIG_EVENT = "http://192.168.0.101:8084/ths-move/Equipment/api/getEfenceConfig.vm";
                Server.VALIDATE_APP_CODE = "http://192.168.0.101:8084/ths-move/app/validateAppCode.vm";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            callbackContext.success(configStr);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
