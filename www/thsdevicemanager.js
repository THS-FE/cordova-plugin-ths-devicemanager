var exec = require('cordova/exec');
/**
 * 初始化 
 * @param config 服务地址等信息
 * {
	"UPLOAD_DEVICE_INFO":"上传设备信息地址",
	"UPLOAD_NOTICE_RECEIVE":"上传设备远程控制质量下发状态服务地址",
	"UPLOAD_LOCATION":"上传设备位置信息地址",
	"GET_STRATEGY":"获取设备策略信息地址",
	"EQUIP_ACTIVE":"上传设备管理器激活状态地址",
	"UPLOAD_EVENT":"上传事件地址",
	"EFENCECONFIG_EVENT":"获取地理围栏信息地址",
	"VALIDATE_APP_CODE":"验证App 是否完整地址",
	"UPLOAD_EFENCETRIGGER_INFO":"触发围栏报警信息到服务器端地址",
	"QR_CODE_LOGIN":"扫描二维码登录",
	}
 */
exports.init = function (config, success, error) {
    exec(success, error, 'ThsDeviceManager', 'init', [config]);
};

/**
 * 设置用户信息 
 * @param user 用户信息
 * {
	"loginName":"",
	"password":""
	}
 */
exports.setUser = function (user, success, error) {
    exec(success, error, 'ThsDeviceManager', 'setUser', [user]);
};

/**
 * 上传设备信息
 */
exports.upLoadDeviceInfo = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'upLoadDeviceInfo', []);
};

/**
 * 更新设备激活状态信息
 */
exports.updateDeviceActiveStatus = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'updateDeviceActiveStatus', []);
};


/**
 * 启动服务
 */
exports.startService = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'startService', []);
};

/**
 * 获取设备信息
 * 返回：{"availMemory":760209408,"battery":"92 %","carrierName":"Smart","countryCode":"kh","deviceBoard":"unknown",
 * "deviceBrand":"ZTE","deviceName":"P650A30","imei":"861672030107829","ipAddress":"192.168.99.126",
 * "isEmulator":false,"isGoogleSdk":false,"isRooted":false,"isSecured":false,"manufacturer":"ZTE","mcc":"456",
 * "meid":"861672030107829","mnc":"06","model":"ZTE BV0720T","netMode":"WIFI","netOperator":"未知","phoneNum":"+85516223570",
 * "pushDeviceId":"7aed586dcc4d493588685e9758116105","resolution":"720*1280","screenSize":"4.6","systemVersion":"6.0",
 * "uniqueID":"861672030107829","wifiMacAddress":""}
 */
exports.getDeviceInfo = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'getDeviceInfo', []);
};

/**
 * 调用app 完整性检测
 * @param sha1sumInfoUrl 验证app 完整性的服务器地址
 */
exports.verifyApp = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'verifyApp', []);
};


/**
 * 获取定位
 * 返回定位数据
 */
exports.startLoc = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'startLoc', []);
};

/**
 * 加密文件，
 * 返回加密后的文件路径
 */
exports.encryptFile = function (filePath,outFilePath,success, error) {
    exec(success, error, 'ThsDeviceManager', 'encryptFile', [filePath,outFilePath]);
};

/**
 * 解密文件
 * 返回解密后的文件路径
 */
exports.decryptionFile = function (filePath,outFilePath,success, error) {
    exec(success, error, 'ThsDeviceManager', 'decryptionFile', [filePath,outFilePath]);
};

/**
 * 激活设备管理器
 */
exports.enableDeviceManager = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'enableDeviceManager', []);
};

/**
 * 取消设备管理器
 */
exports.disableDeviceManager = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'disableDeviceManager', []);
};

/**
 * 设置手势密码
 */
exports.setPwd = function(success, error){
    exec(success, error, 'ThsDeviceManager', 'setPwd', []);
};

/**
 * 验证手势密码
 */
exports.veryPwd = function(success, error){
    exec(success, error, 'ThsDeviceManager', 'veryPwd', []);
};

// 监听验证手势密码回调
exports.onVeryPwdInAndroidCallback = function(data) {
   
   cordova.fireDocumentEvent('thsDeviceManager.onVeryPwdReceiver', data);
};

// 监听设置手势密码回调
exports.onSetPwdInAndroidCallback = function(data) {
   
   cordova.fireDocumentEvent('thsDeviceManager.onSetPwdReceiver', data);
};

/**
 * 二维码验证登录
 * @param loginName 登录用户
 * @param password 登录密码
 * @param token 扫描二维码token
 */
exports.qrCodeLogin = function(loginName,password,token,success, error){
    exec(success, error, 'ThsDeviceManager', 'qrCodeLogin', [loginName,password,token]);
}



