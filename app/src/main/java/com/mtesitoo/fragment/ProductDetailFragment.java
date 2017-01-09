package com.mtesitoo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nan on 12/31/2015.
 */
public class ProductDetailFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Product mProduct;

    @Bind(R.id.product_image_slider)
    SliderLayout mImageSlider;
    @Bind(R.id.product_detail_info_border)
    RelativeLayout mInfoBorder;
    @Bind(R.id.product_detail_price_border)
    RelativeLayout mPriceBorder;
    @Bind(R.id.product_detail_date_border)
    RelativeLayout mDateBorder;

    @Bind(R.id.product_detail_name)
    TextView mProductName;
    @Bind(R.id.product_detail_description)
    TextView mProductDescription;
    @Bind(R.id.product_detail_location)
    TextView mProductLocation;
    @Bind(R.id.product_detail_category)
    TextView mProductCategory;

    @Bind(R.id.product_detail_unit)
    TextView mProductUnit;
    @Bind(R.id.product_detail_quantity)
    TextView mProductQuantity;
    @Bind(R.id.product_detail_price)
    TextView mProductPrice;

    @Bind(R.id.product_detail_posting_date)
    TextView mProductPostingDate;
//    @Bind(R.id.product_detail_expiration_date)
//    TextView mProductExpirationDate;

    ArrayList<Uri> auxImages;
    int productId;

    public static ProductDetailFragment newInstance(Context context, Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_product_key), product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_edit_product) {
            ProductDetailEditFragment f = ProductDetailEditFragment.newInstance(getActivity(), mProduct);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = this.getArguments();

        mProduct = args.getParcelable(getString(R.string.bundle_product_key));
        productId = mProduct.getId();
        mProductName.setText(mProduct.getName());
        mProductDescription.setText(mProduct.getDescription());
        mProductLocation.setText(mProduct.getLocation());
        mProductCategory.setText(mProduct.getCategoriesStringList(this.getContext()));
        mProductUnit.setText(mProduct.getSIUnit());
        mProductQuantity.setText(mProduct.getQuantity().toString());
        mProductPrice.setText(mProduct.getPricePerUnit());
       //mProductExpirationDate.setText(mProduct.getExpiration().toString());

        updateImageSlider();
        updateBorderPaddings();
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

        urls.add(mProduct.getmThumbnail().toString());

        for(Uri image : mProduct.getAuxImages()){
            urls.add(image.toString());
        }

        for (String url : urls) {
            if(!url.equals("") && !url.equals(" ")){
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView
                        .image(url)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mImageSlider.addSlider(sliderView);
            }else{
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView
                        .image("http://tesitoo.com/image/cache/no_image-100x100.png")
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                mImageSlider.addSlider(sliderView);
            }
        }

        mImageSlider.setDuration(8000);

        if(urls.size() <= 1){
            mImageSlider.stopAutoCycle();
            mImageSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }

        mImageSlider.addOnPageChangeListener(this);
    }
}