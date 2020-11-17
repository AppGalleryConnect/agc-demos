import agconnect from '@agconnect/api';
import '@agconnect/instance';

/**
 * Initializes app configuration
 */
export async function configInstance() {
  agconnect.instance().configInstance(agConnectConfig);
}

var agConnectConfig =
  {
    // Fill in the code when integrating AGC development services into your Web application,
    // the code can be obtained in your project on the AGC portal.
    "agcgw":{
      "backurl":"https://lfagcgwtest01cn.hwcloudtest.cn:18062",
      "url":"https://lfagcgwtest01cn.hwcloudtest.cn:18062"
    },
    "client":{
      "appType":"9999",
      "cp_id":"4130086000000999937",
      "product_id":"258913027873082392",
      "client_id":"476658973819284736",
      "client_secret":"23F8A09ED6DE034019493060F87A26654F40F50282292265D0D49696A8C6362A",
      "project_id":"258913027873082392",
      "app_id":"98751101985221090",
      "api_key":"CgB6e3x9HaoF6w/LRkpsqK5geqMp7lgrugpVnzQfI44DPhW8kayJeaXHocxQxD+y9aLdlGQy1//DGvJUNTdS88H+"
    },
    "service":{
      "analytics":{
        "collector_url":"habackup.hwcloudtest.cn:31405,datacollector-drcn.dt.dbankcloud.cn",
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
    "configuration_version":"1.0"
  };

