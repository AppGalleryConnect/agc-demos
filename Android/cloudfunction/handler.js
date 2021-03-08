//handler.js is a demo for handler function.

let myHandler = function(event, context) {
    var number1;
    var number2;
    if (event.body) {
        var reqBody = JSON.parse(event.body);
        number1=reqBody.number1;
        number2=reqBody.number2;
    } else {
        number1=event.number1;
        number2=event.number2;
    }

    var sum = number1+number2;
    var obj={"result":sum}
    var res = new context.HTTPResponse(obj, {
        "res-type": "context.env",
        "faas-content-type": "json"
    }, "application/json", "200");
    //send response
    context.callback(res);
};

module.exports.myHandler = myHandler