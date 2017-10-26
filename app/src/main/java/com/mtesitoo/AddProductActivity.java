package com.mtesitoo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mtesitoo.adapter.AddProductPagerAdapter;
import com.mtesitoo.helper.AddProductHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mtesitoo.R.id.dots;

public class AddProductActivity extends AppCompatActivity {

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

                if (position == pagerAdapter.getCount() - 2) {
                    nextButton.setText(getString(R.string.action_preview));
                    return;
                }

                if (position == pagerAdapter.getCount() - 1) {
                    nextButton.setText(getString(R.string.action_submit));
                    return;
                }
                nextButton.setText(getString(R.string.action_next));
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
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.controls_forward)
    void goForward() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }
}
