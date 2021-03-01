/*
* Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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

import agconnect from '@agconnect/api';
import '@agconnect/instance';

/**
 * Initializes app configuration
 */
export function configInstance() {
  agconnect.instance().configInstance(agConnectConfig);
  // set your app versionname
  agconnect.instance().setAppVersion('1.0.0')
}

// paste your SDK Code Snippet here, SDK Code Snippet can be found on your project general information
var agConnectConfig =
  {
    "agcgw": {},
    "client": {},
    "service": {},
    "region": "CN",
    "configuration_version": "1.0"
  }

