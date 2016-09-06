package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtesitoo.R;
import com.mtesitoo.adapter.OrderListAdapter;
import com.mtesitoo.adapter.OrderProductListAdapter;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IOrderRequest;
import com.mtesitoo.helper.FormatHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 29-04-2016.
 */
public class OrderDetailFragment extends Fragment{

 private Order mOrder;

    @Bind(R.id.order_detail_id)
    TextView mOrderId;
    @Bind(R.id.order_detail_status)
    TextView mOrderStatus;
    @Bind(R.id.order_detail_total_price)
    TextView mTotal;
    @Bind(R.id.order_detail_item_count_title)
    TextView mOrderItemCountTitle;
    @Bind(R.id.order_detail_item_count)
    TextView mOrderItemCount;
    @Bind(R.id.order_detail_date_ordered)
    TextView mOrderPlacedDate;

    @Bind(R.id.order_detail_customer_id)
    TextView mCustomerId;
    @Bind(R.id.order_detail_customer_name)
    TextView mCustomerName;
    @Bind(R.id.order_detail_customer_telephone)
    TextView mCustomerTelephone;
    @Bind(R.id.order_detail_customer_email)
    TextView mCustomerEmail;
    @Bind(R.id.order_detail_customer_address)
    TextView mCustomerAddress;

    @Bind (R.id.listView_products)
    ListView mListViewProducts;

    public static OrderDetailFragment newInstance(Context context, Order order) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_product_key), order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = this.getArguments();

        mOrder = args.getParcelable(getString(R.string.bundle_product_key));

        // Get additional information for the order
        getOrderDetails();
    }

    private void getOrderDetails()
    {
        //TODO naily TEMPORARY METHOD TO GET STUCK IN
        // TODO naily Move it so that it gets called before the fragment is created.
        // otherwise, teh screen flickers when being drawn again here
        IOrderRequest orderService = new OrderRequest(getActivity());

        orderService.getDetailedOrders(mOrder, new ICallback() {
            @Override
            public void onResult(Object object) {
                Log.d("TEST getOrderDetails()", mOrder.toString());
                updateView();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void updateView()
    {
        ArrayList<OrderProduct> products = mOrder.getProducts();

        int itemCount = products.size();

        String itemCountTitle = (itemCount == 1)?
                getString(R.string.order_item_count_singular) :
                getString(R.string.order_item_count_plural);


        mOrderId.setText(Integer.toString(mOrder.getmId()));
        mOrderStatus.setText(mOrder.getmOrderStatus());
        mTotal.setText(FormatHelper.formatPrice(getString(R.string.currency_symbol), mOrder.getmTotalPrice()));
        mOrderItemCountTitle.setText(itemCountTitle);
        mOrderItemCount.setText(Integer.toString(itemCount));
        mOrderPlacedDate.setText(FormatHelper.formatDate(mOrder.getmDateOrderPlaced()));

        mCustomerId.setText(Integer.toString(mOrder.getCustomerId()));
        mCustomerName.setText(mOrder.getCustomerName());
        mCustomerTelephone.setText(mOrder.getCustomerTelephone());
        mCustomerEmail.setText(mOrder.getEmailAddress());
        mCustomerAddress.setText(mOrder.getDeliveryAddress());

        mListViewProducts.setAdapter(new OrderProductListAdapter(getActivity(), products));
    }
}
