/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import UIKit
import AGConnectAuth
import AGConnectStorage

class ViewController: UIViewController {
    var storage : AGCStorage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        storage = AGCStorage.getInstanceForBucketName("your_bucket_name")
    }
    
    @IBAction func login(_ sender: Any) {
        AGCAuth.instance().signInAnonymously().onSuccess { (result) in
            print("login success")
        }.onFailure { (error) in
            print("login failed ",error)
        }
    }

    @IBAction func logout(_ sender: Any) {
        AGCAuth.instance().signOut()
    }
    
    @IBAction func uploadFile(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let dirPath = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true).first;
        let filePath = dirPath!.appending("/test.jpg")
        let task = storageReference?.uploadFile(URL.init(fileURLWithPath: filePath))
        task?.onSuccess(callback: { (result) in
            print("upload success ",result ?? "")
        }).onComplete(callback: { (task) in
            print("upload complete ",task.result ?? "")
        }).onProgress(callback: { (result) in
            print("upload progress \(result?.bytesTransferred ?? 0), \(result?.totalByteCount ?? 0)")
        }).onPaused(callback: { (result) in
            print("upload paused")
        }).onCancel {
            print("upload canceled")
        }.onFailure(callback: { (error) in
            print("upload failed ",error)
        })
    }
    
    @IBAction func downloadFile(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let dirPath = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true).first;
        let filePath = dirPath!.appending("/test.jpg")
        let task = storageReference?.download(toFile: URL.init(fileURLWithPath: filePath))
        task?.onSuccess(callback: { (result) in
            print("download success ",result ?? "")
        }).onComplete(callback: { (task) in
            print("download complete ",task.result ?? "")
        }).onProgress(callback: { (result) in
            print("download progress \(result?.bytesTransferred ?? 0), \(result?.totalByteCount ?? 0)")
        }).onPaused(callback: { (result) in
            print("download paused")
        }).onCancel {
            print("download canceled")
        }.onFailure(callback: { (error) in
            print("download failed ",error)
        })
    }
    
    @IBAction func getFileMetadata(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let task = storageReference?.getMetadata()
        task?.onSuccess(callback: { (result) in
            print("getFileMetadata success ",result ?? "")
        }).onFailure(callback: { (error) in
            print("getFileMetadata failed ",error)
        })
    }
    
    @IBAction func updateFileMetadata(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let metadata = AGCStorageMetadata.init()
        metadata.contentType = "image/jpg"
        metadata.cacheControl = "no-cache"
        metadata.contentEncoding = "identity"
        metadata.contentDisposition = "inline"
        metadata.contentLanguage = "en"
        let task = storageReference?.update(metadata)
        task?.onSuccess(callback: { (result) in
            print("updateFileMetadata success ",result ?? "")
        }).onFailure(callback: { (error) in
            print("updateFileMetadata failed ",error)
        })
    }
    
    @IBAction func getFileList(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let task = storageReference?.list(100)
        task?.onSuccess(callback: { (result) in
            print("getFileList success ",result ?? "")
        }).onFailure(callback: { (error) in
            print("getFileList failed ",error)
        })
    }
    
    @IBAction func deleteFile(_ sender: Any) {
        let storageReference = storage?.reference(withPath: "test.jpg")
        let task = storageReference?.deleteFile()
        task?.onSuccess(callback: { (result) in
            print("deleteFile success ",result ?? "")
        }).onFailure(callback: { (error) in
            print("deleteFile failed ",error)
        })
    }
}

