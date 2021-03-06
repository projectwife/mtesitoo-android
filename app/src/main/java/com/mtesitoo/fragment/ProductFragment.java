package com.mtesitoo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mtesitoo.AddProductActivity;
import com.mtesitoo.R;
import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 12/30/2015
 */
public class ProductFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ProductListAdapter mProductListAdapter;
    private Integer sellerId;
    ;
    SwipeRefreshLayout mSwipeLayout;

    public static ProductFragment newInstance(Context context, Integer sellerId) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(context.getString(R.string.bundle_seller_key), sellerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_product:
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        sellerId = args.getInt(getString(R.string.bundle_seller_key));

        updateProductList();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = getListView();
        getListView().setDivider(null);

        mSwipeLayout = new SwipeRefreshLayout(getActivity());
        ((ViewGroup) listView.getParent()).addView(mSwipeLayout);
        ((ViewGroup) listView.getParent()).removeView(listView);
        mSwipeLayout.addView(listView);

        mSwipeLayout.setOnRefreshListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeLayout.setRefreshing(true);
        updateProductList();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("submit_product_thumbnail"));
        Log.d("LocalBroadcastManager", "RegisterReceiver");

        reloadProductList();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        Log.d("LocalBroadcastManager", "UnregisterReceiver");
        super.onPause();
    }

    private void updateProductList() {
        IProductRequest productService = new ProductRequest(getActivity());

        productService.getProducts(sellerId, new ICallback<List<Product>>() {
            @Override
            public void onResult(List<Product> result) {
                mProductListAdapter.refresh((ArrayList<Product>) result);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception e) {
                mSwipeLayout.setRefreshing(false);
            }
        });

        mProductListAdapter = new ProductListAdapter(getActivity(), new ArrayList<Product>());
        setListAdapter(mProductListAdapter);
    }

    @Override
    public void onRefresh() {
        updateProductList();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("result");
            Log.d("LocalBroadcastManager", "result : " + message);
            reloadProductList();
        }
    };

    private void reloadProductList() {
        mSwipeLayout.setRefreshing(true);
        updateProductList();
    }
}
