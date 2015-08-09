package com.mtesitoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtesitoo.ProductDetailActivity;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jackwu on 2015-07-11.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private static ArrayList<Product> mProducts;

    public ProductListAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
        mContext = context;
        mProducts = products;
    }

    public void refresh(ArrayList<Product> products) {
        mProducts.clear();
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, parent, false);

        ViewHolder holder = new ViewHolder(convertView);
        holder.context = mContext;
        holder.product = product;

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPricePerUnit());

        return convertView;
    }

    static class ViewHolder {

        Product product;
        Context context;

        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_price)
        TextView productPrice;

        @OnClick(R.id.product_detail)
        public void onClick(View view) {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(context.getString(R.string.bundle_product_key), product);
            context.startActivity(intent);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}