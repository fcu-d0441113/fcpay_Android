package com.example.root.fcpay.Order.OrderRecord;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderRecord;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class OrderRecordViewController extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Toolbar toolBar;
    private ListView orderRecordListView;
    private ArrayList<OrderRecord> orderRecords = OrderRecordViewModel.orderRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_view_controller);

        initComponent();
        setEventListener();
    }

    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        orderRecordListView = (ListView)findViewById(R.id.orderRecordListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        orderRecordListView.setAdapter(myCustomAdapter);
    }

    private void setEventListener(){
        orderRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQRCodeDialog(i);
                view.setSelected(true);
            }
        });
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull
                            MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderRecordViewController.this, OrderTableViewModel.class));
                        break;
                    case R.id.list:
                        break;
                    case R.id.profile:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderRecordViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(1).setChecked(true);
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderRecords.size();
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


            view = getLayoutInflater().inflate(R.layout.layout_order_record_body, null);
            TextView orderId = (TextView) view.findViewById(R.id.orderId);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView count = (TextView) view.findViewById(R.id.count);
            orderId.setText("共NT$ "+orderRecords.get(i).getTotalOrderPrice());
            date.setText(orderRecords.get(i).orderDate);
            count.setText("共有 "+orderRecords.get(i).getTotalOrderNumber()+"個便當");
            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            if(orderRecords.get(i).isPickup()) {
                icon.setImageResource(R.drawable.check);
            }
            else{
                //icon.setImageResource(R.drawable.ic_waiting_pickup);
            }
            notifyDataSetChanged();
            return view;
        }
    }

    public void showQRCodeDialog(int index){

        final Dialog dialog = new Dialog(OrderRecordViewController.this);
        dialog.setTitle("選擇數量");
        dialog.setContentView(R.layout.dialog_qrcode_dialog);
        ImageView ivCode = (ImageView)dialog.findViewById(R.id.ivCode);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(orderRecords.get(index).orderId, BarcodeFormat.QR_CODE,
                    500, 500);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        TextView orderId = (TextView)dialog.findViewById(R.id.dialogOrderId);
        TextView orderNumber = (TextView)dialog.findViewById(R.id.dialogTotalOrderNumber);
        TextView orderPrice = (TextView)dialog.findViewById(R.id.dialogTotalOrderPrice);

        //處理各項目的字串，合成一個String
        String detail = "";
        int voi = orderRecords.get(index).details.size();
        for(int i=0;i<orderRecords.get(index).details.size();i++){
            detail = detail + orderRecords.get(index).details.get(i).quantity + "個 ";
            detail = detail + orderRecords.get(index).details.get(i).manufacturer;
            detail = detail + " 的 " + orderRecords.get(index).details.get(i).product + "\n";
        }

        orderId.setText(orderRecords.get(index).orderId);
        orderNumber.setText(detail);  //"便當總數量: "+Integer.toString(orderRecords.get(index).getTotalOrderNumber())
        orderPrice.setText("共有 "+Integer.toString(orderRecords.get(index).getTotalOrderNumber())+" 個便當\n便當總價格: NT "+Integer.toString(orderRecords.get(index).getTotalOrderPrice())+"$");

        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_record, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();       //重新整理，重新請求一次
                startActivity(new Intent(OrderRecordViewController.this, OrderRecordViewModel.class ));
                return true;
            case android.R.id.home:
                finish();   //完成，結束頁面
                startActivity(new Intent(OrderRecordViewController.this,OrderTableViewModel.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
