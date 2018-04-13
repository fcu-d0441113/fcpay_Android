package com.example.root.fcpay.Order.OrderTable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.CoreData.Product;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderTableViewModel extends AppCompatActivity {

    public static ProductModel productModel = new ProductModel();
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table_view_model);
        mQueue = Volley.newRequestQueue(this);
        productModel.clear();
        jsonParse();
    }
    private void jsonParse() {

        String url = "http://fcorder.fcudata.science/todayOrders.php";

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                productModel.addProduct(new Product(
                                        jsonObject.getString("productID"),
                                        jsonObject.getString("product"),
                                        jsonObject.getString("productPrice"),
                                        jsonObject.getString("manufacturerID"),
                                        jsonObject.getString("manufacturer"),
                                        jsonObject.getString("introduction")));
                            }
                            startActivity(new Intent(OrderTableViewModel.this, OrderTableViewController.class));
                        } catch (JSONException e) {
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
