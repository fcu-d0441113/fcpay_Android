package com.example.root.fcpay.Order.OrderTable;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderDetail;
import com.example.root.fcpay.Login.OAuthLoginViewController;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.Order.OrderDetail.OrderDetailUIViewModel;
import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.Order.PlentyOrder.OrderPlentyViewController;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;

public class OrderTableViewController extends AppCompatActivity {

    private BottomNavigationView bnv;
    private ListView productListView;
    private Toolbar toolBar;
    private ProductModel productModel = OrderTableViewModel.productModel;
    public static OrderDetailModel orderDetailModel = new OrderDetailModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table_view_controller);
        initComponent();
        setEventListener();
    }
    private void initComponent(){
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        productListView = (ListView)findViewById(R.id.productListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        productListView.setAdapter(myCustomAdapter);
    }

    private void setEventListener(){
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == productModel.size()){
                    Intent OrderDetailUIViewModel = new Intent(OrderTableViewController.this, OrderPlentyViewController.class);
                    startActivity(OrderDetailUIViewModel);
                }
                else{
                    orderDetailModel.clear();
                    orderDetailModel.addOrderDetail(new OrderDetail(
                            productModel.getID(i),
                            productModel.getName(i),
                            productModel.getPrice(i),
                            productModel.getManufacturerName(i),
                            productModel.getIntroduce(i),
                            "1"
                    ));
                    Intent OrderDetailUIViewModel = new Intent(OrderTableViewController.this, OrderDetailUIViewModel.class);
                    startActivity(OrderDetailUIViewModel);
                }
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
                        break;
                    case R.id.list:
                        startActivity(new Intent(OrderTableViewController.this, OrderRecordViewModel.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(OrderTableViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(0).setChecked(true);
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productModel.size()+1;
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

            if(i != productModel.size()) {
                view = getLayoutInflater().inflate(R.layout.layout_order_table, null);
                TextView productName = (TextView) view.findViewById(R.id.productName);
                TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
                TextView ManufacturerName = (TextView) view.findViewById(R.id.manufacturerName);

                productName.setText(productModel.getName(i));
                productPrice.setText(productModel.getPrice(i));
                ManufacturerName.setText(productModel.getManufacturerName(i));
            }
            else{
                view = getLayoutInflater().inflate(R.layout.layout_order_table_foot, null);
                TextView foot = (TextView)view.findViewById(R.id.foot);
                foot.setText("訂多個便當");
            }
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_table, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();      //完成，關閉頁面
                startActivity(new Intent(OrderTableViewController.this, OrderTableViewModel.class ));
                return true;
            case android.R.id.home:
                startActivity(new Intent(OrderTableViewController.this,OAuthLoginViewController.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
