module.exports = (function() {
var __MODS__ = {};
var __DEFINE__ = function(modId, func, req) { var m = { exports: {}, _tempexports: {} }; __MODS__[modId] = { status: 0, func: func, req: req, m: m }; };
var __REQUIRE__ = function(modId, source) { if(!__MODS__[modId]) return require(source); if(!__MODS__[modId].status) { var m = __MODS__[modId].m; m._exports = m._tempexports; var desp = Object.getOwnPropertyDescriptor(m, "exports"); if (desp && desp.configurable) Object.defineProperty(m, "exports", { set: function (val) { if(typeof val === "object" && val !== m._exports) { m._exports.__proto__ = val.__proto__; Object.keys(val).forEach(function (k) { m._exports[k] = val[k]; }); } m._tempexports = val }, get: function () { return m._tempexports; } }); __MODS__[modId].status = 1; __MODS__[modId].func(__MODS__[modId].req, m, m.exports); } return __MODS__[modId].m.exports; };
var __REQUIRE_WILDCARD__ = function(obj) { if(obj && obj.__esModule) { return obj; } else { var newObj = {}; if(obj != null) { for(var k in obj) { if (Object.prototype.hasOwnProperty.call(obj, k)) newObj[k] = obj[k]; } } newObj.default = obj; return newObj; } };
var __REQUIRE_DEFAULT__ = function(obj) { return obj && obj.__esModule ? obj.default : obj; };
__DEFINE__(1631754354878, function(require, module, exports) {

/*! *****************************************************************************
Copyright (c) Microsoft Corporation.

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
***************************************************************************** */
function o(o,e){for(var r=0,t=e.length,n=o.length;r<t;r++,n++)o[n]=e[r];return o}var e,r;Object.defineProperty(exports,"__esModule",{value:!0}),exports.LogLevel=void 0,(r=exports.LogLevel||(exports.LogLevel={}))[r.DEBUG=0]="DEBUG",r[r.VERBOSE=1]="VERBOSE",r[r.INFO=2]="INFO",r[r.WARN=3]="WARN",r[r.ERROR=4]="ERROR",r[r.SILENT=5]="SILENT";var t=function(){function r(){var e=this;this.moduleName="",this.logLevel=exports.LogLevel.VERBOSE,this.userLogProvider=null,this.logProvider=function(t,n){for(var l=[],s=2;s<arguments.length;s++)l[s-2]=arguments[s];if(!(n<e.logLevel)){var g=r.consoleType[n];if(!g)throw new Error("invalid logType: "+n);console[g].apply(console,o(["[Module:"+t+"] ["+(new Date).toISOString()+"] | "],l))}}}return r.createLogger=function(o){for(var e=0,t=r.logInstanceArray;e<t.length;e++){var n=t[e];if(n.moduleName==o)return n}var l=new r;return l.moduleName=o,r.logInstanceArray.push(l),l},r.prototype.setLogProvider=function(o){if("function"!=typeof o)throw new Error("logProvider must be set as a function");this.logProvider=o},r.prototype.setUserLogProvider=function(o){this.userLogProvider=o},r.prototype.debug=function(){for(var e=[],r=0;r<arguments.length;r++)e[r]=arguments[r];this.doLog.apply(this,o([exports.LogLevel.DEBUG],e))},r.prototype.log=function(){for(var e=[],r=0;r<arguments.length;r++)e[r]=arguments[r];this.doLog.apply(this,o([exports.LogLevel.VERBOSE],e))},r.prototype.info=function(){for(var e=[],r=0;r<arguments.length;r++)e[r]=arguments[r];this.doLog.apply(this,o([exports.LogLevel.INFO],e))},r.prototype.warn=function(){for(var e=[],r=0;r<arguments.length;r++)e[r]=arguments[r];this.doLog.apply(this,o([exports.LogLevel.WARN],e))},r.prototype.error=function(){for(var e=[],r=0;r<arguments.length;r++)e[r]=arguments[r];this.doLog.apply(this,o([exports.LogLevel.ERROR],e))},r.prototype.doLog=function(e){for(var r=[],t=1;t<arguments.length;t++)r[t-1]=arguments[t];this.userLogProvider&&this.userLogProvider.apply(this,o([this.moduleName,e],r)),this.logProvider.apply(this,o([this.moduleName,e],r))},r.consoleType=((e={})[exports.LogLevel.DEBUG]="log",e[exports.LogLevel.VERBOSE]="log",e[exports.LogLevel.SILENT]="log",e[exports.LogLevel.INFO]="info",e[exports.LogLevel.WARN]="warn",e[exports.LogLevel.ERROR]="error",e),r.logInstanceArray=[],r}();exports.Logger=t,exports.setGlobalLogLevel=function(o){for(var e=0,r=t.logInstanceArray;e<r.length;e++){r[e].logLevel=o}},exports.setGlobalUserLogHandler=function(e){for(var r=function(r){e?r.setUserLogProvider((function(t,n){for(var l=[],s=2;s<arguments.length;s++)l[s-2]=arguments[s];n<r.logLevel||e.apply(void 0,o([t,n],l))})):r.setUserLogProvider(null)},n=0,l=t.logInstanceArray;n<l.length;n++){r(l[n])}};

}, function(modId) {var map = {}; return __REQUIRE__(map[modId], modId); })
return __REQUIRE__(1631754354878);
})()
//miniprogram-npm-outsideDeps=[]
//# sourceMappingURL=index.js.map