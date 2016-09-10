package com.mtesitoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtesitoo.OrderActivity;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IOrderRequest;
import com.mtesitoo.helper.FormatHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Used to display info about orders.
 */
public class OrderListAdapter extends ArrayAdapter<Order> {
    private Context mContext;
    private static ArrayList<Order> mOrders;

    public OrderListAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        mContext = context;
        mOrders = orders;
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

        holder.orderId.setText(Integer.toString(order.getId()));
        holder.name.setText(order.getCustomerName());
        holder.totalPrice.setText(FormatHelper.formatPrice(mContext.getString(R.string.currency_symbol), order.getTotalPrice()));
        holder.orderStatus.setText(order.getOrderStatus().getStatus());
        holder.dateOrdered.setText(FormatHelper.formatDate(order.getDateOrderPlaced()));

        return convertView;
    }

    static class ViewHolder {
        Order order;
        Context context;

        @Bind(R.id.order_id)          TextView orderId;
        @Bind(R.id.order_customer_name)     TextView name;
        @Bind(R.id.order_total_price)      TextView totalPrice;
        @Bind(R.id.order_status)  TextView orderStatus;
        @Bind(R.id.order_date_ordered)      TextView dateOrdered;

        @OnClick(R.id.order_details_link)
        public void onClick(View view) {
            displayOrderDetails();
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        private void displayOrderDetails()
        {
            IOrderRequest orderService = new OrderRequest(context);

            orderService.getDetailedOrders(order, new ICallback() {
                @Override
                public void onResult(Object object) {
                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra(context.getString(R.string.bundle_product_key), order);
                    context.startActivity(intent);
                }

                @Override
                public void onError(Exception e) {}
            });
        }
    }
}