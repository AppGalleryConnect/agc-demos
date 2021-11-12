module.exports = (function() {
var __MODS__ = {};
var __DEFINE__ = function(modId, func, req) { var m = { exports: {}, _tempexports: {} }; __MODS__[modId] = { status: 0, func: func, req: req, m: m }; };
var __REQUIRE__ = function(modId, source) { if(!__MODS__[modId]) return require(source); if(!__MODS__[modId].status) { var m = __MODS__[modId].m; m._exports = m._tempexports; var desp = Object.getOwnPropertyDescriptor(m, "exports"); if (desp && desp.configurable) Object.defineProperty(m, "exports", { set: function (val) { if(typeof val === "object" && val !== m._exports) { m._exports.__proto__ = val.__proto__; Object.keys(val).forEach(function (k) { m._exports[k] = val[k]; }); } m._tempexports = val }, get: function () { return m._tempexports; } }); __MODS__[modId].status = 1; __MODS__[modId].func(__MODS__[modId].req, m, m.exports); } return __MODS__[modId].m.exports; };
var __REQUIRE_WILDCARD__ = function(obj) { if(obj && obj.__esModule) { return obj; } else { var newObj = {}; if(obj != null) { for(var k in obj) { if (Object.prototype.hasOwnProperty.call(obj, k)) newObj[k] = obj[k]; } } newObj.default = obj; return newObj; } };
var __REQUIRE_DEFAULT__ = function(obj) { return obj && obj.__esModule ? obj.default : obj; };
__DEFINE__(1631754354880, function(require, module, exports) {
var e=require("@agconnect/api"),r=require("@agconnect/log"),t=require("@agconnect/core");function n(e){return e&&"object"==typeof e&&"default"in e?e:{default:e}}var o=n(e),s=function(){function e(){this.storage={}}return e.getInstance=function(r,t){return e.memoryInsMap.has(r)&&e.memoryInsMap.get(r)||e.memoryInsMap.set(r,new e),e.memoryInsMap.get(r)},e.prototype.get=function(e){return Promise.resolve(this.storage[e])},e.prototype.remove=function(e){return delete this.storage[e],Promise.resolve()},e.prototype.set=function(e,r){return this.storage[e]=r,Promise.resolve()},e.memoryInsMap=new Map,e}(),i=function(){function e(){this.encryptImpl=void 0}return e.prototype.setEncryptImp=function(e){this.encryptImpl=e},e.prototype.decrypt=function(e){return null!=this.encryptImpl&&null!=this.encryptImpl&&null!=e&&null!=e?this.encryptImpl.decrypt(e):e},e.prototype.encrypt=function(e){return null!=this.encryptImpl&&null!=this.encryptImpl&&null!=e&&null!=e?this.encryptImpl.encrypt(e):e},e}(),a=function(){function e(){this.DB_NAME="agcLocalStorageDb",this.OBJECT_STORE_NAME="agc",this.KEY_PATH="agcStorage",this.VERSION=1,this.agcCryptImpl=new i,this.logger=r.Logger.createLogger("AGCStorageService")}return e.getInstance=function(t,n){if(!window.indexedDB)throw r.Logger.createLogger("AGCStorageService").error("Your environment doesn't support a stable version of IndexedDB."),new Error("Your environment doesn't support a stable version of IndexedDB.");e.indexedDBInsMap.has(t)&&e.indexedDBInsMap.get(t)||e.indexedDBInsMap.set(t,new e);var o=e.indexedDBInsMap.get(t);return o.agcCryptImpl.setEncryptImp(n),o},e.prototype.initIndexedDb=function(e){var r=this;return window.indexedDB?new Promise((function(t,n){var o=window.indexedDB.open(r.DB_NAME,r.VERSION),s=r;o.onupgradeneeded=function(e){var r=e.target.result;try{r.objectStoreNames.contains(s.OBJECT_STORE_NAME)||r.createObjectStore(s.OBJECT_STORE_NAME,{keyPath:s.KEY_PATH})}catch(e){n(e)}},o.onsuccess=function(r){try{t(r.target.result.transaction([s.OBJECT_STORE_NAME],e).objectStore(s.OBJECT_STORE_NAME))}catch(e){n(e)}},o.onerror=function(e){n(new Error(e.target.error))}})):Promise.reject("Your environment doesn't support a stable version of IndexedDB.")},e.prototype.get=function(e){if(!e)return Promise.reject(new Error("key is null"));var r=this;return this.initIndexedDb("readonly").then((function(t){return new Promise((function(n,o){try{var s=t.get(e);s.onsuccess=function(){var e=s.result;n(null!=e&&"value"in e?r.agcCryptImpl.decrypt(e.value):e)},s.onerror=function(e){o(e.target.error)}}catch(e){o(e)}}))})).catch((function(e){return Promise.reject(e)}))},e.prototype.set=function(e,r){if(!e||!r)return Promise.reject(new Error("key or value is null"));var t=this,n={},o={};n[t.KEY_PATH]=e,o.value=t.agcCryptImpl.encrypt(r);var s=Object.assign(o,n);return this.initIndexedDb("readwrite").then((function(r){try{var n=r.get(e);return n.onsuccess=function(){var e=n.result,t=null!=e&&"value"in e?r.put(s):r.add(s);t.onsuccess=function(){return Promise.resolve()},t.onerror=function(e){return Promise.reject(e.target.error)}},n.onerror=function(e){return t.logger.error("---get value from db error."),Promise.reject("get value from db error.")},Promise.resolve()}catch(e){return t.logger.error("---set value to db error."+e),Promise.reject("set value to db error."+e)}})).catch((function(e){return Promise.reject("init db error."+e)}))},e.prototype.remove=function(e){return e?this.initIndexedDb("readwrite").then((function(r){try{var t=r.delete(e);return t.onsuccess=function(){return Promise.resolve()},t.onerror=function(e){return Promise.reject(e.target.error)},Promise.resolve()}catch(e){return Promise.reject(e)}})).catch((function(e){return Promise.reject(e)})):Promise.reject(new Error("key is null"))},e.indexedDBInsMap=new Map,e}(),c=function(){function e(){this.agcCryptImpl=new i}return e.getInstance=function(t,n){if(!e.isSessionStorageAvailable())throw r.Logger.createLogger("AGCStorageService").error("Your environment doesn't support a stable version of sessionStorage."),new Error("Your environment doesn't support a stable version of sessionStorage.");!e.sessionMap.has(t)&&e.sessionMap.get(t)||e.sessionMap.set(t,new e);var o=e.sessionMap.get(t);return o.agcCryptImpl.setEncryptImp(n),o},e.isSessionStorageAvailable=function(){try{return sessionStorage.setItem("agctestKey","testValue"),sessionStorage.removeItem("agctestKey"),!0}catch(e){return!1}},e.prototype.get=function(e){if(!e)return Promise.reject(new Error("key is null"));try{var r=sessionStorage.getItem(e);return Promise.resolve(""===r?null:this.agcCryptImpl.decrypt(r))}catch(e){return Promise.reject(e)}},e.prototype.set=function(e,r){if(!e||!r)return Promise.reject(new Error("key or value is null"));try{return sessionStorage.setItem(e,this.agcCryptImpl.encrypt(r)),Promise.resolve()}catch(e){return Promise.reject(e)}},e.prototype.remove=function(e){if(!e)return Promise.reject(new Error("key is null"));try{return sessionStorage.removeItem(e),Promise.resolve()}catch(e){return Promise.reject(e)}},e.sessionMap=new Map,e}(),u=function(){function e(){this.agcCryptImpl=new i,this.logger=r.Logger.createLogger("AGCStorageService")}return e.getInstance=function(r,t){e.MiniProgramStorageMap.has(r)&&e.MiniProgramStorageMap.get(r)||e.MiniProgramStorageMap.set(r,new e);var n=e.MiniProgramStorageMap.get(r);return n.agcCryptImpl.setEncryptImp(t),n},e.prototype.get=function(e){var r=this;return new Promise((function(t,n){try{wx.getStorage({key:e,success:function(e){e?t(r.agcCryptImpl.decrypt(e.data)):(r.logger.log("get storage resp undefined"),t(void 0))},fail:function(e){r.logger.log("get storage failed,",e),t(void 0)}})}catch(e){r.logger.error("catch error in get:",e),n(e)}}))},e.prototype.set=function(e,r){var t=this;return new Promise((function(n,o){try{wx.setStorage({key:e,data:t.agcCryptImpl.encrypt(r),success:function(){t.logger.log("set storage success, key: ",e),n()},fail:function(e){t.logger.log("set storage failed. ",e),o(e)}})}catch(e){t.logger.error("catch error in set:",e),o(e)}}))},e.prototype.remove=function(e){var r=this;return new Promise((function(t,n){try{wx.removeStorage({key:e,success:function(){r.logger.log("remove storage success. "),t()},fail:function(e){r.logger.log("remove storage failed. ",e),n(e)}})}catch(e){r.logger.error("catch error in remove:",e),n(e)}}))},e.MiniProgramStorageMap=new Map,e}(),g="undefined"==typeof window&&"object"==typeof wx,p=function(){function e(e){this.name=t.DEFAULT_CATEGORY,e&&(this.name=e)}return e.prototype.getStorageInstance=function(e,r){var t;if(!g){switch(e){case 2:t=s.getInstance(this.name,r);break;case 0:t=a.getInstance(this.name,r);break;case 1:t=c.getInstance(this.name,r);break;default:t=s.getInstance(this.name,r)}return t}switch(e){case 2:return s.getInstance(this.name,r);default:return u.getInstance(this.name,r)}},e.prototype.createPersistentStorage=function(){return g?u.getInstance(this.name):a.getInstance(this.name)},e.prototype.createTemporaryStorage=function(){return g?u.getInstance(this.name):c.getInstance(this.name)},e.prototype.createMemoryStorage=function(){return s.getInstance(this.name)},e}();o.default.registerInternalService({name:"AGCStorageService",serviceFactory:function(e){return new p(e.name())}});

}, function(modId) {var map = {}; return __REQUIRE__(map[modId], modId); })
return __REQUIRE__(1631754354880);
})()
//miniprogram-npm-outsideDeps=["@agconnect/api","@agconnect/log","@agconnect/core"]
//# sourceMappingURL=index.js.map