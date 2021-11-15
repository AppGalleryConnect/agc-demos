/*
* Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
// index.ts
var agconnect = require('@agconnect/api');
require('@agconnect/instance');
require('@agconnect/remoteconfig');

import {ConfigValue} from "@agconnect/remoteconfig-types"
Page({
  data: {
    configInfo: ''
  },
  onLoad() {
    var agConnectConfig =
    {
      // App configuration information.
    };
    // Initialize AppGallery Connect.
    agconnect.instance().configInstance(agConnectConfig);
  },

  formSubmit(e: any) {
    const { fetchReqTimeoutMillis, minFetchIntervalMillis, defaultConfig } = e.detail.value;
    switch (e.detail.target.dataset.type) {
      case 'fetchandApply':
        this.fetchAndApply(fetchReqTimeoutMillis, minFetchIntervalMillis);
        break;
      case 'showConfig':
        this.applyDefaultConfig(defaultConfig);
        this.showConfig();
        break;
      default:
        break;
    }
  },
  applyDefaultConfig(defaultConfig: any) {
    if (defaultConfig) {
      var defaultConfigJson = JSON.parse(defaultConfig);
      if (defaultConfigJson && typeof defaultConfigJson == 'object') {
        let defaultConfigMap = new Map();
        for (var k in defaultConfigJson) {
          defaultConfigMap.set(k, JSON.stringify(defaultConfigJson[k]));
        }
        agconnect.remoteConfig().applyDefault(defaultConfigMap);
      }
    }
  },
  fetchAndApply(fetchReqTimeoutMillis: any, minFetchIntervalMillis: any) {
    if (fetchReqTimeoutMillis) {
      agconnect.remoteConfig().fetchReqTimeoutMillis = fetchReqTimeoutMillis;
    }
    if (minFetchIntervalMillis) {
      agconnect.remoteConfig().minFetchIntervalMillis = minFetchIntervalMillis;
    }
    agconnect.remoteConfig().fetch().then(() => {
      agconnect.remoteConfig().apply();
      wx.showToast({
        title: 'fetch OK',
        icon: 'success',
        duration: 2000
      });
    }).catch((error: any) => {
      console.error('fetch error', error);
      wx.showToast({
        title: 'fetch fail',
        icon: 'error',
        duration: 2000
      });
    });
  },
  showConfig() {
    let obj = '';
    let resultMap = agconnect.remoteConfig().getMergedAll();
    resultMap.forEach(function (value: ConfigValue, key: string) {
      obj = obj + key + ': ( value:' + value.getValueAsString() + ', source:' + value.getSource() + ' )\n';
    });
    this.setData({
      configInfo: obj
    });
  }
})
