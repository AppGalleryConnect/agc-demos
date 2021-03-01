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
import '@agconnect/remoteconfig';
import "@agconnect/instance";

export class RCSCrypt {
}

RCSCrypt.prototype.encrypt = function (value) {
  return value + '---RCSEncrypt';
};

RCSCrypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

export class Crypt {
}

Crypt.prototype.encrypt = function (value) {
  return value + '---encrypt';
};

Crypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

/**
 * Set where auth-related data is stored locally。0：indexedDB；1：sessionStorage；2：memory
 * @param saveMode storage mode
 */
export function setUserInfoPersistence(saveMode) {
  agconnect.remoteConfig().setUserInfoPersistence(saveMode);
}

/**
 * Set the object to encrypt/decrypt Info, sach as clinet token.
 * @param cryptImpl object used to encrypt/decrypt
 */
export function setCryptImp(cryptImpl) {
  agconnect.instance().setCryptImp(cryptImpl);
}

/**
 * Set the object to encrypt/decrypt config Info,only for remoteconfig.
 * @param cryptImpl object used to encrypt/decrypt
 */
export function setRCSCryptImp(cryptImpl) {
  agconnect.remoteConfig().setCryptImp(cryptImpl);
}

/**
 * Set timeout interval for sending a fetch request, in milliseconds. The default value is 1 minute.
 * @param fetchReqTimeoutMillis timeout interval for sending a fetch request
 */
export function setFetchReqTimeoutMillis(fetchReqTimeoutMillis) {
  agconnect.remoteConfig().fetchReqTimeoutMillis = fetchReqTimeoutMillis;
}

/**
 * Interval for fetching configuration updates from the cloud, in milliseconds. The default interval is 12 hours.
 * @param minFetchIntervalMillis Interval for fetching configuration updates from the cloud
 */
export function setMinFetchIntervalMillis(minFetchIntervalMillis) {
  agconnect.remoteConfig().minFetchIntervalMillis = minFetchIntervalMillis;
}

/**
 * initialization
 */
export function initialized() {
  return agconnect.remoteConfig().initialized().then(() => {
    return Promise.resolve();
  }).catch((err) => {
    return Promise.reject(err);
  });
}

/**
 * Fetches latest parameter values from the cloud at the default interval of 12 hours.
 * If the method is called within an interval, cached data is returned.
 */
export function fetch() {
  return agconnect.remoteConfig().fetch().then(() => {
    return Promise.resolve();
  }).catch((err) => {
    return Promise.reject(err);
  });
}

/**
 * Applies parameter values fetched from the cloud.
 */
export function apply() {
  return agconnect
    .remoteConfig().apply().then((res) => {
        return Promise.resolve(res);
      }
    ).catch(error => {
      return Promise.reject(error);
    });
}

/**
 * Loads the cached data that is successfully fetched from the cloud last time.
 */
export function applyLastLoad() {
  return agconnect
    .remoteConfig().loadLastFetched().then(async (res) => {
        if (res) {
          await agconnect.remoteConfig().apply(res);
        }
        return Promise.resolve(res);
      }
    ).catch(error => {
      return Promise.reject(error);
    });
}

/**
 * Returns all values obtained after the combination of the default values and values fetched from the cloud.
 */
export function getMergedAll() {
  return agconnect.remoteConfig().getMergedAll();
}

/**
 * Returns the value of the boolean type for a key.
 * @param key Key of a parameter specified in Remote Configuration.
 */
export function getValueAsBoolean(key) {
  return agconnect.remoteConfig().getValueAsBoolean(key);
}

/**
 * Returns the value of the number type for a key.
 * @param key Key of a parameter specified in Remote Configuration.
 */
export function getValueAsNumber(key) {
  return agconnect.remoteConfig().getValueAsNumber(key);
}

/**
 * Returns the value of the string type for a key.
 * @param key Key of a parameter specified in Remote Configuration.
 */
export function getValueAsString(key) {
  return agconnect.remoteConfig().getValueAsString(key);
}

/**
 * Returns the source of a value.
 * @param key Key of a parameter specified in Remote Configuration.
 */
export function getSource(key) {
  return agconnect.remoteConfig().getValue(key).getSource();
}

/**
 * Sets a default value for a parameter.
 * @param map Default parameters in map format.
 */
export function applyDefault(map) {
  return agconnect.remoteConfig().applyDefault(map);
}

/**
 * Clears all cached data, including the data fetched from the cloud, default values passed,
 * custom timeout interval of the fetch request, and interval for fetching configuration updates from the cloud.
 */
export function clearAll() {
  return agconnect.remoteConfig().clearAll();
}
