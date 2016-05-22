package com.mtesitoo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 29-04-2016.
 */
public class OrderDetailFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

 private Order mOrder;


    @Bind(R.id.product_image_slider)
    SliderLayout mImageSlider;
    @Bind(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @Bind(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
    @Bind(R.id.order_detail_date_border)
    RelativeLayout mDateBorder;

    @Bind(R.id.customer_name)
    TextView mCustomerName;
    @Bind(R.id.product_name)
    TextView mProductName;
    @Bind(R.id.product_quantity)
    TextView mProductQuantity;
    @Bind(R.id.product_price)
    TextView mProductPrice;

    @Bind(R.id.order_total_price)
    TextView mTotal;
    @Bind(R.id.order_delivery_address)
    TextView mDeliveryAddress;
    @Bind(R.id.order_status)
    TextView mOrderStatus;

    @Bind(R.id.order_placed_date)
    TextView mOrderPlacedDate;
    @Bind(R.id.order_payment_method)
    TextView mPaymentMethod;

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
        mProductName.setText(mOrder.getmProductName());
        mCustomerName.setText(mOrder.getmCustomerName());
        mTotal.setText(mOrder.getmTotalPrice());
        mDeliveryAddress.setText(mOrder.getmDeliveryAddress());
        mOrderStatus.setText(mOrder.getmOrderStatus());
        mProductQuantity.setText(mOrder.getmProductQuantity().toString());
        mProductPrice.setText(mOrder.getmProductPrice());
        mPaymentMethod.setText(mOrder.getmPaymentMethod());
        mOrderPlacedDate.setText(mOrder.getmDateOrderPlaced().toString());

        updateBorderPaddings();
        updateImageSlider();
    }

    @Override
    public void onStop() {
        mImageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void updateBorderPaddings() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = mImageSlider.getLayoutParams();
        params.height = (int) (metrics.widthPixels * 0.65);

        int padding = Integer.parseInt(getString(R.string.padding_20));
        mInfoBorder.setPadding(padding, padding, padding, padding / 2);
        mPriceBorder.setPadding(padding, padding / 2, padding, padding / 2);
        mDateBorder.setPadding(padding, padding / 2, padding, padding);
    }

    private void updateImageSlider() {
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        urls.add("http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        urls.add("http://cdn3.nflximg.net/images/3093/2043093.jpg");
        urls.add("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (String url : urls) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            mImageSlider.addSlider(sliderView);
        }

        mImageSlider.setDuration(8000);
        mImageSlider.addOnPageChangeListener(this);
    }

}
