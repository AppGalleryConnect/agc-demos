cc.Class({
    extends: cc.Component,

    properties: {
        HttpTrigger: {
            default: null,
            type: cc.EditBox,
          },
          Body: {
            default: null,
            type: cc.EditBox,
          },
          result: {
            default: null,
            type: cc.EditBox,
          },
        // defaults, set visually when attaching this script to the Canvas
    },

    // use this for initialization
    onLoad: function () {
    },
    start() {
    },

    runFunction(){
      console.log("runFunction")
      let functionCallable = agconnect.function().wrap(this.HttpTrigger.string);
      functionCallable.call(JSON.parse(this.Body.string)).then(res=>{  
        console.log("runFunction ok")
        this.result.string = JSON.stringify(res.getValue())
      }).catch(e=>{
        console.error(JSON.stringify(e));
        this.result.string = "runFunction fail";
      });
    },

    // called every frame
    update: function (dt) {

    },
});
