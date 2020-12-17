const {AGCClient, CredentialParser} = require('@agconnect/common-server');
const {StorageManagement} = require('@agconnect/cloudstorage-server');
const fs = require('fs');

const credential = CredentialParser.toCredential('[PATH]/agc-apiclient-xxx-xxx.json');
AGCClient.initialize(credential);

let bucketName = '';

uploadFile();
function uploadFile() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);

  bucket.upload('./test.txt')
    .then(res => console.log(res))
    .catch(err => console.log(err));
}

// downloadFile();
function downloadFile() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);
  const remoteFile = bucket.file('test.txt');
  const localFile = './test.txt';

  remoteFile.createReadStream()
    .on('error', err => {
    })
    .on('end', () => {
    })
    .pipe(fs.createWriteStream(localFile))
}

// getFileMetadata();
function getFileMetadata() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);
  const file = bucket.file('test.txt');
  file.getMetadata().then(res => {
    console.log(res);
  }).catch(err => {
    console.log(err);
  })
}

// updateFileMetadata();
function updateFileMetadata() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);
  const file = bucket.file('test.txt');

  const metadata = {
    contentLanguage: 'en-US',
    customMetadata: {
      test: 'test'
    }
  };

  file.setMetadata(metadata).then(res => {
    console.log(res);
  }).catch(err => {
    console.log(err);
  })
}

// getFileList();
function getFileList() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);

  bucket.getFiles({delimiter: '/'}).then(res => {
    console.log(res)
  }).catch(err => {
    console.log(err);
  })
}

// deleteFile();
function deleteFile() {
  const storage = new StorageManagement();
  const bucket = storage.bucket(bucketName);
  const file = bucket.file('test.txt');
  file.delete().then(res => {
  }).catch(err => {
  })
}
