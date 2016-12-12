package com.mtesitoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.fragment.EditOrderFragment;
import com.mtesitoo.helper.FormatHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naily on 14/08/16
 */
public class OrderProductListAdapter extends ArrayAdapter<OrderProduct>
{
    private Context mContext;
    private Order mOrder;
    private static ArrayList<OrderProduct> mOrderProducts;

    public OrderProductListAdapter(Context context, Order order, ArrayList<OrderProduct> orderProducts)
    {
        super(context, 0, orderProducts);
        mContext = context;
        mOrder = order;
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
        holder.order = mOrder;
        holder.orderProduct = orderProduct;

        double totalPrice = orderProduct.getUnitPrice() * orderProduct.getQuantity();

        holder.orderProductId.setText(Integer.toString(orderProduct.getId()));
        holder.productName.setText(orderProduct.getName());
        holder.totalPrice.setText(FormatHelper.formatPrice(mContext.getString(R.string.currency_symbol), totalPrice));
        holder.model.setText(orderProduct.getModel());
        holder.quantity.setText(Integer.toString(orderProduct.getQuantity()));
        holder.itemPrice.setText(FormatHelper.formatPrice(mContext.getString(R.string.currency_symbol), orderProduct.getUnitPrice()));

        // The app shouldn't display the status of individual products. However, this field is
        // important when debugging. Make to hide it for the final version.
        holder.productStatus.setText(orderProduct.getOrderStatus().getStatus(mContext));
        // todo naily UNCOMMENT TO HIDE THE PRODUCT STATUS FIELD FOR FINAL VERSION
        holder.productStatusTitle.setVisibility(View.GONE);
        holder.productStatus.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder {
        Order order;
        OrderProduct orderProduct;
        Context context;

        @Bind(R.id.order_product_id)            TextView orderProductId;
        @Bind(R.id.order_product_name)          TextView productName;
        @Bind(R.id.order_product_total_price)   TextView totalPrice;
        @Bind(R.id.order_product_model)         TextView model;
        @Bind(R.id.order_product_quantity)      TextView quantity;
        @Bind(R.id.order_product_item_price)    TextView itemPrice;
        @Bind(R.id.order_product_status_title)  TextView productStatusTitle;
        @Bind(R.id.order_product_status)        TextView productStatus;

        @OnClick(R.id.order_product_edit_status)
        public void onClick(View view) {
            Fragment f = EditOrderFragment.newInstance(context, orderProduct, order);
            FragmentActivity fragmentActivity = (FragmentActivity)context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).addToBackStack(null).commit();
        }


            public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
