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

<template>
  <div class="remoteconfigContainer">
    <el-form :model="dataForm_sdk" :rules="rules" status-icon label-position="left" label-width="80px"
             class="demo-ruleForm remoteconfig-page">
      <h3 class="title">JS-RemoteConfig-SDK</h3>
      <el-form-item label="Property Settings" label-width="200px" class="attriSetTitle">
      </el-form-item>
      <el-form-item label="FetchReqTimeoutMillis" label-width="200px" class="fetchReqTimeoutMillisText">
        <el-input type="text" v-model="dataForm_sdk.fetchReqTimeoutMillis" placeholder="Default 1 minute"></el-input>
      </el-form-item>
      <el-form-item label="MinFetchIntervalMillis" label-width="200px" class="minFetchIntervalMillisText">
        <el-input type="text" v-model="dataForm_sdk.minFetchIntervalMillis" placeholder="Default 12 hours"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit" class="onSubmitButton">OK</el-button>
        <el-button @click="reset" class="resetButton">Reset</el-button>
      </el-form-item>

      <el-form-item label="FetchConfig" label-width="200px" class="fetchConfigTitle">
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetch" class="fetchButton">Fetch</el-button>
        <el-button type="primary" @click="applyConfig" class="applyConfigButton">Apply</el-button>
        <el-button type="primary" @click="applyLastLoad" class="applyLastLoadButton">LoadLastFetched</el-button>
      </el-form-item>
      <el-form-item label="DefaultConfig" label-width="200px" class="setDefaultVauleTitle">
      </el-form-item>
      <el-form-item>
        <el-input type="textarea" :disabled="false" v-model="dataForm_sdk.defaultConfig"
                  placeholder="Please enter JSON format string"
                  class="inputValueText"></el-input>
      </el-form-item>
      <el-button type="primary" @click="applyDefault" class="applyDefaultButton">applyDefault</el-button>
      <el-form-item label="GetVaule" class="getVauleTitle">
      </el-form-item>
      <el-form-item>
        <el-input type="textarea" :disabled="false" autosize="autosize" v-model="dataForm_sdk.valueText"
                  class="getValueText"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input type="text" :disabled=valueTextDisable v-model="dataForm_sdk.key"
                  placeholder="Input your key" class="keyInput"></el-input>
        <el-button type="primary" @click="getValue" class="getValueButton">OK</el-button>
        <el-button @click="clearAll" class="clearValueButton">ClearAll</el-button>
      </el-form-item>
      <el-radio-group v-model="radio" @change="providerChange">
        <el-radio label="all">allConfig</el-radio>
        <el-radio label="bool">bool value</el-radio>
        <el-radio label="number">int value</el-radio>
        <el-radio label="string">string value</el-radio>
      </el-radio-group>
      <el-form-item>
      </el-form-item>
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">StorageMode</template>
          <el-radio-group v-model="saveMode" @change="setStorageMode">
            <el-radio label="0">INDEXDB</el-radio>
            <el-radio label="1">SESSION</el-radio>
            <el-radio label="2">MEMORY</el-radio>
          </el-radio-group>
          <br/>
        </el-collapse-item>
      </el-collapse>
    </el-form>
  </div>
</template>

