package com.mtesitoo.helper;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nan on 1/1/2016.
 */
public class FileHelper {
    private static final HashMap<String, Boolean> fileTypes;

    static {
        fileTypes = new HashMap<>();
        fileTypes.put(".jpg", true);
    }

    public static void clean(Context context) {
        File[] files = context.getFilesDir().listFiles();

        if (files != null) {
            for (File file : files) {
                int i = file.getName().lastIndexOf('.');
                String type = file.getName().substring(i);

                if (fileTypes.get(type) != null && fileTypes.get(type) == true) {
                    file.delete();
                }
            }
        }
    }
}
