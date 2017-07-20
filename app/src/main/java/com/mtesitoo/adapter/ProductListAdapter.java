package com.mtesitoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtesitoo.ProductActivity;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by jackwu on 2015-07-11.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private float deviceWidth;
    private static ArrayList<Product> mProducts;
    private String uri;


    public ProductListAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
        mContext = context;
        mProducts = products;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        deviceWidth = metrics.widthPixels;
    }

    public void refresh(ArrayList<Product> products) {
        mProducts.clear();
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item2, parent, false);
        }

        int padding = Integer.parseInt(mContext.getString(R.string.padding));
        if (position == 0) {
            convertView.setPadding(padding, padding, padding, padding / 2);
        } else {
            convertView.setPadding(padding, padding / 2, padding, padding / 2);
        }

        ViewHolder holder = new ViewHolder(convertView);
        holder.context = mContext;
        holder.product = product;

        ViewGroup.LayoutParams params = holder.itemLayout.getLayoutParams();
        params.width = (int) deviceWidth;

//        params = holder.layoutDivider.getLayoutParams();
//        params.width = (int) (0.85 * deviceWidth);

        params = holder.productThumbnail.getLayoutParams();
        params.height = (int) (0.25 * deviceWidth);
        params.width = (int) (0.25 * deviceWidth);

        holder.productName.setText(product.getName());

        //Disabled category display for seller app.
        //holder.productCategory.setText(product.getCategoriesStringList(mContext));

        holder.productPrice.setText(product.getPricePerUnit());

        //exp date
        String expDate = "";
        if (product.getExpiration() instanceof Date) {
            expDate = product.getExpirationFormattedForApp().toString();
        }
        if (product.isProductExpired()) {
            holder.productExpDate.setText(expDate);
            holder.productExpDate.setTextColor(Color.RED);
        } else {
            holder.productExpDate.setText(expDate);
        }

        //qty remaining
        int qtyRemaining = product.getQuantity();
        holder.productQtyRemaining.setText(String.valueOf(qtyRemaining));

        //product status
        int statusCode = product.getStatus();
        String status = "";
        if (statusCode == 0) {
            status = "Disabled";
        } else if (statusCode == 1) {
            status = "Enabled";
        } else if (statusCode == 5) {
            status = "Pending Approval";
        }

        holder.productStatus.setText(status);

        uri = product.getmThumbnail().toString();

        if(uri.contains(" ")){
            uri = uri.replace(" ","%20");
            Picasso.with(holder.context).load(uri).into(holder.productThumbnail);
        }
        else{
            Picasso.with(holder.context).load(uri).into(holder.productThumbnail);
        }

        return convertView;
    }

    static class ViewHolder {

        Product product;
        Context context;

        @BindView(R.id.product_list_item)
        LinearLayout itemLayout;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.product_thumbnail)
        ImageView productThumbnail;
        @BindView(R.id.product_price)
        TextView productPrice;
        @BindView(R.id.mvExpirationDate)
        TextView productExpDate;
        @BindView(R.id.mvProductQtyRemaining)
        TextView productQtyRemaining;
        @BindView(R.id.mvProductStatus)
        TextView productStatus;

        //TODO: Show category for buyers app
        //Disabled category display for seller app
//        @BindView(R.id.product_category)
//        TextView productCategory;

//        @BindView(R.id.product_layout_divider)
//        View layoutDivider;

        //@OnClick(R.id.product_see_details_link)
        @OnClick(R.id.gridViewProductListing)
        public void onClick(View view) {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra(context.getString(R.string.bundle_product_key), product);
            context.startActivity(intent);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}