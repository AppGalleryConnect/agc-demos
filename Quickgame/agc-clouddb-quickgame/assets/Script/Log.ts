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

const {ccclass, property} = cc._decorator;

@ccclass
export default class Log extends cc.Component {

    @property(cc.Label)
    label: cc.Label = null;

    @property(cc.Button)
    button: cc.Button = null;

    @property(cc.EditBox)
    countryCode: cc.EditBox = null;

    @property(cc.EditBox)
    phoneNumber: cc.EditBox = null;

    @property(cc.EditBox)
    password: cc.EditBox = null;

    @property(cc.Label)
    errorMessage: cc.Label = null;

    changeCode() {
        console.log(this.countryCode.string);
    }

    changePhone() {
        console.log(this.phoneNumber.string);
    }

    changePassword() {
        console.log(this.password.string);
    }

    async submit() {
        try {
            const aa = await agconnect.auth().getCurrentUser();
            if (aa) {
                console.log('already signIn');
                this.errorMessage.string = 'already signIn';
                cc.director.loadScene('Main');
            }
            const credential = agconnect.auth.PhoneAuthProvider
                .credentialWithPassword(this.countryCode.string, this.phoneNumber.string, this.password.string);
            const d = await agconnect.auth().signIn(credential);
            console.log(JSON.stringify(d));
            console.log('signIn success');
            cc.director.loadScene('Main');
        } catch (e) {
            console.error("signIn Failed");
            this.errorMessage.string = '[error] ' + JSON.stringify(e);
        }
    }

    back() {
        cc.director.loadScene('Main');
    }

    async logout() {
        try {
            await agconnect.auth().signOut();
            this.errorMessage.string = 'signOut success';
        } catch (e) {
            console.error("signOut Failed");
            this.errorMessage.string = '[error] ' + JSON.stringify(e);
        }
    }
}
