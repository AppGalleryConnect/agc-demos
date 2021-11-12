module.exports = (function() {
var __MODS__ = {};
var __DEFINE__ = function(modId, func, req) { var m = { exports: {}, _tempexports: {} }; __MODS__[modId] = { status: 0, func: func, req: req, m: m }; };
var __REQUIRE__ = function(modId, source) { if(!__MODS__[modId]) return require(source); if(!__MODS__[modId].status) { var m = __MODS__[modId].m; m._exports = m._tempexports; var desp = Object.getOwnPropertyDescriptor(m, "exports"); if (desp && desp.configurable) Object.defineProperty(m, "exports", { set: function (val) { if(typeof val === "object" && val !== m._exports) { m._exports.__proto__ = val.__proto__; Object.keys(val).forEach(function (k) { m._exports[k] = val[k]; }); } m._tempexports = val }, get: function () { return m._tempexports; } }); __MODS__[modId].status = 1; __MODS__[modId].func(__MODS__[modId].req, m, m.exports); } return __MODS__[modId].m.exports; };
var __REQUIRE_WILDCARD__ = function(obj) { if(obj && obj.__esModule) { return obj; } else { var newObj = {}; if(obj != null) { for(var k in obj) { if (Object.prototype.hasOwnProperty.call(obj, k)) newObj[k] = obj[k]; } } newObj.default = obj; return newObj; } };
var __REQUIRE_DEFAULT__ = function(obj) { return obj && obj.__esModule ? obj.default : obj; };
__DEFINE__(1631754354879, function(require, module, exports) {
var e=require("@agconnect/api"),t=require("axios"),o=require("@agconnect/log");function r(e){return e&&"object"==typeof e&&"default"in e?e:{default:e}}var n=r(e),s=r(t),c=function(){function e(e){this.CancelToken=s.default.CancelToken,s.default.defaults.timeout=3e3,null==e&&null==e||(s.default.defaults.adapter=e)}return e.prototype.getAxiosIns=function(){return s.default},e.prototype.post=function(e,t,o,r){return this.sendRequest("POST",e,t,o,r)},e.prototype.get=function(e,t,o,r){return this.sendRequest("GET",e,t,o,r)},e.prototype.delete=function(e,t,o,r){return this.sendRequest("DELETE",e,t,o,r)},e.prototype.put=function(e,t,o,r){return this.sendRequest("PUT",e,t,o,r)},e.prototype.sendRequest=function(e,t,o,r,n){if(!this.checkParam(t))return Promise.reject("URL IS ERROR!");var c={url:t,method:e,transformResponse:null==n?void 0:n.transformResponse,headers:r,timeout:null==n?void 0:n.timeout,responseType:null==n?void 0:n.responseType,onUploadProgress:null==n?void 0:n.onUploadProgress,onDownloadProgress:null==n?void 0:n.onDownloadProgress,validateStatus:null==n?void 0:n.validateStatus,cancelToken:null==n?void 0:n.cancelToken};return"PUT"!=e&&"POST"!=e||(c.data=o),"GET"!=e&&"DELETE"!=e||(c.params=o),s.default.request(c)},e.prototype.checkParam=function(e){return!(!e||e.match(/\s/g)||!e.match(/^(ht)tp(s?)\:\/\/[0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*(:(0-9)*)*(\/?)([a-zA-Z0-9\-\.\?\,\'\/\\\+&amp;%\$#_]*)?/))},e}(),i=function(){function e(){}return e.prototype.getPlatform=function(){return"NodeJS"},e.prototype.getPlatformVersion=function(){return""},e.prototype.getPackageName=function(){return""},e.prototype.getAppVersion=function(){return""},e.prototype.getLanguage=function(){return""},e.prototype.getScript=function(){return""},e.prototype.getCountry=function(){return""},e}();function a(e){return new Promise((function(t,o){var r,n=e.method&&e.method.toUpperCase()||"GET",s={method:n,header:e.headers,url:e.url,success:function(r){var n={data:r.data,status:r.statusCode,statusText:r.errMsg,headers:r.header,config:e};e.validateStatus&&e.validateStatus(r.statusCode)||!e.validateStatus&&200==r.statusCode?t(n):o({message:r.errMsg,code:r.statusCode,response:n})},fail:function(e){o({message:e.errMsg,response:{}})},complete:function(){r=void 0}};0!==e.timeout&&(s.timeout=e.timeout),e.responseType&&(s.responseType=e.responseType),e.cancelToken&&e.cancelToken.promise.then((function(e){r&&(r.abort&&r.abort(),o(e),r=void 0)})),"PUT"==n||"POST"==n?s.data=e.data:"GET"!=n&&"DELETE"!=n||(s.data=e.params),r=wx.request(s)}))}var u=function(){function e(){this.logger=o.Logger.createLogger("AGCPlatformInfoService")}return e.prototype.getPlatform=function(){return"JS-SDK-Mini-Program"},e.prototype.getPlatformVersion=function(){return""},e.prototype.getPackageName=function(){return""},e.prototype.getAppVersion=function(){try{return wx.getAccountInfoSync().miniProgram.version}catch(e){return this.logger.error("getAppVersion:fail,",e),""}},e.prototype.getLanguage=function(){try{return wx.getSystemInfoSync().language}catch(e){return this.logger.error("getLanguage:fail,",e),""}},e.prototype.getScript=function(){return""},e.prototype.getCountry=function(){return""},e}(),p=function(){function e(){this.logger=o.Logger.createLogger("AGCWebSocketService")}return e.prototype.connect=function(e,t,o){return this.websocket&&3!=this.websocket.readyState&&(this.websocket.close(),this.websocket=null),this.websocket=wx.connectSocket({url:e,header:t,protocols:o}),this.websocket?Promise.resolve():(this.logger.error("webSocket create fail"),Promise.reject("webSocket create fail"))},e.prototype.getReadyState=function(){return this.websocket?Number(this.websocket.readyState):null},e.prototype.send=function(e,t,o){this.websocket?this.websocket.send({data:e,success:t,fail:o}):o&&o()},e.prototype.close=function(e,t,o,r){this.websocket?this.websocket.close({code:e,reason:t,success:o,fail:r}):r&&r()},e.prototype.onOpen=function(e){this.websocket?this.websocket.onOpen(e):this.logger.error("webSocket connect failed")},e.prototype.onMessage=function(e){this.websocket?this.websocket.onMessage((function(t){e&&e(t.data)})):this.logger.error("webSocket connect failed")},e.prototype.onClose=function(e){this.websocket?this.websocket.onClose((function(t){e&&e(t.code,t.reason,t.wasClean)})):this.logger.error("webSocket connect failed")},e.prototype.onError=function(e){this.websocket?this.websocket.onError(e):this.logger.error("webSocket connect failed")},e}(),l="undefined"==typeof window&&"object"==typeof wx;var f=n.default;f.registerInternalService({name:"AGCNetworkService",serviceFactory:function(e){return l?new c(a):new c}}),f.registerInternalService({name:"AGCPlatformInfoService",serviceFactory:function(e){return l?new u:new i}}),l&&f.registerInternalService({name:"AGCWebSocketService",serviceFactory:function(e){return new p}});

}, function(modId) {var map = {}; return __REQUIRE__(map[modId], modId); })
return __REQUIRE__(1631754354879);
})()
//miniprogram-npm-outsideDeps=["@agconnect/api","axios","@agconnect/log"]
//# sourceMappingURL=index.js.map