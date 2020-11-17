import agconnect from '@agconnect/api';
import '@agconnect/remoteConfig';

export class Crypt {
}

Crypt.prototype.encrypt = function (value) {
  return value + '---encrypt';
};

Crypt.prototype.decrypt = function (value) {
  return value.split('---')[0];
};

export function setUserInfoPersistence(saveMode) {
  agconnect.remoteConfig().setUserInfoPersistence(saveMode);
}

export function setCryptImp(cryptImpl) {
  agconnect.remoteConfig().setCryptImp(cryptImpl);
}

export function setFetchReqTimeoutMillis(fetchReqTimeoutMillis) {
  agconnect.remoteConfig().fetchReqTimeoutMillis = fetchReqTimeoutMillis;
}

export function setMinFetchIntervalMillis(minFetchIntervalMillis) {
  agconnect.remoteConfig().minFetchIntervalMillis = minFetchIntervalMillis;
}

export async function fetch() {
  return agconnect.remoteConfig().fetch().then(() => {
    return Promise.resolve();
  }).catch((err) => {
    return Promise.reject(err);
  });
}

export function apply() {
  return agconnect
    .remoteConfig().apply().then((res) => {
        return Promise.resolve(res);
      }
    ).catch(error => {
      return Promise.reject(error);
    });
}

export function applyLastLoad() {
  return agconnect
    .remoteConfig().loadLastFetched().then(async (res) => {
      if (res) {
        await agconnect.remoteConfig().apply(res);
      }
      return Promise.resolve(res);
    }
  ).catch(error => {
    return Promise.reject(error);
  });
}

export function getMergedAll() {
  return agconnect.remoteConfig().getMergedAll();
}

export function getValueAsBoolean(key) {
  return agconnect.remoteConfig().getValueAsBoolean(key);
}

export function getValueAsNumber(key) {
  return agconnect.remoteConfig().getValueAsNumber(key);
}

export function getValueAsString(key) {
  return agconnect.remoteConfig().getValueAsString(key);
}

export function getSource(key) {
  return agconnect.remoteConfig().getValue(key).getSource();
}

export function applyDefault(map) {
  return agconnect.remoteConfig().applyDefault(map);
}
export function clearAll() {
  agconnect.remoteConfig().clearAll();
}
