var exec = require('cordova/exec');
/**
 * 初始化 
 * @param baseUrl 服务地址等信息 例：http://192.168.0.101:8084/ths-move/
 */
exports.init = function (baseUrl, success, error) {
    exec(success, error, 'ThsDeviceManager', 'init', [baseUrl]);
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
exports.encryptFile = function (filePath, outFilePath, success, error) {
    exec(success, error, 'ThsDeviceManager', 'encryptFile', [filePath, outFilePath]);
};

/**
 * 解密文件
 * 返回解密后的文件路径
 */
exports.decryptionFile = function (filePath, outFilePath, success, error) {
    exec(success, error, 'ThsDeviceManager', 'decryptionFile', [filePath, outFilePath]);
};

/**
 * 加密文本，
 * 返回加密后的文本
 */
exports.encryptStr = function (content, success, error) {
    exec(success, error, 'ThsDeviceManager', 'encryptStr', [content]);
};

/**
 * 解密文本
 * 返回解密后的文本路径
 */
exports.decryptionStr = function (content, success, error) {
    exec(success, error, 'ThsDeviceManager', 'decryptionStr', [content]);
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
exports.setPwd = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'setPwd', []);
};

/**
 * 验证手势密码
 */
exports.veryPwd = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'veryPwd', []);
};

/**
 * 关闭验证手势密码
 */
exports.closeActivity = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'closeActivity', []);
};

// 监听验证手势密码回调
exports.onVeryPwdInAndroidCallback = function (data) {
    data = JSON.stringify(data);
    data = JSON.parse(data);
    cordova.fireDocumentEvent('thsDeviceManager.onVeryPwdReceiver', data);
};

// 监听设置手势密码回调
exports.onSetPwdInAndroidCallback = function (data) {
    data = JSON.stringify(data);
    data = JSON.parse(data);
    cordova.fireDocumentEvent('thsDeviceManager.onSetPwdReceiver', data);
};

/**
 * 二维码验证登录
 * @param loginName 登录用户
 * @param password 登录密码
 * @param token 扫描二维码token
 */
exports.qrCodeLogin = function (loginName, password, token, success, error) {
    exec(success, error, 'ThsDeviceManager', 'qrCodeLogin', [loginName, password, token]);
}



