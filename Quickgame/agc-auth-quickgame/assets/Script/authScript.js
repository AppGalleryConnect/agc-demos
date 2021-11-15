var checkType = 'phone';
cc.Class({
  extends: cc.Component,
  properties: {
    account: {
      default: null,
      type: cc.EditBox,
    },
    password: {
      default: null,
      type: cc.EditBox,
    },
    PIN: {
      default: null,
      type: cc.EditBox,
    },
    accountInfo: {
      default: null,
      type: cc.EditBox,
    },
  },
  onLoad: function () {
  },
  update: function () { },

  start() {
    this.getUserInfo();
  },

  //Create a phone or email user
  createUser() {
    if (checkType == "phone") {
      agconnect.auth().createPhoneUser(new agconnect.auth.PhoneUser('86', this.account.string, this.password.string, this.PIN.string)).then((res) => {
        this.getUserInfo();
        console.log('createPhoneUser ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "createPhoneUser fail.";
      })
    } else {
      agconnect.auth().createEmailUser(new agconnect.auth.EmailUser(this.account.string, this.password.string, this.PIN.string)).then((res) => {
        this.getUserInfo();
        console.log('createEmailUser ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string ="createEmailUser fail.";
      })
    }
  },

  //Get email or phone verification code
  getPin() {
    if (checkType == "phone") {
      agconnect.auth().requestPhoneVerifyCode('86', this.account.string, 1001, 'zh_CN', 90).then((res) => {
        console.log('getPhonePin ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "getPhonePin fail.";
      });
    } else {
      agconnect.auth().requestEmailVerifyCode(this.account.string, 1001, 'zh_CN', 120).then((res) => {
        console.log('getEmailPin ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "getEmailPin fail.";
      });
    }
  },

  //Get user information
  getUserInfo() {
    agconnect.auth().getCurrentUser().then((user) => {
      if (user) {
        var userInfo = JSON.stringify({
          uid: user.getUid(),
          anonymous: user.isAnonymous(),
          displayName: user.getDisplayName(),
          email: user.getEmail(),
          phone: user.getPhone(),
          photoUrl: user.getPhotoUrl(),
          providerId: user.getProviderId(),
        });
        this.accountInfo.string = userInfo;
      } else {
        this.accountInfo.string = '';
      }
    }).catch((e) => {
      console.error(JSON.stringify(e))
      this.accountInfo.string = "getUserInfo fail.";
    });
  },
  //Anonymous login
  loginAnonymously() {
    agconnect.auth().signInAnonymously().then(() => {
      this.getUserInfo();
      console.log("loginAnonymously ok")
    }).catch((e) => {
      console.error(JSON.stringify(e))
      this.accountInfo.string = "loginAnonymously fail.";
    });
  },
  // login out
  loginOut() {
    agconnect.auth().signOut().then(() => {
      this.getUserInfo();
      console.log("loginOut ok")
    }).catch((e) => {
      console.error(JSON.stringify(e))
      this.accountInfo.string = "loginOut fail.";
    });
  },
  getPhoneCredential(countryCode, account, password, verifyCode) {
    if (verifyCode) {
      return agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, account, password, verifyCode);
    }
    return agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, account, password);
  },
  getEmailCredential(account, password, verifyCode) {
    if (verifyCode) {
      return agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(account, password, verifyCode);
    }
    return agconnect.auth.EmailAuthProvider.credentialWithPassword(account, password);
  },

  login() {
    if (checkType == "phone") {
      var credential = this.getPhoneCredential('86', this.account.string, this.password.string, this.PIN.string);
      agconnect.auth().signIn(credential).then((res) => {
        this.getUserInfo();
        console.log('phoneLogin ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "phoneLogin fail.";
      });
    } else {
      var credential = this.getEmailCredential(this.account.string, this.password.string, this.PIN.string);
      agconnect.auth().signIn(credential).then((res) => {
        this.getUserInfo();
        console.log('emailLogin ok');
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "emailLogin fail.";
      });
    }
  },

  deleteUser() {
    agconnect.auth().deleteUser().then(() => {
      this.getUserInfo();
      console.log('deleteUser ok');
    }).catch((e) => {
      console.error(JSON.stringify(e))
      this.accountInfo.string = "deleteUser fail.";
    });
  },
  hwGameLogin: function () {
    let that = this
    qg.gameLogin({
      forceLogin: 1,
      appid: "", // input your appid
      success: function (data) {
        let paramsObj = {
          playerSign: data.gameAuthSign,
          playerId: data.playerId,
          displayName: data.displayName,
          imageUrl: data.imageUri,
          playerLevel: data.playerLevel,
          signTs: data.ts
        };
        agconnect.auth().signIn(agconnect.auth.HwGameAuthProvider.credentialWithPlayerSign(paramsObj)).then(res => {
          console.log("hwGameLogin ok")
          that.getUserInfo();
        }).catch((e) => {
          console.error(JSON.stringify(e))
          this.accountInfo.string = "hwGameLogin fail.";
        })
      },
      fail: function (data, code) {
        let errorStr = "gameLoginWithReal fail:" + JSON.stringify(data) + ", code:" + code
        console.error(errorStr); 
        this.accountInfo.string = "gameLoginWithReal fail.";
      }
    })
  },

  checkFunc(type) {
    if (type.node.name == 'toggle1') {
      checkType = 'phone';
    } else {
      checkType = 'email';
    }
  },
  // Re authentication of phone user
  reauth() {
    let credential = '';
    if (checkType == "phone") {
      if (this.PIN.string) {
        credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode('86', this.account.string, this.password.string, this.PIN.string);
      } else {
        credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword('86', this.account.string, this.password.string);
      }
      agconnect.auth().getCurrentUser().then(user => {
        if (!user) {
          console.error('no user login');
          this.accountInfo.string = 'no user login';
          return;
        }
        user.userReauthenticate(credential).then(res => {
          console.log("userReauthenticate ok")
        })
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "userReauthenticate fail.";
      });
    } else {
      if (this.PIN.string) {
        credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(this.account.string, this.password.string, this.PIN.string);
      } else {
        credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(this.account.string, this.password.string);
      }
      agconnect.auth().getCurrentUser().then(user => {
        if (!user) {
          console.error('no user login');
          this.accountInfo.string = 'no user login';
          return;
        }
        user.userReauthenticate(credential).then(res => {
          console.log("userReauthenticate ok")
        })
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "userReauthenticate fail.";
      });
    }
  },

  // Update user profile
  updateProfile() {
    let obj = {
      displayName: 'agc user',
      photoUrl: 'a url',
    };
    agconnect.auth().getCurrentUser().then(user => {
      if (!user) {
        console.error('no user login');
        this.accountInfo.string = 'no user login';
        return;
      }
      user.updateProfile(obj).then(res => {
        this.getUserInfo();
        console.log("updateProfile ok")
      })
    }).catch((e) => {
      console.error(JSON.stringify(e))
      this.accountInfo.string = "updateProfile fail.";
    });
  },

  resetPWDPin() {
    if (checkType == "phone") {
      agconnect.auth().requestPhoneVerifyCode('86', this.account.string, 1002, 'zh_CN', 90).then((res) => {
        console.log("phone pin send ok")
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "phone pin send fail.";
      })
    } else {
      agconnect.auth().requestEmailVerifyCode(this.account.string, 1002, 'zh_CN', 120).then((ret) => {
        console.log("email pin send ok")
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "email pin send fail.";
      });
    }
  },

  resetPassword() {
    if (checkType == "phone") {
      agconnect.auth().resetPasswordByPhone('86', this.account.string, this.password.string, this.PIN.string).then((res) => {
        console.log("resetPassword by phone ok")
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "resetPassword by phone fail.";
      })
    } else {
      agconnect.auth().resetPasswordByEmail(this.account.string, this.password.string, this.PIN.string).then((res) => {
        console.log("resetPassword by email ok")
      }).catch((e) => {
        console.error(JSON.stringify(e))
        this.accountInfo.string = "resetPassword by email fail.";
      })
    }
  }
});