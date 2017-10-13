package com.mtesitoo.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mtesitoo.R;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.service.OrderRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IOrderRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naily on 16/09/16.
 */
public class EditOrderFragment extends Fragment {

    //TODO NAILY RENAME TO EditOrderSingleProductFragment
    private Order order;
    private OrderProduct orderProduct;

    @BindView(R.id.rGroupEditStatusOptions)
    RadioGroup editStatusOptions;

    public static EditOrderFragment newInstance(Context context, OrderProduct orderProductToEdit, Order order) {
        EditOrderFragment fragment = new EditOrderFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.bundle_product_key), order);
        args.putParcelable(context.getString(R.string.bundle_order_product_key), orderProductToEdit);
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
        orderProduct = args.getParcelable(getString(R.string.bundle_order_product_key));

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

        //todo naily - P2 do nothing if the order status hasn't changed
        //todo naily Do we still need the order object here?

        IOrderRequest orderService = new OrderRequest(getContext());

        orderService.submitEditStatusSingleProduct(orderProduct, newOrderStatus, new ICallback<OrderProduct>() {
            @Override
            public void onResult(OrderProduct editedOrderProduct) {

                //TODO NAILY HACK TO MAKE SURE WE GET THE RIGHT STATUS BACK
                //OVERRIDE GETDETAILEDORDERS METHOD?
                order.setOrderStatus(editedOrderProduct.getOrderStatus());

                IOrderRequest orderService = new OrderRequest(getContext());

                orderService.getDetailedOrders(order, new ICallback<Order>() {
                    @Override
                    public void onResult(Order theOrder) {
                        Fragment f = OrderDetailFragment.newInstance(getContext(), theOrder);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                    }

                    @Override
                    public void onError(Exception e) {}
                });
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    @OnClick(R.id.btn_edit_order_cancel)
    public void onClickCancel(View view) {
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
