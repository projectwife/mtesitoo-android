package com.mtesitoo.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.mtesitoo.OrderActivity;
import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IOrderRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naily on 16/09/16.
 */
public class EditOrderFragment extends Fragment {

    private Order order;

    @Bind(R.id.rGroupEditStatusOptions)
    RadioGroup editStatusOptions;

    @Bind(R.id.btn_edit_order_submit)
    Button submitBtn;
    @Bind(R.id.btn_edit_order_cancel)
    Button cancelBtn;

    public static EditOrderFragment newInstance(Context context, Order order) {
        EditOrderFragment fragment = new EditOrderFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_product_key), order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        order = args.getParcelable(getString(R.string.bundle_product_key));

        View view = inflater.inflate(R.layout.activity_edit_order_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateView();

        //TODO NAILY MOVE TO FUNCTION
        OrderStatus status = order.getOrderStatus();
        int option = R.id.rBtnEditStatusCancelOption;
        if (status == OrderStatus.PENDING) {
            editStatusOptions.check(R.id.rBtnEditStatusPendingOption);
        }
        else if (status == OrderStatus.SHIPPED) {
            editStatusOptions.check(R.id.rBtnEditStatusShippedOption);
        }
        else if (status == OrderStatus.COMPLETE) {
            editStatusOptions.check(R.id.rBtnEditStatusCompleteOption);
        }
        else if (status == OrderStatus.CANCELED) {
            editStatusOptions.check(R.id.rBtnEditStatusCancelOption);
        }
        else
        {
            Log.e("Edit Order Status", "Order Status " + status.name() + " isn't supported");
            editStatusOptions.clearCheck();
        }
    }

    @OnClick(R.id.btn_edit_order_submit)
    public void onClickSubmit(View view) {

        OrderStatus newOrderStatus = getSelectedStatus();

        IOrderRequest orderService = new OrderRequest(getContext());

        orderService.submitOrder(order, newOrderStatus, new ICallback() {
            @Override
            public void onResult(Object object) {
                Fragment f = OrderDetailFragment.newInstance(getContext(), order);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    @OnClick(R.id.btn_edit_order_cancel)
    public void onClickCancel(View view) {
        Log.d("TEMP", "CANCEL CLICKED");
        //Todo Naily - implement this as a back button
        Fragment f = OrderDetailFragment.newInstance(getContext(), order);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
    }

    private OrderStatus getSelectedStatus()
    {
        int optionSelected = editStatusOptions.getCheckedRadioButtonId();

        switch(optionSelected) {
            case R.id.rBtnEditStatusPendingOption:
                return OrderStatus.PENDING;
            case R.id.rBtnEditStatusShippedOption:
                return OrderStatus.SHIPPED;
            case R.id.rBtnEditStatusCompleteOption:
                return OrderStatus.COMPLETE;
            case R.id.rBtnEditStatusCancelOption:
                return OrderStatus.CANCELED;
            default:
                Log.e("EditOrderFragment", "Order status selected in the UI isn't supported");
                return OrderStatus.ALL;
        }
    }
}
