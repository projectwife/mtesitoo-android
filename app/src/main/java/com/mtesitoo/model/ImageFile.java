package com.mtesitoo.model;

import android.content.Context;

import com.mtesitoo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nan on 1/1/2016.
 */
public class ImageFile extends File {
    private static final String IMAGE_SUFFIX = ".jpg";

    public ImageFile(Context context) {
        super(context.getFilesDir(), buildFileName(context));
    }

    private static String buildFileName(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = context.getString(R.string.app_name) + "_" + timeStamp + IMAGE_SUFFIX;

        try {
            FileOutputStream fos = context.openFileOutput(imageFileName, Context.MODE_WORLD_WRITEABLE);
            fos.close();
        } catch (Exception e) {
        }

        return imageFileName;
    }
}