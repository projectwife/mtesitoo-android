package com.mtesitoo.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtesitoo.Constants;
import com.mtesitoo.R;
import com.mtesitoo.adapter.AddProductPreviewAdapter;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.helper.AddProductHelper;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mtesitoo.R.id.dots;

/**
 * Created by eduardodiaz on 18/10/2017.
 */

public class AddProductPreviewFragment extends Fragment {

    @BindView(R.id.tv_preview_product_name)
    TextView productNameTextView;
    @BindView(R.id.tv_preview_product_description)
    TextView productDescriptionTextView;
    @BindView(R.id.tv_preview_product_location)
    TextView productLocationTextView;
    @BindView(R.id.tv_preview_product_expiration)
    TextView productExpirationTextView;
    @BindView(R.id.tv_preview_product_category)
    TextView productCategoryTextView;
    @BindView(R.id.tv_preview_product_price)
    TextView productPriceTextView;
    @BindView(R.id.tv_preview_product_quantity)
    TextView productQuantityTextView;
    @BindView(R.id.tv_preview_product_units)
    TextView productUnitsTextView;

    @BindView(R.id.iv_preview_product_pictures)
    ViewPager productPicturesViewPager;

    @BindView(dots)
    TabLayout pagerIndicator;

    public AddProductPreviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product_preview, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateScreen();
    }

    private void populateScreen() {

        Map<String, String> detailsData = AddProductHelper.getInstance().getProductDetailsData();

        productNameTextView.setText(detailsData.get(Constants.PRODUCT_NAME_KEY));
        productDescriptionTextView.setText(detailsData.get(Constants.PRODUCT_DESCRIPTION_KEY));
        productLocationTextView.setText(detailsData.get(Constants.PRODUCT_LOCATION_KEY));
        productExpirationTextView.setText(detailsData.get(Constants.PRODUCT_EXPIRATION_KEY));

        Map<String, Integer> quantityData = AddProductHelper.getInstance().getProductQuantityData();

        ICategoryCache cache = new CategoryCache(getContext());
        List<Category> categories = cache.getCategories();
        for (Category c : categories) {
            if (c.getId() == quantityData.get(Constants.PRODUCT_CATEGORY_KEY)) {
                AddProductHelper.getInstance().setProductCategory(c.getId());
                productCategoryTextView.setText(c.getName());
                break;
            }
        }

        IUnitCache unitCache = new UnitCache(getContext());
        final List<Unit> units = unitCache.getWeightUnits();
        for (Unit u : units) {
            if (u.getId() == quantityData.get(Constants.PRODUCT_UNITS_KEY)) {
                productUnitsTextView.setText(u.getName());
                break;
            }
        }

        productPriceTextView.setText(String.valueOf(quantityData.get(Constants.PRODUCT_PRICE_KEY)));
        productQuantityTextView.setText(String.valueOf(quantityData.get(Constants.PRODUCT_QUANTITY_KEY)));


        productPicturesViewPager.setAdapter(new AddProductPreviewAdapter(getContext(),
                AddProductHelper.getInstance().getProductPictureListData()));

        productPicturesViewPager.setPageMargin(getContext().getResources()
                .getDimensionPixelOffset(R.dimen.new_product_page_preview_spacing));

        pagerIndicator.setupWithViewPager(productPicturesViewPager);
    }
}
