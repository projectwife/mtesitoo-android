package com.mtesitoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtesitoo.ItemDetailActivity;
import com.mtesitoo.R;
import com.mtesitoo.model.Item;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jackwu on 2015-07-11.
 */

public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context mContext;

    public ItemListAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_item, parent, false);

        ViewHolder holder = new ViewHolder(convertView);
        holder.context = mContext;
        holder.item = item;

        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(mContext.getString(R.string.currency_symbol) + item.getPrice().toString());

        return convertView;
    }

    static class ViewHolder {

        Item item;
        Context context;

        @Bind(R.id.item_name)
        TextView itemName;
        @Bind(R.id.item_price)
        TextView itemPrice;

        @OnClick(R.id.item_detail)
        public void onClick(View view) {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra(context.getString(R.string.bundle_item_key), item);
            context.startActivity(intent);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}