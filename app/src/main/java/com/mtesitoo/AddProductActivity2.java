package com.mtesitoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class AddProductActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);

        AppCompatButton addButton = (AppCompatButton) findViewById(R.id.add_new_product_button);
        AppCompatButton cancelButton = (AppCompatButton) findViewById(R.id.cancel_new_product_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: add product
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Todo: ask user is sure they want to cancel if any fields contain data
                // Todo: improve by allowing user to save draft products for future adding?

                finish();
            }
        });

    }
}
