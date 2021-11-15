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
var agconnect = require('@agconnect/api');
require('@agconnect/instance');
require('@agconnect/function');
import { FunctionResult } from "@agconnect/function-types"

Page({
  data: {
    functionRes: ''
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
      wx.showToast({
        title: 'run OK',
        icon: 'success',
        duration: 2000
      });
      this.setData({
        functionRes: JSON.stringify(res.getValue())
      })
    }).catch((error: any) => {
      console.error('run error', error);
      wx.showToast({
        title: 'run fail',
        icon: 'error',
        duration: 2000
      });
    });
  }
})
