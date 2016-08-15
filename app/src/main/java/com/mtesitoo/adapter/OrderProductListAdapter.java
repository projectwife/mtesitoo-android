package com.mtesitoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtesitoo.R;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.helper.FormatHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by naily on 14/08/16
 */
public class OrderProductListAdapter extends ArrayAdapter<OrderProduct>
{
    private Context mContext;
    private static ArrayList<OrderProduct> mOrderProducts;

    public OrderProductListAdapter(Context context, ArrayList<OrderProduct> orderProducts)
    {
        super(context, 0, orderProducts);
        mContext = context;
        mOrderProducts = orderProducts;
    }

    public void refresh(ArrayList<OrderProduct> products)
    {
        mOrderProducts.clear();
        mOrderProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_product_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderProduct orderProduct = getItem(position);

        holder.context = mContext;
        holder.orderProduct = orderProduct;

        double totalPrice = orderProduct.getUnitPrice() * orderProduct.getQuantity();

        holder.orderProductId.setText(Integer.toString(orderProduct.getId()));
        holder.productName.setText(orderProduct.getName());
        holder.totalPrice.setText(FormatHelper.formatPrice(mContext.getString(R.string.currency_symbol), totalPrice));
        holder.model.setText(orderProduct.getModel());
        holder.quantity.setText(Integer.toString(orderProduct.getQuantity()));
        holder.itemPrice.setText(FormatHelper.formatPrice(mContext.getString(R.string.currency_symbol), orderProduct.getUnitPrice()));

        return convertView;
    }

    static class ViewHolder {
        OrderProduct orderProduct;
        Context context;

        @Bind(R.id.order_product_id)            TextView orderProductId;
        @Bind(R.id.order_product_name)          TextView productName;
        @Bind(R.id.order_product_total_price)   TextView totalPrice;
        @Bind(R.id.order_product_model)         TextView model;
        @Bind(R.id.order_product_quantity)      TextView quantity;
        @Bind(R.id.order_product_item_price)    TextView itemPrice;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
