package com.example.root.fcpay.Order.PlentyOrder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderDetail;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.Order.OrderDetail.OrderDetailUIViewModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

public class OrderPlentyViewController extends AppCompatActivity {

    ProductModel productModel = OrderTableViewModel.productModel;
    public OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    static Dialog dialog;
    int index;
    ListView plentyListView;
    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plenty_view_controller);
        plentyListView = (ListView)findViewById(R.id.plentyListView);
        productModel.clearQuantity();
        myCustomAdapter = new MyCustomAdapter();
        plentyListView.setAdapter(myCustomAdapter);
        plentyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showDialog();
            }
        });

    }
    public void showDialog(){

        final Dialog dialog = new Dialog(OrderPlentyViewController.this);
        dialog.setTitle("選擇數量");
        dialog.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) dialog.findViewById(R.id.set);
        Button b2 = (Button) dialog.findViewById(R.id.cancel);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                productModel.setQuantity(index, String.valueOf(np.getValue()));
                myCustomAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // dismiss the dialog
            }
        });
        dialog.show();
    }


    private class MyCustomAdapter extends BaseAdapter {
        private int mCurrentItem=0;
        private boolean isClick=false;

        @Override
        public int getCount() {
            return productModel.size();
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
            view = getLayoutInflater().inflate(R.layout.layout_order_plenty_body, null);
            TextView orderPlentyItemName = (TextView)view.findViewById(R.id.orderPlentyItemName);
            TextView orderPlentyItemValue = (TextView)view.findViewById(R.id.orderPlentyItemValue);
            orderPlentyItemName.setText(String.format("%s\n%6s NT$%s",
                    productModel.getManufacturerName(i),
                    productModel.getName(i),
                    productModel.getPrice(i)));
            orderPlentyItemValue.setText(productModel.getQuantity(i));
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_plenty, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        orderDetailModel.clear();
        boolean isNull = true;
        for(int i = 0;i<productModel.size();i++) {
            if(Integer.valueOf(productModel.getQuantity(i)) != 0) {
                orderDetailModel.addOrderDetail(new OrderDetail(
                        productModel.getID(i),
                        productModel.getName(i),
                        productModel.getPrice(i),
                        productModel.getManufacturerName(i),
                        productModel.getIntroduce(i),
                        productModel.getQuantity(i)));
                isNull = false;
            }
        }
        if(isNull == true){
            showErrorDialog();
        }
        else{
            startActivity(new Intent(OrderPlentyViewController.this, OrderDetailUIViewModel.class));
        }
        return true;
    }

    private void showErrorDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("訂餐錯誤")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("你沒有選擇任何餐點，請點選想要的餐點設定訂購數").create();
        dialog.show();
    }

}