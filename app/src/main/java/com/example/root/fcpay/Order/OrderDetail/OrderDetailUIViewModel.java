package com.example.root.fcpay.Order.OrderDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.root.fcpay.CoreData.OrderDetailCheckItem;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.R;

import java.util.ArrayList;

public class OrderDetailUIViewModel extends AppCompatActivity {


    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    public static ArrayList<OrderDetailCheckItem> orderDetailCheckItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_uiview_model);

        buildData();
        finish();   //完成，將空白頁面刪除
        startActivity(new Intent(OrderDetailUIViewModel.this, OrderDetailUIViewController.class));
    }

    private void buildData() {
        orderDetailCheckItems.clear();
        orderDetailCheckItems.add(new OrderDetailCheckItem("餐點","",1));
        for (int i = 0; i < orderDetailModel.getOrderDetailSize(); i++) {
            orderDetailCheckItems.add(new OrderDetailCheckItem(orderDetailModel.getOrderDetailmanufacturerName(i)+"\n"+
                    orderDetailModel.getOrderDetailProductName(i)+"  NT$"+orderDetailModel.getOrderDetailProductPrice(i),
                    orderDetailModel.getOrderDetailProductQuantity(i),2));
        }
        orderDetailCheckItems.add(new OrderDetailCheckItem("總額","",1));
        orderDetailCheckItems.add(new OrderDetailCheckItem("總便當數量",Integer.toString(orderDetailModel.getTotalOrderQuantity()),2));
        orderDetailCheckItems.add(new OrderDetailCheckItem("總便當金額",Integer.toString(orderDetailModel.getTotalOrderPrice()),2));
        orderDetailCheckItems.add(new OrderDetailCheckItem("取餐資訊","",1));
        orderDetailCheckItems.add(new OrderDetailCheckItem("取餐時間","中午",2));
        orderDetailCheckItems.add(new OrderDetailCheckItem("取餐地點","資電館",2));
    }




}
