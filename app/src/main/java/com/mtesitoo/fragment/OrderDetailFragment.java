package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mtesitoo.R;
import com.mtesitoo.adapter.OrderProductListAdapter;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.helper.FormatHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 29-04-2016
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

    @Bind(R.id.order_detail_scrollView)
    ScrollView scrollView;

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
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        mOrder = args.getParcelable(getString(R.string.bundle_product_key));

        View view = inflater.inflate(R.layout.activity_order_fragment_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView()
    {
        ArrayList<OrderProduct> products = mOrder.getProducts();

        int itemCount = products.size();

        String itemCountTitle = (itemCount == 1)?
                getString(R.string.order_item_count_singular) :
                getString(R.string.order_item_count_plural);


        mOrderId.setText(Integer.toString(mOrder.getId()));
        mOrderStatus.setText(mOrder.getOrderStatus().getStatus(getContext()));
        mTotal.setText(FormatHelper.formatPrice(getString(R.string.currency_symbol), mOrder.getTotalPrice()));
        mOrderItemCountTitle.setText(itemCountTitle);
        mOrderItemCount.setText(Integer.toString(itemCount));
        mOrderPlacedDate.setText(FormatHelper.formatDate(mOrder.getDateOrderPlaced()));

        mCustomerId.setText(Integer.toString(mOrder.getCustomerId()));
        mCustomerName.setText(mOrder.getCustomerName());
        mCustomerTelephone.setText(mOrder.getCustomerTelephone());
        mCustomerEmail.setText(mOrder.getEmailAddress());
        mCustomerAddress.setText(mOrder.getDeliveryAddress());

        mListViewProducts.setAdapter(new OrderProductListAdapter(getActivity(), products));
        setListViewHeightBasedOnChildren(mListViewProducts);

        // Automatically scroll back up to the top of the page
        scrollView.smoothScrollTo(0,0);
    }

    /**
     * Dynamically set the height of myListView so that it expands to show all its children.
     * This is to avoid having a separate scrollbar for the main view and for the list view.
     */
    private static void setListViewHeightBasedOnChildren(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null)
            return;

        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        View listItem = null;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(myListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < myListAdapter.getCount(); i++) {
            listItem = myListAdapter.getView(i, listItem, myListView);

            if (i == 0)
                listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }
}
