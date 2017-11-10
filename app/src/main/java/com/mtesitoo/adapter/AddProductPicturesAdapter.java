package com.mtesitoo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mtesitoo.R;
import com.mtesitoo.helper.AddProductHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mtesitoo.helper.AddProductHelper.MAX_PICTURES;

/**
 * Created by eduardodiaz on 20/10/2017.
 */

public class AddProductPicturesAdapter extends RecyclerView.Adapter<AddProductPicturesAdapter.ViewHolder> {

    private List<Uri> pictures;
    private Callback callback;
    private Context context;

    public AddProductPicturesAdapter(Context context, Callback callback) {

        this.pictures = AddProductHelper.getInstance().getProductPictureListData();
        this.callback = callback;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_product_list_item, parent, false);

        return new ViewHolder(v, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (getItemCount() == 1 || (pictures.size() != MAX_PICTURES && position == pictures.size())) {
            holder.bind(context, Uri.EMPTY);
            return;
        }
        holder.bind(context, pictures.get(position));
    }

    @Override
    public int getItemCount() {
        return pictures.size() < MAX_PICTURES ? 1 + pictures.size() : pictures.size();
    }

    public void insertPicture(int position, Uri url) {
        if (pictures.size() == MAX_PICTURES - 1 && position == MAX_PICTURES - 1) {
            pictures.add(position, url);
            AddProductHelper.getInstance().setProductPictures(pictures);
            notifyDataSetChanged();
            return;
        }

        pictures.add(url);
        AddProductHelper.getInstance().setProductPictures(pictures);
        //notifyDataSetChanged();
        notifyItemInserted(position);
    }

    public void updatePicture(int position, Uri url) {
        pictures.set(position, url);
        AddProductHelper.getInstance().setProductPictures(pictures);
        //notifyDataSetChanged();
        notifyItemChanged(position);
    }

    public void removePicture(int position) {
        pictures.remove(position);
        AddProductHelper.getInstance().setProductPictures(pictures);
        //notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView productPicture;
        private Callback callback;

        ViewHolder(View itemView, Callback callback) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.callback = callback;
        }

        void bind(Context context, final Uri picture) {


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemClick(picture.toString(), getLayoutPosition());
                    }
                }
            });

            if (picture.toString().isEmpty()) {
                productPicture.setVisibility(View.INVISIBLE);
                return;
            }
            productPicture.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(picture)
                    .into(productPicture);
        }
    }

    public interface Callback {
        void onItemClick(String picture, int position);
    }
}
