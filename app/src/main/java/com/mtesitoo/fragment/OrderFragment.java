package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;
import com.mtesitoo.adapter.OrderListAdapter;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.IOrderRequest;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 12/30/2015.
 */
public class OrderFragment extends ListFragment {
    private OrderListAdapter mOrderListAdapter;
    private Seller mSeller;

    public static OrderFragment newInstance(Context context, Seller seller) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_seller_key), seller);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        mSeller = args.getParcelable(getString(R.string.bundle_seller_key));

        updateOrderList();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOrderList();
    }

    private void updateOrderList() {
        IOrderRequest orderService = new OrderRequest(getActivity());

        orderService.getOrders(mSeller.getId(), new ICallback<List<Order>>() {
            @Override
            public void onResult(List<Order> result) {
                mOrderListAdapter.refresh((ArrayList<Order>) result);
            }

            @Override
            public void onError(Exception e) {
            }
        });

        mOrderListAdapter = new OrderListAdapter(getActivity(),new ArrayList<Order>());
        setListAdapter(mOrderListAdapter);
    }
}
