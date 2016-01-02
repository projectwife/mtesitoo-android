package com.mtesitoo.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;

import java.util.ArrayList;

import butterknife.ButterKnife;

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
        Order order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item, parent, false);
        }

        ViewHolder holder = new ViewHolder(convertView);
        holder.context = mContext;
        holder.order = order;
        return convertView;
    }

    static class ViewHolder {
        Order order;
        Context context;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}