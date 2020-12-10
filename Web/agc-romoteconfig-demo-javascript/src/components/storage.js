import agconnect from '@agconnect/api';
import '@agconnect/storage';

let storage = agconnect.instance().getService('AGCStorageService').getStorageNewInstance(0);

function getSaveMode(key) {
  return storage.get(key);
}

function setSaveMode(key, value) {
  return storage.set(key, value);
}

export { getSaveMode, setSaveMode };
