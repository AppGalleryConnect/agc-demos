import agconnect from '@agconnect/api';
import '@agconnect/instance';
import '@agconnect/function';

export function call(httpTriggerURI, reqBody, timeout){
  let functionCallable = agconnect.function().wrap(httpTriggerURI);
  functionCallable.timeout = timeout;
  return functionCallable.call(reqBody);
}

export function callClone(httpTriggerURI, reqBody, timeout){
  let functionCallable = agconnect.function().wrap(httpTriggerURI);
  return functionCallable.clone(timeout).call(reqBody);
}

export class Crypt {
}

Crypt.prototype.encrypt = function (value) {
  return value + '---encrypt';
};

Crypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

//set a default cryptImpl, this cryptImpl can encrypt your client token as well
export function setCryptImp(cryptImpl) {
  agconnect.instance().setCryptImp(cryptImpl);
}
