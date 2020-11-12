<template>
    <div class="login-container">
        <el-form
            :model="dataForm_sdk"
            :rules="rules"
            status-icon
            ref="ruleForm_sdk"
            v-if="loginState == false"
            label-position="left"
            label-width="0px"
            class="demo-ruleForm login-page"
            ><h3 class="title">JS-SDK</h3>
            <el-form-item prop="account">
                <el-input
                    type="text"
                    v-model="dataForm_sdk.account"
                    auto-complete="off"
                    placeholder="手机/邮箱"
                ></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input
                    type="password"
                    v-model="dataForm_sdk.password"
                    auto-complete="off"
                    placeholder="密码"
                ></el-input>
            </el-form-item>
            <el-form-item prop="verifyCode">
                <el-input type="text" v-model="dataForm_sdk.verifyCode" auto-complete="off" placeholder="验证码">
                    <el-button slot="suffix" type="info" size="mini" @click="getVerifyCode">获取验证码</el-button>
                </el-input>
            </el-form-item>
            <br />
            <el-form-item style="width: 100%;">
                <el-row>
                    <el-button type="primary" size="medium" style="width: 30%;" @click="loginByPwd">密码登录</el-button>
                    <el-button type="primary" size="medium" style="width: 30%;" @click="loginByVerifyCode"
                        >验证码登录</el-button
                    >
                    <el-button type="primary" size="medium" style="width: 30%;" @click="loginAnonymously"
                        >匿名登录</el-button
                    >
                </el-row>
                <br />
                <el-button type="success" size="medium" style="width: 100%;" @click="createUser" round
                    >创建用户</el-button
                >
                <br /><br />
                <el-collapse accordion>
                    <el-collapse-item>
                        <template slot="title">登录场景</template>
                        <el-radio-group v-model="provider" @change="providerChange">
                            <el-radio label="phone">手机</el-radio>
                            <el-radio label="email">邮箱</el-radio>
                            <el-radio label="QQ" disabled>QQ</el-radio>
                            <el-radio label="weChat">微信</el-radio>
                        </el-radio-group>
                        <br />
                    </el-collapse-item>
                    <el-collapse-item>
                        <template slot="title">存储模式</template>
                        <el-radio-group v-model="saveMode" @change="setStorageMode">
                            <el-radio label="2">MEMORY</el-radio>
                            <el-radio label="1">SESSION</el-radio>
                            <el-radio label="0">INDEXDB</el-radio>
                        </el-radio-group>
                        <br />
                    </el-collapse-item>
                </el-collapse>
            </el-form-item>
        </el-form>
        <div align="center" v-if="loginState == true">
            <h3 class="title">User Info</h3>
            <el-form :label-position="labelPosition" style="width: 20%;" label-width="80px" :model="accountInfo">
                <el-form-item label="UID:">
                    {{ accountInfo.uid }}
                </el-form-item>
                <el-form-item label="Anonymous:">
                    {{ accountInfo.anonymous }}
                </el-form-item>
                <el-form-item label="email:">
                    {{ accountInfo.email }}
                </el-form-item>
                <el-form-item label="phone:">
                    {{ accountInfo.phone }}
                </el-form-item>
            </el-form>
            <br /><br />
            <el-button type="primary" size="medium" style="width: 20%;" @click="logOut">注销</el-button>
            <br /><br />
            <el-button type="danger" size="medium" style="width: 20%;" @click="deleteUser">删除用户</el-button>
        </div>
    </div>
</template>

<script>
import * as agc from './auth';
import { getSaveMode, setSaveMode } from './storage';

