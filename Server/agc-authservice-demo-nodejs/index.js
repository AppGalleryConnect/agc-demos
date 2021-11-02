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

var {AGCClient, CredentialParser} = require("@agconnect/common-server");
var {AGCAuth} = require("@agconnect/auth-server");

var credential = CredentialParser.toCredential("[PATH]/agc-apiclient-xxx-xxx.json");
var yourClientName = "abc";
AGCClient.initialize(credential, yourClientName, "CN");

var authService = AGCAuth.getInstance(yourClientName);

/**
 * get privateKey and publicKey
 */
var keyPair = authService.generateKey();
console.log("privateKey:", keyPair.getPrivateKey());
console.log("privateKey:", keyPair.getPublicKey());

/**
 * do sign and get jwt
 */
var jwt = authService.sign("uid", "userName", "url", keyPair.getPrivateKey());
console.log("generate jwt:", jwt.getToken());
console.log("generate jwt expiration time:", jwt.getExpirationTime());

/**
 * export user info to file:[PATH]/xxx.json
 */
authService.exportUserData("[PATH]/xxx.json").then(res => {
    console.info("export success count:", res.getSuccessCount());
    console.info("export success count:", res.getErrorUsers());
    console.info("export fail uid detail:", res.getErrorUsersList());
}).catch(e => {
    console.error(e.message);
})

/**
 * import user from file:[PATH]/yyy.json
 */
authService.importUserData("[PATH]/yyy.json").then(res => {
    console.info("import success count:", res.getSuccessCount());
    console.info("import success count:", res.getErrorUsers());
    console.info("import fail uid detail:", res.getErrorUsersList());
}).catch(e => {
    console.error(e.message);
})

/**
 * verify access token
 */
authService.verifyAccessToken("your access token", true).then(res => {
    console.info("verify ok, uid is:", res.getAud());
    // get user info from AuthAccessToken object, include name/picture/phone/email and so on
}).catch(e => {
    console.error(e.message);
})

/**
 * revoke token of uid:a uid
 */
authService.revokeRefreshTokens("a uid").then(() => {
    console.info("revoke success");
}).catch(e => {
    console.error(e.message);
})