package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;
import com.mtesitoo.adapter.OrderListAdapter;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.service.logic.IOrderRequest;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 12/30/2015.
 *
 * Naily Makangu (05-Aug-2016)
 * This activity changes the title of the app to the type of
 * orders the user is looking at. E.g. Pending orders, complete orders.
 * The title is restored to the name of the app in onDestroy.
 *
 * At the moment, we are only supporting the following order status:
 * Pending, Shipped, Complete, Canceled
 *
 */

public class OrderFragment extends ListFragment {
    private OrderListAdapter mOrderListAdapter;
    private Seller mSeller;
    private OrderStatus orderStatus;

    public static OrderFragment newInstance(Context context, Seller seller) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_seller_key), seller);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_order, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_filterByPending:
                filterActionMenu(item, OrderStatus.PENDING);
                return true;

            case R.id.action_filterByShipped:
                filterActionMenu(item, OrderStatus.SHIPPED);
                return true;

            case R.id.action_filterByComplete:
                filterActionMenu(item, OrderStatus.COMPLETE);
                return true;

            case R.id.action_filterByCanceled:
                filterActionMenu(item, OrderStatus.CANCELED);
                return true;

            case R.id.action_filterByClear:
                filterActionMenu(item, OrderStatus.ALL);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void filterActionMenu(MenuItem item, OrderStatus status)
    {
        item.setChecked(true);
        orderStatus = status;
        updateOrderList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        mSeller = args.getParcelable(getString(R.string.bundle_seller_key));

        orderStatus = OrderStatus.PENDING;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //restore the title of the app to the default.
        getActivity().setTitle(R.string.app_name);
    }

    /*
        Update the list of orders. Also updates the application title as it is linked to
        the type of orders being displayed.
     */
    private void updateOrderList() {
        IOrderRequest orderService = new OrderRequest(getActivity());

        orderService.getOrders(mSeller.getId(), orderStatus, new ICallback<List<Order>>() {
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

        setAppTitle();
    }

    private void setAppTitle()
    {
        int appTitleResId;

        switch (orderStatus)
        {
            case PENDING:
                appTitleResId = R.string.menu_item_order_filterBy_Pending;
                break;
            case SHIPPED:
                appTitleResId = R.string.menu_item_order_filterBy_Shipped;
                break;
            case COMPLETE:
                appTitleResId = R.string.menu_item_order_filterBy_Complete;
                break;
            case CANCELED:
                appTitleResId = R.string.menu_item_order_filterBy_Canceled;
                break;
            case ALL:
                appTitleResId = R.string.menu_item_order_filterBy_Clear;
                break;
            default:
                Log.e("setAppTitle", "Status " + orderStatus.name() + "isn't supported");
                appTitleResId = R.string.app_name;
        }

        getActivity().setTitle(appTitleResId);
    }
}