export default {
    data() {
        return {
            saveMode: '2',
            provider: 'phone',
            loginState: false,
            dataForm_sdk: {
                account: '',
                password: '',
                verifyCode: '',
            },
            rules: {
                email: [{ required: true, message: '请输入账号', trigger: 'blur' }],
                password: [{ required: false, message: '请输入密码', trigger: 'blur' }],
            },
            dialogVisible: false,
            labelPosition: 'left',
            accountInfo: {
                name: '',
                uid: '',
                anonymous: '',
                email: '',
                phone: '',
            },
        };
    },
    async created() {
        let agConnectConfig = {
            // Initialize the agconnect in the service worker by passing in
            // your app's agconnect config object.
        };
        agc.initConfig(agConnectConfig);
        let saveMode = await getSaveMode('saveMode');
        console.log('save mode is' + saveMode);
        agc.setUserInfoPersistence(saveMode ? parseInt(saveMode) : 2);
        agc.getUserInfo().then(
            (user) => {
                this.getUserInfo();
            },
            (reason) => {
                console.log('get user error');
            },
        );
    },
    methods: {
        async setStorageMode() {
            agc.setUserInfoPersistence(parseInt(this.saveMode));
            console.log(this.saveMode);
            await setSaveMode('saveMode', this.saveMode);
        },
        providerChange() {
            console.log('provider :', this.provider);
            this.dataForm_sdk.account = '';
            this.dataForm_sdk.password = '';
            this.dataForm_sdk.verifyCode = '';

            if (this.provider == 'weChat') {
                var obj = new WxLogin({
                    id: 'login_container',
                    appid: 'wx2d39556764a4cc31',
                    scope: 'snsapi_userinfo',
                    redirect_uri: 'http%3A%2F%2F127.0.0.1%3A8080',
                    state: 'STATE',
                    style: '',
                    href: '',
                });
                let accessToken = '';
                let openId = '';
            }
        },
        loginByPwd() {
            if (this.dataForm_sdk.password == '') {
                alert('Please input password!');
                return;
            }
            switch (this.provider) {
                case 'phone':
                    agc.loginWithPhone('86', this.dataForm_sdk.account, this.dataForm_sdk.password)
                        .then((res) => {
                            alert('login successfully!');
                            this.getUserInfo();
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                case 'email':
                    agc.loginWithEmail(this.dataForm_sdk.account, this.dataForm_sdk.password).then(
                        () => {
                            alert('login successfully!');
                            this.getUserInfo();
                        },
                        (reason) => {
                            alert(reason);
                        },
                    );
                    break;
                default:
                    break;
            }
        },
        loginByVerifyCode() {
            if (this.dataForm_sdk.verifyCode == '') {
                alert('Please typein verifycode!');
                return;
            }

            switch (this.provider) {
                case 'phone':
                    agc.loginWithPhone(
                        '86',
                        this.dataForm_sdk.account,
                        this.dataForm_sdk.password,
                        this.dataForm_sdk.verifyCode,
                    )
                        .then((res) => {
                            alert('login successfully!');
                            this.getUserInfo();
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                case 'email':
                    agc.loginWithEmail(
                        this.dataForm_sdk.account,
                        this.dataForm_sdk.password,
                        this.dataForm_sdk.verifyCode,
                    )
                        .then((res) => {
                            alert('login successfully!');
                            this.getUserInfo();
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                case 'QQ':
                    break;
                case 'weChat':
                    break;
                default:
                    break;
            }
        },
        loginAnonymously() {
            console.log('JS-SDK login anonymously!');
            agc.loginAnonymously()
                .then((res) => {
                    alert('login successfully!');
                    this.getUserInfo();
                })
                .catch((err) => {
                    alert(err);
                });
        },
        createUser() {
            console.log('create user...');
            switch (this.provider) {
                case 'phone':
                    agc.createPhoneUser(
                        '86',
                        this.dataForm_sdk.account,
                        this.dataForm_sdk.verifyCode,
                        this.dataForm_sdk.password,
                    )
                        .then((res) => {
                            alert('create user successfully!');
                            this.getUserInfo();
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                case 'email':
                    agc.createEmailUser(
                        this.dataForm_sdk.account,
                        this.dataForm_sdk.password,
                        this.dataForm_sdk.verifyCode,
                    )
                        .then((res) => {
                            alert('create user successfully!');
                            this.getUserInfo();
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                default:
                    break;
            }
        },
        getUserInfo() {
            agc.getUserInfo()
                .then((user) => {
                    this.accountInfo.anonymous = user.isAnonymous();
                    this.accountInfo.uid = user.getUid();
                    this.accountInfo.name = user.getDisplayName();
                    this.accountInfo.email = user.getEmail();
                    this.accountInfo.phone = user.getPhone();
                    this.loginState = true;
                    this.dialogVisible = true;
                })
                .catch((err) => {
                    alert(err);
                });
        },
        getVerifyCode() {
            console.log('request for email verifycode...');
            this.dataForm_sdk.verifyCode = '';
            switch (this.provider) {
                case 'phone':
                    agc.getVerifyCode('86', this.dataForm_sdk.account, 'zh_CN', 90, 90)
                        .then((ret) => {
                            alert('verify code sent by AGC!');
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                case 'email':
                    agc.getEmailVerifyCode(this.dataForm_sdk.account, 'zh_CN', 120)
                        .then((ret) => {
                            alert('verify code sent by AGC!');
                        })
                        .catch((err) => {
                            alert(err);
                        });
                    break;
                default:
                    break;
            }
        },
        logOut() {
            console.log('log out!');
            agc.logout()
                .then(() => {
                    alert('log out!');
                    this.accountInfo = {
                        name: '',
                        uid: '',
                        anonymous: '',
                        email: '',
                        phone: '',
                    };
                    this.loginState = false;
                })
                .catch((err) => {
                    alert(err);
                });
        },
        deleteUser() {
            this.$confirm('永久删除当前用户, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            })
                .then(() => {
                    console.log('delete current user...');
                    agc.deleteUser();
                    this.loginState = false;
                    this.accountInfo = {
                        name: '',
                        uid: '',
                        anonymous: '',
                        email: '',
                        phone: '',
                    };
                })
                .catch((err) => {
                    alert(err);
                });
        },
    },
};
</script>
<style scoped>
.login-container {
    width: 100%;
    height: 100%;
}

.login-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
}

label.el-checkbox.remember {
    margin: 0px 0px 15px;
    text-align: left;
}
</style>
