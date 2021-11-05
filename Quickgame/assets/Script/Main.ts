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

import * as context from './config/agconnect-services.json';
import { operate } from "./operate";

const {ccclass, property} = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {

    @property(cc.Label)
    dataString: cc.Label = null;

    protected async onLoad() {
        try {
            if (operate.isOpenZone()) {
                const da = await operate.queryDate();
                this.dataString.string = JSON.stringify(da);
            }
        } catch (e) {
            console.error('error');
            console.error(JSON.stringify(e));
        }
    }

    cilck_to_Log() {
        cc.director.loadScene('Log');
    }

    cilck_to_Zone() {
        cc.director.loadScene('Zone');
    }

    cilck_to_Insert() {
        cc.director.loadScene('Insert');
    }

    cilck_to_Query() {
        cc.director.loadScene('Query');
    }

    cilck_to_Delete() {
        cc.director.loadScene('Delete');
    }

    init() {
        try {
            agconnect.instance().configInstance(context);
            operate.initCloudDB();
            this.dataString.string = 'init CloudDB success! please login'
        } catch (e) {
            console.error('init CloudDB failed');
            this.dataString.string = '[error] ' + JSON.stringify(e);
        }
    }
}
