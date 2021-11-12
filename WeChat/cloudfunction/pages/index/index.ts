// index.ts
var agconnect = require('@agconnect/api');
require('@agconnect/instance');
require('@agconnect/function');
import {FunctionResult} from "@agconnect/function-types"

Page({
  data: {
    functionRes: ''
  },
  onLoad() {
    var agConnectConfig = {
      "agcgw":{
        "backurl":"connect-drcn.dbankcloud.cn",
        "url":"connect-drcn.hispace.hicloud.com",
        "websocketbackurl":"connect-ws-drcn.hispace.dbankcloud.cn",
        "websocketurl":"connect-ws-drcn.hispace.dbankcloud.com"
      },
      "agcgw_all":{
        "CN":"connect-drcn.hispace.hicloud.com",
        "CN_back":"connect-drcn.dbankcloud.cn",
        "DE":"connect-dre.hispace.hicloud.com",
        "DE_back":"connect-dre.dbankcloud.cn",
        "RU":"connect-drru.hispace.hicloud.com",
        "RU_back":"connect-drru.dbankcloud.cn",
        "SG":"connect-dra.hispace.hicloud.com",
        "SG_back":"connect-dra.dbankcloud.cn"
      },
      "client":{
        "cp_id":"2850086000482154745",
        "product_id":"736430079245872406",
        "client_id":"675105777177871680",
        "client_secret":"8883056A812E160F633EF91B26B9002FD6D9A03972534B825D30B8B6D482EE66",
        "project_id":"736430079245872406",
        "app_id":"104590791",
        "api_key":"CgB6e3x9ljrI8rF2DEMWsOFNqV3PgZnzZ5xrNsEBdp6Z1FRO+UCDNIBEVi07hpdaZ311Mk8/7HTUB3FhCUkucR5m",
        "package_name":"com.huawei.minipush"
      },
      "oauth_client":{
        "client_id":"104590791",
        "client_type":19
      },
      "app_info":{
        "app_id":"104590791",
        "package_name":"com.huawei.minipush"
      },
      "service":{
        "analytics":{
          "collector_url":"datacollector-drcn.dt.hicloud.com,datacollector-drcn.dt.dbankcloud.cn",
          "resource_id":"p1",
          "channel_id":""
        },
        "search":{
          "url":"https://search-drcn.cloud.huawei.com"
        },
        "cloudstorage":{
          "storage_url":"https://agc-storage-drcn.platform.dbankcloud.cn"
        },
        "ml":{
          "mlservice_url":"ml-api-drcn.ai.dbankcloud.com,ml-api-drcn.ai.dbankcloud.cn"
        }
      },
      "region":"CN",
      "configuration_version":"3.0"
    };
    agconnect.instance().configInstance(agConnectConfig);
  },

  formSubmit(e: any) {
    console.log(e)
    const { httpTrigger, body } = e.detail.value;
    switch (e.detail.target.dataset.type) {
      case 'run':
        this.run(httpTrigger, body);
        break;
      default:
        break;
    }
  },
  run(httpTrigger: string, body: string) {
    let functionCallable = agconnect.function().wrap(httpTrigger);
    functionCallable.call(body).then((res: FunctionResult) => {
      this.setData({
        functionRes:JSON.stringify(res.getValue())
      })
    });
  }
})
