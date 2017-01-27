package com.mtesitoo.model;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nan on 1/1/2016.
 */
public class ImageFile extends File {

    public ImageFile(String path) throws IOException {
        super(path);
    }
    public ImageFile(Context context) throws IOException {
        super(createImageFile(context).getAbsolutePath());
    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "mtesitoo" + "_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public Uri getUri() {
        return Uri.parse(super.toURI().toString());
    }
}