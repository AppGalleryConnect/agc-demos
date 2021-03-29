
package com.huawei.agconnect.callback;

import com.huawei.agconnect.server.edukit.common.model.ProgressCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件上传进度回调通知实现示例
 *
 * @since 2020-07-14
 */
public class ProgressCallbackImpl implements ProgressCallback {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProgressCallbackImpl.class);

    @Override
    public void onProgressChanged(double v) {
        System.out.printf("file upload progress %.2f %%", v * 100);
        LOGGER.info("File upload notify, current progress is {}", v);
    }
}
