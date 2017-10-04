package com.mtesitoo.backend.model;

/**
 * Created by eduardodiaz on 02/10/2017.
 */

public class ProfilePicture {

    private String filename;
    private String thumbnailPath;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
