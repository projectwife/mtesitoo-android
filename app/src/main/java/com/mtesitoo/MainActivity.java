package com.mtesitoo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mtesitoo.adapter.ItemListAdapter;
import com.mtesitoo.model.Item;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    private ItemListAdapter mItemListAdapter;

    @Bind(R.id.item_list)
    ListView mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<Item> items = new ArrayList<>();

        Item item1 = new Item("Item 1", 100.00, 10, "Car");
        Item item2 = new Item("Item 2", 200.00, 20, "Bus");
        Item item3 = new Item("Item 3", 300.00, 30, "Bike");

        items.add(item1);
        items.add(item2);
        items.add(item3);

        mItemListAdapter = new ItemListAdapter(this, items);
        mItemList.setAdapter(mItemListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_item) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
