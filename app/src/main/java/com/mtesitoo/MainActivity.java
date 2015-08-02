package com.mtesitoo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mtesitoo.adapter.ProductListAdapter;
import com.mtesitoo.model.Product;
import com.mtesitoo.service.ModuleService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    private ProductListAdapter mProductListAdapter;

    @Bind(R.id.product_list)
    ListView mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            new HttpTask().execute().get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }

        mProductListAdapter = new ProductListAdapter(this, new ArrayList<Product>());
        mProductList.setAdapter(mProductListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_item) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpTask extends AsyncTask<Void, Void, ArrayList<Product>> {
        protected ArrayList<Product> doInBackground(Void... params) {
            return ModuleService.getlatest();
        }

        protected void onPostExecute(ArrayList<Product> products) {
            mProductListAdapter.refresh(products);
        }
    }
}
