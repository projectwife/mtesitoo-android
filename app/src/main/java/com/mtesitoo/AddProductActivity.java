package com.mtesitoo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mtesitoo.adapter.AddProductPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mtesitoo.R.id.dots;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(dots)
    TabLayout pagerIndicator;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
