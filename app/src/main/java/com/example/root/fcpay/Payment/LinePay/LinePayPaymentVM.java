package com.example.root.fcpay.Payment.LinePay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.CoreData.ISunnyData;
import com.example.root.fcpay.CoreData.LinePayData;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Payment.iSunny.SunnyBankPaymentVC;
import com.example.root.fcpay.Payment.iSunny.SunnyBankPaymentVM;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LinePayPaymentVM extends AppCompatActivity {

    private RequestQueue mQueue;
    private SharedPreferences userProfileManager;
    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    private int paymentType;
    private static LinePayData linePayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_pay_payment_vm);
        mQueue = Volley.newRequestQueue(this);
        userProfileManager = getSharedPreferences("userProfile",0);
        Intent intent = getIntent();
        paymentType = intent.getIntExtra("PAYMENTTYPE", -1);
        //jsonParse();
        test();     //暫時測試用
        jsonParse2();
    }

    private void test() {       //暫時測試用
        linePayData = new LinePayData(
                "100",
                "test total foods",
                "0005",        //手動改
                "TWD"
        );
    }

    //這一大串等php寫好直接改就可以了，預設傳回4個參數
    /*
    private void jsonParse() {

        String url = "http://fcorder.fcudata.science/order.php";
        //打包product，傳送parameters用
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
        //打包parameters
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("paymentType", String.valueOf(paymentType));
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
                            linePayData = new LinePayData(
                                    response.getString("amount"),
                                    response.getString("productName"),
                                    response.getString("orderId"),
                                    response.getString("currency")
                            );
                            finish();   //結束，關閉頁面
                            startActivity(new Intent(LinePayPaymentVM.this, LinePayPaymentVC.class));
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
    }*/

    private void jsonParse2() {
        String url = "https://sandbox-api-pay.line.me/v2/payments/request";    //linepay sandbox請求
        //打包parameters
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("amount", linePayData.getAmount());
            parameters.put("productName", linePayData.getProductName());
            parameters.put("orderId", linePayData.getOrderId());
            parameters.put("currency", linePayData.getCurrency());
            parameters.put("confirmUrl", "http://fcorder.fcudata.science/ModelManager/LinePay.php");
        }catch (Exception e){
            e.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("result", response.getString("returnCode"));
                            //Log.v("info", response.getString("info"));
                            String info = response.getString("info");
                            String paymentUrl = new JSONObject(info).getString("paymentUrl");
                            linePayData.setPaymentUrlWeb(new JSONObject(paymentUrl).getString("web"));
                            linePayData.setPaymentUrlLine(new JSONObject(paymentUrl).getString("app"));
                            linePayData.setTransactionId(new JSONObject(info).getString("transactionId"));
                            finish();   //結束，關閉頁面
                            startActivity(new Intent(LinePayPaymentVM.this, LinePayPaymentVC.class));
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
                headers.put("X-LINE-ChannelId", "1591021350");
                headers.put("X-LINE-ChannelSecret", "49d286199c615f1d311b56a2910357f5");
                headers.put("Content-Type", "application/json;charset=UTF-8");
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

    public static LinePayData getLinePayData() {
        return linePayData;
    }
}
