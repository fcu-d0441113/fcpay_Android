package com.example.root.fcpay.Order.OrderRecord;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.root.fcpay.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class OrderRecordViewController extends AppCompatActivity {

    ListView orderRecordListView;
    public ArrayList<OrderRecord> orderRecords = OrderRecordViewModel.orderRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_view_controller);

        orderRecordListView = (ListView)findViewById(R.id.orderRecordListView);

        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        orderRecordListView.setAdapter(myCustomAdapter);
        orderRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQRCodeDialog(i);
                view.setSelected(true);
            }
        });
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
            orderId.setText(orderRecords.get(i).orderDate);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            if(orderRecords.get(i).isPickup()) {
                icon.setImageResource(R.drawable.ic_pickup);
            }
            else{
                icon.setImageResource(R.drawable.ic_waiting_pickup);
            }

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

        orderId.setText(orderRecords.get(index).orderId);
        orderNumber.setText("便當總數量: "+Integer.toString(orderRecords.get(index).getTotalOrderNumber()));
        orderPrice.setText("便當總價格: NT "+Integer.toString(orderRecords.get(index).getTotalOrderPrice())+"$");

        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_record, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(OrderRecordViewController.this, OrderRecordViewModel.class ));
        return true;
    }

}
