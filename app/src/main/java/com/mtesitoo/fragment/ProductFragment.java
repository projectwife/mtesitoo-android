package com.mtesitoo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.AddProductActivity;
import com.mtesitoo.R;
import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 12/30/2015
 */
public class ProductFragment extends ListFragment {
    private ProductListAdapter mProductListAdapter;
    private Seller mSeller;
    ProgressDialog pd;

    public static ProductFragment newInstance(Context context, Seller seller) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_seller_key), seller);
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

        switch(item.getItemId())
        {
            case R.id.action_add_product:
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        mSeller = args.getParcelable(getString(R.string.bundle_seller_key));

        updateProductList();

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
        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Fetching products");
        pd.show();
        updateProductList();
    }

    private void updateProductList() {
        IProductRequest productService = new ProductRequest(getActivity());

        productService.getProducts(mSeller.getId(), new ICallback<List<Product>>() {
            @Override
            public void onResult(List<Product> result) {
                mProductListAdapter.refresh((ArrayList<Product>) result);
                pd.dismiss();
            }

            @Override
            public void onError(Exception e) {
                pd.dismiss();
            }
        });

        mProductListAdapter = new ProductListAdapter(getActivity(), new ArrayList<Product>());
        setListAdapter(mProductListAdapter);
    }
}