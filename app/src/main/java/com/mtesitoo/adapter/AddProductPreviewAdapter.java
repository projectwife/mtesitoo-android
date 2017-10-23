package com.mtesitoo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mtesitoo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eduardodiaz on 23/10/2017.
 */

public class AddProductPreviewAdapter extends PagerAdapter {

    private List<Uri> images;
    private LayoutInflater inflater;
    private Context context;

    public AddProductPreviewAdapter(Context context, List<Uri> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.add_product_preview_item, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Picasso.with(context)
                .load(images.get(position))
                .into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