<script>
  import * as agc from './remoteConfig';
  import {getSaveMode, setSaveMode} from './storage';

  export default {
    name: 'remoteconfigDemo',
    data() {
      return {
        dataForm_sdk: {
          fetchReqTimeoutMillis: '',
          minFetchIntervalMillis: '',
          key: '',
          valueText: '',
          defaultConfig: ''
        },
        radio: 'all',
        saveMode: '0',
        valueTextDisable: true,
        rules: {},
      };
    },
    async created() {
      // Gets the storage location last set by the user in the demo and inherits it
      agc.setCryptImp(new agc.Crypt());

      let saveMode = await getSaveMode('saveMode');
      this.saveMode = saveMode ? saveMode : '0';

      // setUserInfoPersistence and setCryptImp must be executed when init
      agc.setUserInfoPersistence(parseInt(this.saveMode));

      agc.setRCSCryptImp(new agc.RCSCrypt());
      await agc.initialized();
    },
    methods: {
      onSubmit() {
        if (isNaN(Number(this.dataForm_sdk.fetchReqTimeoutMillis)) || isNaN(Number(this.dataForm_sdk.minFetchIntervalMillis))) {
          alert('Please input positive number !');
          return;
        }
        if (Number(this.dataForm_sdk.fetchReqTimeoutMillis) < 0 || Number(this.dataForm_sdk.minFetchIntervalMillis) < 0) {
          alert('Please input positive number !');
          return;
        }
        if (this.dataForm_sdk.fetchReqTimeoutMillis != "") {
          agc.setFetchReqTimeoutMillis(this.dataForm_sdk.fetchReqTimeoutMillis);
        } else {
          agc.setFetchReqTimeoutMillis(60 * 1000);
        }
        if (this.dataForm_sdk.minFetchIntervalMillis != "") {
          agc.setMinFetchIntervalMillis(this.dataForm_sdk.minFetchIntervalMillis);
        } else {
          agc.setMinFetchIntervalMillis(12 * 60 * 60 * 1000);
        }
        alert('property set successfully!');
      },
      reset() {
        this.dataForm_sdk.fetchReqTimeoutMillis = '';
        this.dataForm_sdk.minFetchIntervalMillis = ''
      },
      async fetch() {
        await agc.fetch().then((res) => {
          alert('fetch successfully!');
        }).catch((err) => {
          alert(err.message);
        });
      },
      getValue() {
        if (this.radio == "all") {
          let obj = Object.create(null);
          let resultMap = agc.getMergedAll();
          for (let [k, v] of resultMap) {
            obj[k] = '( value:' + v.value + ', source:' + v.source + ' )';
          }
          this.dataForm_sdk.valueText = JSON.stringify(obj);
        } else if (this.radio == "bool") {
          this.dataForm_sdk.valueText = '( value:' + agc.getValueAsBoolean(this.dataForm_sdk.key) + ', source:' + agc.getSource(this.dataForm_sdk.key) + ' )';
        } else if (this.radio == "number") {
          this.dataForm_sdk.valueText = '( value:' + agc.getValueAsNumber(this.dataForm_sdk.key) + ', source:' + agc.getSource(this.dataForm_sdk.key) + ' )';
        } else {
          this.dataForm_sdk.valueText = '( value:' + agc.getValueAsString(this.dataForm_sdk.key) + ', source:' + agc.getSource(this.dataForm_sdk.key) + ' )';
        }
      },
      applyConfig() {
        return agc
          .apply().then((res) => {
              if (res) {
                alert('apply successfully!');
              } else {
                alert('apply failed!');
              }
            }
          ).catch(error => {
            alert(error.message);
          });
      },
      applyLastLoad() {
        return agc
          .applyLastLoad().then((res) => {
              if (res) {
                alert('applyLastLoad successfully!');
              } else {
                alert('applyLastLoad failed!');
              }
            }
          ).catch(error => {
            alert(error.message);
          });
      },

      providerChange() {
        if (this.radio != 'all') {
          this.valueTextDisable = false;
        } else {
          this.valueTextDisable = true;
          this.dataForm_sdk.key = '';
        }
      },
      applyDefault() {
        if (this.dataForm_sdk.defaultConfig != '') {
          if (typeof this.dataForm_sdk.defaultConfig == 'string') {
            try {
              var defaultConfigJson = JSON.parse(this.dataForm_sdk.defaultConfig);
              if (typeof defaultConfigJson == 'object' && defaultConfigJson) {
                let defaultConfigMap = new Map();
                for (var k in defaultConfigJson) {
                  defaultConfigMap.set(k, JSON.stringify(defaultConfigJson[k]));
                }
                agc.applyDefault(defaultConfigMap);
                alert('applyDefault success');
              } else {
                alert('please input JSON string!');
                this.dataForm_sdk.defaultConfig = '';
              }
            } catch (e) {
              alert('please input JSON string!');
              this.dataForm_sdk.defaultConfig = '';
            }
          }
        } else {
          alert('please input JSON string!');
          this.dataForm_sdk.defaultConfig = '';
        }
      },

      async setStorageMode() {
        // Save the storage location locally
        await setSaveMode('saveMode', this.saveMode);
        // refresh the page to let new saveMode works
        this.$router.go(0);
      },
      clearAll() {
        agc.clearAll().then(()=>{
          alert('clearAll OK');
        });
      }
    },
  };

</script>

<style scoped>
  .remoteconfig-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: auto;
    width: 800px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .attriSetTitle,
  .getVauleTitle,
  .fetchConfigTitle,
  .setDefaultVauleTitle {
    border-bottom: 1px solid #eaeaea;
  }

  .onSubmitButton,
  .resetButton,
  .applyConfigButton,
  .applyLastLoadButton,
  .fetchButton {
    width: 120px;
  }

  .applyLastLoadButton {
    width: 150px;
  }

  .minFetchIntervalMillisText,
  .fetchReqTimeoutMillisText {
    margin-left: 120px;
    margin-right: 120px;
  }

  .getValueButton {
    margin-left: 30px;
    width: 120px;
  }

  .applyDefaultButton {
    margin-left: 530px;
    width: 120px;
  }

  .getValueText,
  .inputValueText {
    width: 600px;
  }

  .keyInput {
    width: 300px;
  }
</style>
