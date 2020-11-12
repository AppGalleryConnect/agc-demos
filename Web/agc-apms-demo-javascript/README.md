### yarn serve 启动服务

#### App.vue
启用 禁用 sdk
#### PageA.vue
1. 自定义追踪 上传案例
2. 动态添加script脚本
#### PageB.vue
1. 动态添加script脚本
2. 动态添加图片资源

- 切换pageA和pageB 动态添加资源至20条时触发上报，观察控制台network，若看到两次token请求和两次webv3请求则证明触发了数据上报，可以在webv3中看到请求数据内容，刷新页面和关闭页面时也会触发数据上报


#### 自定义追踪
|事件|入参|参数是否必须|说明|
|:---:|:---:|:---:|:---:|
|start|-|-|开启追踪|
|stop|-|-|结束追踪|
|setAttribute|(key: string, value: string)|true|自定义属性|
|getAttribute|(attr: string)|true|获取指定的自定义属性|
|getAttributes|-|-|获取所有的自定义属性|
|setMetric|(key: string, value: number)|true|自定义指标|
|getMetric|(metric: string)|true|获取指定的自定义指标|
|getMetrics|-|-|获取所有的自定义指标|

start和stop需成对出现，setAttribute和setMetric需在stop之前

```js
// 创建一个trace实例
const uploadSpeedTrace = APM.createTrace('uploadSpeedTrace')
// 启动trace
uploadSpeedTrace.start()

// upload code...

// 设置自定义属性或指标
uploadSpeedTrace.setAttribute('file_type', 'photo')

// 设置自定义属性或指标
uploadSpeedTrace.setMetric('file_size', 5319)

// 结束trace
uploadSpeedTrace.stop() 
```

```js
// 创建一个trace实例
const successTimeTrace = APM.createTrace('successTimeTrace')
// 启动trace
successTimeTrace.start()
// 设置上传结果指标，成功为1，失败为0
uploadSpeedTrace.setMetric('upload_result', 1)

// 结束trace
successTimeTrace.stop()

```

#### 上报数据样例
```json
{
  "website": [1594951239098, 119, 119, 190, -1, 190, 2, 173, 190, 192],
  "network": [1594951239090, 0, 0, -1, 8, 0, 0, 8, 0],
  "resources": [
    [1594951239122, "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", 32, "link", "https://cdn.bootcdn.net/ajax/libs/element-ui/2.13.2/theme-chalk/index.css", 26, 32, 32, 32, 32, 32, 0, 42, 47, 57, 0, 232432, 232432],
    [1594951239124, "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", 34, "img", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1091405991,859863778&fm=26&gp=0.jpg", 21, 34, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0],
    [1594951239124, "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", 34, "img", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593495345540&di=584ed642f89a3665f71e4dbd8acb9dba&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg", 55, 34, 0, 0, 0, 0, 0, 0, 0, 89, 0, 0, 0],
    [1594951239124, "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", 34, "script", "https://cdn.bootcdn.net/ajax/libs/vue/3.0.0-beta.15/vue.global.js", 31, 34, 34, 34, 34, 34, 0, 59, 60, 65, 0, 475253, 475253],
    [1594951239124, "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", 34, "script", "https://cdn.bootcdn.net/ajax/libs/element-ui/2.13.2/index.js", 45, 34, 34, 34, 34, 34, 0, 62, 63, 79, 0, 567158, 567158]
  ],
  "error": [
    [
      [1594951239201, "file://localhost:35729/livereload.js?snipver=1", "file://localhost:35729/livereload.js?snipver=1 is load error"]
    ],
    [
      [1594951239203, "Error: 123"]
    ],
    []
  ],
  "browser": ["Netscape", "Mozilla", "Chrome/83.0", "Win32", true, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36", "file:///D:/workspace/CP_APMS_APMSDK_web/example/index.html", "file:", -1, true, "4g", "visible"],
  "agent_version": "1.0.0",
  "agent_type": "web",
  "visitKey": "visit#f2nl58yvdK",
  "traces": [
    [1594951239164, "TRACE-test1", 258276, 2, [{
        "a": "aaa"
      }, {
        "b": "bbb"
      }, {
        "c": "ccc"
      }, {
        "d": "ddd"
      }, {
        "e": "eee"
      }],
      [{
        "fff": 1234
      }, {
        "ggg": 4321
      }]
    ],
    [1594951239165, "TRACE-test2", 952495, 1001, [{
        "e": "eee"
      }],
      []
    ]
  ],
  "platform": [2005646545446540000, 9105416654164565000, 3301156545454546400, 1010745441]
}
```
