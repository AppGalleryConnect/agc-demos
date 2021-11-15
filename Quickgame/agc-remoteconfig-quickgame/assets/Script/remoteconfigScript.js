cc.Class({
    extends: cc.Component,

    properties: {
        configBox: {
            default: null,
            type: cc.EditBox,
        },
        inputconfigBox: {
            default: null,
            type: cc.EditBox,
        },

        FetchReqTimeout: {
            default: null,
            type: cc.EditBox,
        },
        minFetchInterval: {
            default: null,
            type: cc.EditBox,
        },
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad() {

    },

    start() {
        agconnect.remoteConfig().initialized().then(() => {
            console.log("initialized ok")
        }).catch((err) => {
            console.error("initialized fail")
            this.configBox.string = "initialized fail.";
        });
    },

    fetch() {
        agconnect.remoteConfig().fetchReqTimeoutMillis = this.FetchReqTimeout.string;
        agconnect.remoteConfig().minFetchIntervalMillis = this.minFetchInterval.string;

        agconnect.remoteConfig().fetch().then(() => {
            agconnect.remoteConfig().apply();
        }).catch((err) => {
            console.error(JSON.stringify(err));
            this.configBox.string = "fetch and apply fail.";
        });
    },
    setDefaultConfig() {
        var defaultConfigJson = JSON.parse(this.inputconfigBox.string);
        if (typeof defaultConfigJson == 'object' && defaultConfigJson) {
            let defaultConfigMap = new Map();
            for (var k in defaultConfigJson) {
                defaultConfigMap.set(k, JSON.stringify(defaultConfigJson[k]));
            }
            agconnect.remoteConfig().applyDefault(defaultConfigMap);
        }
    },

    showAllConfig() {
        let map = agconnect.remoteConfig().getMergedAll();
        let obj = {};
        for (let [k, v] of map) {
            obj[k] = '( value:' + v.getValueAsString() + ', source:' + v.getSource() + ' )';
        }
        this.configBox.string = JSON.stringify(obj);
    }
});
