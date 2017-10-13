package com.mtesitoo.helper;

import android.net.Uri;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by eduardodiaz on 24/09/2017.
 */

public final class UriAdapter extends TypeAdapter<Uri> {
    @Override
    public void write(JsonWriter out, Uri uri) throws IOException {
        out.value(uri.toString());
    }

    @Override
    public Uri read(JsonReader in) throws IOException {
        return Uri.parse(in.nextString());
    }
}
