
package test.model;

import java.util.List;

/**
 * Function description.
 *
 * @author xxxxxxx
 * @since 2019-10-24
 */
public class PublishFileInfo {
    public String getFileDestUrl() {
        return fileDestUrl;
    }

    public void setFileDestUrl(String fileDestUrl) {
        this.fileDestUrl = fileDestUrl;
    }

    public String getImageResolution() {
        return imageResolution;
    }

    public void setImageResolution(String imageResolution) {
        this.imageResolution = imageResolution;
    }

    public String getImageResolutionSingature() {
        return imageResolutionSingature;
    }

    public void setImageResolutionSingature(String imageResolutionSingature) {
        this.imageResolutionSingature = imageResolutionSingature;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private String fileDestUrl;

    private String imageResolution;

    private String imageResolutionSingature;

    private int size;
}
