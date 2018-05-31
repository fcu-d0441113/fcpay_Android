package com.example.root.fcpay.Order.PlentyOrder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;

public class OrderPlentyViewController extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Toolbar toolBar;
    private ProductModel productModel = OrderTableViewModel.productModel;
    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    private static Dialog dialog;
    private int index;
    private ListView plentyListView;
    private MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plenty_view_controller);
        initComponent();
        productModel.clearQuantity();
        setEventListener();
    }

    private void initComponent(){
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        plentyListView = (ListView)findViewById(R.id.plentyListView);
        myCustomAdapter = new MyCustomAdapter();
        plentyListView.setAdapter(myCustomAdapter);
    }

    private void setEventListener(){
        plentyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showDialog();
            }
        });
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull
                            MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.list:
                        startActivity(new Intent(OrderPlentyViewController.this, OrderRecordViewModel.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(OrderPlentyViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(0).setChecked(true);
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toDetail:
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
                else {
                    startActivity(new Intent(OrderPlentyViewController.this, OrderDetailUIViewModel.class));
                }
                return true;
            case android.R.id.home:
                startActivity(new Intent(OrderPlentyViewController.this,OrderTableViewModel.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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