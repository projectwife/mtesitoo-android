package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtesitoo.R;
import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.ProductService;
import com.mtesitoo.backend.service.logic.IProductService;
import com.mtesitoo.backend.service.logic.IResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 12/30/2015.
 */
public class ProductFragment extends ListFragment {
    private ProductListAdapter mProductListAdapter;
    private Seller mSeller;

    public static ProductFragment newInstance(Context context, Seller seller) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_seller_key), seller);
        fragment.setArguments(args);
        return fragment;
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
        updateProductList();
    }

    public void updateProductList() {
        IProductService productService = new ProductService(getActivity());

        productService.getProducts(mSeller.getId(), new IResponse<List<Product>>() {
            @Override
            public void onResult(List<Product> result) {
                mProductListAdapter.refresh((ArrayList<Product>) result);
            }

            @Override
            public void onError(Exception e) {
            }
        });

        mProductListAdapter = new ProductListAdapter(getActivity(), new ArrayList<Product>());
        setListAdapter(mProductListAdapter);
    }
}