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
import '@agconnect/function';

/**
 * Sets the function to be called using the HTTP trigger identifier of the function and
 * call the function
 * @param httpTriggerURI  HTTP trigger identifier of the cloud function to be called
 * @param reqBody  Input parameters of a cloud function
 * @param timeout Timeout interval for calling a cloud function, in milliseconds
 */
export function call(httpTriggerURI, reqBody, timeout){
  let functionCallable = agconnect.function().wrap(httpTriggerURI);
  functionCallable.timeout = timeout;
  return functionCallable.call(reqBody);
}

/**
 * Creates a FunctionCallable instance with a specified timeout interval in milliseconds and
 * call the function
 * @param httpTriggerURI  HTTP trigger identifier of the cloud function to be called
 * @param reqBody  Input parameters of a cloud function
 * @param timeout Timeout interval for calling a cloud function, in milliseconds
 */
export function callClone(httpTriggerURI, reqBody, timeout){
  let functionCallable = agconnect.function().wrap(httpTriggerURI);
  return functionCallable.clone(timeout).call(reqBody);
}

export class Crypt {
}

Crypt.prototype.encrypt = function (value) {
  return value + '---encrypt';
};

Crypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

/**
 * Set the object to encrypt/decrypt Info, sach as clinet token.
 * @param cryptImpl object used to encrypt/decrypt
 */
export function setCryptImp(cryptImpl) {
  agconnect.instance().setCryptImp(cryptImpl);
}
