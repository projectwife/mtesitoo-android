package com.mtesitoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtesitoo.OrderActivity;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nan on 1/1/2016.
 */
public class OrderListAdapter extends ArrayAdapter<Order> {
    private Context mContext;
    private float deviceWidth;
    private static ArrayList<Order> mOrders;

    public OrderListAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        mContext = context;
        mOrders = orders;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        deviceWidth = metrics.widthPixels;
    }

    public void refresh(ArrayList<Order> products) {
        mOrders.clear();
        mOrders.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = getItem(position);

        holder.context = mContext;
        holder.order = order;

        holder.name.setText("Name : " + order.getmCustomerName());
        //TODO Naily - What's the currency?
        holder.totalPrice.setText("Order Total : " + order.getmTotalPrice());
        holder.orderStatus.setText("Order Status : " + order.getmOrderStatus());
        holder.dateOrdered.setText("Ordered on : "+order.getmDateOrderPlaced());

        return convertView;
    }

    static class ViewHolder {
        Order order;
        Context context;

        @Bind(R.id.customer_Name)     TextView name;
        @Bind(R.id.order_total_price) TextView totalPrice;
        @Bind(R.id.order_status)      TextView orderStatus;
        @Bind(R.id.date_ordered)      TextView dateOrdered;

        @OnClick(R.id.product_detail)
        public void onClick(View view) {
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra(context.getString(R.string.bundle_product_key), order);
            context.startActivity(intent);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}