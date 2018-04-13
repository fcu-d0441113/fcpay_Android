package com.example.root.fcpay.Order.OrderRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.CoreData.OrderRecord;
import com.example.root.fcpay.CoreData.OrderRecordDetail;
import com.example.root.fcpay.Foundation.MyVolley.MyJsonArrayRequest;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderRecordViewModel extends AppCompatActivity {

    RequestQueue mQueue;
    public static ArrayList<OrderRecord> orderRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_view_model);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {

        String url = "http://fcorder.fcudata.science/orderRecord.php";

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("memberId", "T03291");
            parameters.put("offset", "0");
            parameters.put("limit", "20");
        }catch (Exception e){
            e.printStackTrace();
        }
        final MyJsonArrayRequest request = new MyJsonArrayRequest (Request.Method.POST, url, parameters,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                ArrayList<OrderRecordDetail> orderRecordDetails = new ArrayList<>();

                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONArray details = jsonObject.getJSONArray("details");

                                for (int j = 0; j < details.length(); j++) {
                                    JSONObject detail = details.getJSONObject(j);
                                    orderRecordDetails.add(new OrderRecordDetail(
                                            detail.getString("product"),
                                            detail.getString("price"),
                                            detail.getString("manufacturer"),
                                            detail.getString("introduction"),
                                            detail.getString("quantity")
                                    ));
                                }
                                orderRecords.add(new OrderRecord(
                                        jsonObject.getString("orderId"),
                                        jsonObject.getString("totalPrice"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("orderDate"),
                                        jsonObject.getString("pickup"),
                                        jsonObject.getString("paymentType"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("statusDescription"),
                                        orderRecordDetails
                                ));
                            }
                            startActivity(new Intent(OrderRecordViewModel.this, OrderRecordViewController.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

}
