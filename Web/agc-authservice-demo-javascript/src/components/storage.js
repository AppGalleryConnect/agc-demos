import agconnect from '@agconnect/api';
import '@agconnect/storage';

function getSaveMode(key) {
    return agconnect.instance().getService('AGCStorageService').getStorage(0).get(key);
}

function setSaveMode(key, value) {
    return agconnect.instance().getService('AGCStorageService').getStorage(0).set(key, value);
}

export { getSaveMode, setSaveMode };
