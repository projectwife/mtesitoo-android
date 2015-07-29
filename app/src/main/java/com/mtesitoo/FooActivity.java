package com.mtesitoo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FooActivity extends ActionBarActivity {

    @Bind(R.id.next_button)
    Button mNextButton;

    @Bind(R.id.prev_button)
    Button mPrevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        mNextButton.setText(R.string.submit);
    }
}
