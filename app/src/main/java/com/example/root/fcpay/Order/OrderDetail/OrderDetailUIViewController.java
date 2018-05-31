package com.example.root.fcpay.Order.OrderDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderDetailCheckItem;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.Payment.SunnyBankPaymentVM;
import com.example.root.fcpay.R;

import java.util.ArrayList;

public class OrderDetailUIViewController extends AppCompatActivity {

    private Toolbar toolBar;
    private  ArrayList<OrderDetailCheckItem> orderDetailCheckItems = OrderDetailUIViewModel.orderDetailCheckItems;
    private ListView orderDetailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_uiview_controller);

        initComponent();
    }

    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        orderDetailListView = (ListView)findViewById(R.id.orderDetailListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        orderDetailListView.setAdapter(myCustomAdapter);
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderDetailCheckItems.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(orderDetailCheckItems.get(i).layoutType == 1){
                view = getLayoutInflater().inflate(R.layout.layout_order_detail_head, null);
                TextView sectionTitle = (TextView) view.findViewById(R.id.sectionTitle);

                sectionTitle.setText(orderDetailCheckItems.get(i).itemName);
            }
            else if(orderDetailCheckItems.get(i).layoutType == 2){
                view = getLayoutInflater().inflate(R.layout.layout_order_detail_body, null);
                TextView orderDetailItemName = (TextView)view.findViewById(R.id.orderDetailItemName);
                TextView orderDetailItemValue = (TextView)view.findViewById(R.id.orderDetailItemValue);

                orderDetailItemName.setText(orderDetailCheckItems.get(i).itemName);
                orderDetailItemValue.setText(orderDetailCheckItems.get(i).itemValue);
            }

            return view;
        }
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("訂餐確認")
                .setNegativeButton("否", null).setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(OrderDetailUIViewController.this,SunnyBankPaymentVM.class));
                    }

                }).setMessage("是否送出訂單？").create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_detail_ui_view, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toPayment:
                showDialog();
                return true;
            case android.R.id.home:
                startActivity(new Intent(OrderDetailUIViewController.this,OrderTableViewModel.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
