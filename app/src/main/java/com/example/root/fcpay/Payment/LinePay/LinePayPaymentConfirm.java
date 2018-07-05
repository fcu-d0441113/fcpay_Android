package com.example.root.fcpay.Payment.LinePay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.root.fcpay.CoreData.LinePayData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LinePayPaymentConfirm extends AppCompatActivity {

    private RequestQueue mQueue;
    private LinePayData linePayData = LinePayPaymentVM.getLinePayData();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_pay_payment_confirm);
        dialog = ProgressDialog.show(LinePayPaymentConfirm.this, "跳轉中", "請稍候...",true);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {
        String url = "https://sandbox-api-pay.line.me/v2/payments/"+linePayData.getTransectionId()+"/confirm";    //linepay sandbox confirm
        //打包parameters
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("amount", linePayData.getAmount());
            parameters.put("currency", linePayData.getCurrency());
        }catch (Exception e){
            e.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("result", response.getString("returnCode"));
                            Log.v("transactionId", linePayData.getTransectionId());
                            //Log.v("info", response.getString("info"));
                            dialog.dismiss();
                            finish();   //結束，關閉頁面
                            startActivity(new Intent(LinePayPaymentConfirm.this, OrderTableViewController.class));
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
}
