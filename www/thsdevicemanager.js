var exec = require('cordova/exec');
/**
 * 初始化 
 * @param config 服务地址等信息
 */
exports.init = function (config, success, error) {
    exec(success, error, 'ThsDeviceManager', 'init', [config]);
};

/**
 * 启动服务
 */
exports.startService = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'startService', []);
};

/**
 * 获取设备信息
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
 */
exports.startLoc = function (success, error) {
    exec(success, error, 'ThsDeviceManager', 'startLoc', []);
};

/**
 * 加密文件
 */
exports.encryptFile = function (filePath,outFilePath,success, error) {
    exec(success, error, 'ThsDeviceManager', 'encryptFile', [filePath,outFilePath]);
};

/**
 * 解密文件
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

/**
 * 二维码验证登录
 * @param loginName 登录用户
 * @param password 登录密码
 * @param token 扫描二维码token
 */
exports.qrCodeLogin = function(loginName,password,token,success, error){
    exec(success, error, 'ThsDeviceManager', 'qrCodeLogin', [loginName,password,token]);
}



