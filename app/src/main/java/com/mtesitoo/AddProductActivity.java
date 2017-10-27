package com.mtesitoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mtesitoo.adapter.AddProductPagerAdapter;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.fragment.AddProductPreviewFragment;
import com.mtesitoo.helper.AddProductHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mtesitoo.R.id.dots;

public class AddProductActivity extends AppCompatActivity {

    private static final String PREVIEW_FRAGMENT_TAG = "PREVIEW_FRAGMENT";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(dots)
    TabLayout pagerIndicator;

    @BindView(R.id.controls_previous)
    TextView prevButton;

    @BindView(R.id.controls_forward)
    TextView nextButton;

    AddProductPagerAdapter pagerAdapter;

    boolean isPreviewShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        pagerAdapter = new AddProductPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerIndicator.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    prevButton.setVisibility(View.GONE);
                    return;
                }

                prevButton.setVisibility(View.VISIBLE);

                if (position == pagerAdapter.getCount() - 1) {
                    nextButton.setText(getString(R.string.action_preview));
                    nextButton.setCompoundDrawables(null, null, null, null);
                    nextButton.setPadding(0, 0,
                            getResources().getDimensionPixelOffset(R.dimen.new_product_page_bottom_bar_padding), 0);
                    return;
                }
                nextButton.setText(getString(R.string.action_next));

                nextButton.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_arrow_next),
                        null);

                nextButton.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            AddProductHelper.getInstance().clearFields();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddProductHelper.getInstance().clearFields();
    }

    @OnClick(R.id.controls_previous)
    void goBack() {
        if (isPreviewShown) {
            isPreviewShown = false;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_corner, R.anim.exit_right)
                    .remove(getSupportFragmentManager().findFragmentByTag(PREVIEW_FRAGMENT_TAG)).commit();
            nextButton.setText(getString(R.string.action_preview));

            pagerIndicator.setVisibility(View.VISIBLE);
            return;
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.controls_forward)
    void goForward() {
        if (viewPager.getCurrentItem() < pagerAdapter.getCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            return;
        }

        if (!isPreviewShown) {
            isPreviewShown = true;
            pagerIndicator.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_corner, R.anim.exit_right)
                    .add(R.id.add_product_preview_fragment, new AddProductPreviewFragment(), PREVIEW_FRAGMENT_TAG).commit();

            nextButton.setText(getString(R.string.action_submit));
            return;
        }

        submitNewProduct();
    }

    private void submitNewProduct() {
        Toast.makeText(this, "Submitting new product", Toast.LENGTH_SHORT).show();

        final Product product = AddProductHelper.getInstance().getProduct();

        if (!product.isCompleted()) {
            Toast.makeText(this, "Please provide missing product details", Toast.LENGTH_LONG).show();
            return;
        }

        IProductRequest productService = new ProductRequest(this);
        productService.submitProduct(product, new ICallback<String>() {
            @Override
            public void onResult(String result) {
                IProductRequest productService = new ProductRequest(getApplicationContext());
                productService.submitProductThumbnail(Integer.parseInt(result), product.getmThumbnail(), new ICallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Log.d("image thumb upload", "Success");
                        Toast.makeText(getApplicationContext(), "Product thumbnail uploaded.", Toast.LENGTH_LONG).show();
//                        finish(); // finish will be called in the onResult for submitProduct. Shouldn't be called multiple times.

                        Intent intent = new Intent("submit_product_thumbnail");
                        intent.putExtra("result", result);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("image thumb upload err", e.toString());
                        Toast.makeText(getApplicationContext(), "Error occurred while uploading Product thumbnail.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                Toast.makeText(getApplicationContext(), "New Product Added.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                Log.e("product add error", e.toString());
                finish();
            }
        });
    }
}
