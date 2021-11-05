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

import { operate } from "./operate";

const {ccclass, property} = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {

    @property(cc.Label)
    label: cc.Label = null;

    @property
    text: string = 'hello';

    @property(cc.EditBox)
    zoneName: cc.EditBox = null;

    async click() {
        try {
            await operate.openZone(this.zoneName.string);
            cc.director.loadScene('Main');
        } catch (e) {
            console.error('open CloudDB Zone failed');
            this.label.string = '[error] ' + JSON.stringify(e);
        }
    }

    async close() {
        try {
            await operate.closeZone();
            cc.director.loadScene('Main');
        } catch (e) {
            console.error('close zone failed');
            this.label.string = '[error] ' + JSON.stringify(e);
        }
    }

    back() {
        cc.director.loadScene('Main');
    }
}
