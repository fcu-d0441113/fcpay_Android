package com.example.root.fcpay.Payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.CoreData.ISunnyData;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SunnyBankPaymentVM extends AppCompatActivity {

    private RequestQueue mQueue;
    public static ISunnyData iSunnyData;
    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    private SharedPreferences userProfileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunny_bank_payment_vm);
        mQueue = Volley.newRequestQueue(this);
        userProfileManager = getSharedPreferences("userProfile",0);
        jsonParse();
    }
    private void jsonParse() {

        String url = "http://fcorder.fcudata.science/order.php";
        JSONArray products = new JSONArray();
        for(int i=0; i<orderDetailModel.getOrderDetailSize(); i++) {
            try {
                JSONObject product = new JSONObject();
                product.put("productID", orderDetailModel.getOrderDetailProductID(i));
                product.put("quantity", orderDetailModel.getOrderDetailProductQuantity(i));
                products.put(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("paymentType", "1");
            parameters.put("location", "0");
            parameters.put("memberId", userProfileManager.getString("NID","").replace("\"",""));
            parameters.put("product", products);
            parameters.put("memo", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            iSunnyData = new ISunnyData(
                                    response.getString("orderId"),
                                    response.getString("memberId"),
                                    response.getString("procCode"),
                                    response.getString("amount"),
                                    response.getString("initDateTime"),
                                    response.getString("MAC"),
                                    response.getString("replyURL"),
                                    response.getString("memo"),
                                    response.getString("noticeURL")
                            );
                            finish();   //結束，關閉頁面
                            startActivity(new Intent(SunnyBankPaymentVM.this, SunnyBankPaymentVC.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //處理錯誤，並關閉頁面
                showErrorDialog(Integer.toString(error.networkResponse.statusCode),new String(error.networkResponse.data));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("user_id", userProfileManager.getString("NID","").replace("\"",""));
                headers.put("user_auth", userProfileManager.getString("token","").replace("\"",""));
                return headers;
            }
        };
        mQueue.add(request);
    }

    //處理錯誤
    private void showErrorDialog(String statusCode,String errorMessage) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("錯誤")
                .setMessage(statusCode + " , " + errorMessage.substring(12, errorMessage.length()-2) + ".")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                }).create();
        dialog.show();
    }
}
