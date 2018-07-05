package com.example.root.fcpay.Payment.LinePay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.root.fcpay.CoreData.LinePayData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Payment.iSunny.SunnyBankPaymentVC;
import com.example.root.fcpay.R;

import org.apache.http.util.EncodingUtils;

public class LinePayPaymentVC extends AppCompatActivity {

    private WebView linePayWebView;
    private LinePayData linePayData = LinePayPaymentVM.getLinePayData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_pay_payment_vc);
        initComponent();
        String url = linePayData.getPaymentUrlWeb();
        linePayWebView.loadUrl(url);
    }

    private void initComponent() {
        linePayWebView = (WebView)findViewById(R.id.LinePayWebView);
        webviewsetting();
    }

    private void webviewsetting() {
        WebSettings webSettings = linePayWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        linePayWebView.setWebChromeClient(new WebChromeClient());
        linePayWebView.setWebViewClient(new WebViewClient(){

            @Override

            public void onPageFinished(WebView view, String url) {
                if(url.toString().startsWith("http://fcorder.fcudata.science/ModelManager/LinePay.php?transactionId="+linePayData.getTransectionId())){
                    showFinishedDialog();
                }
            }
        });
    }

    private void showFinishedDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("付款完成")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();   //結束，關閉頁面
                        startActivity(new Intent(LinePayPaymentVC.this, LinePayPaymentConfirm.class));
                    }
                }).setMessage("已附款成功! 交易金額: NT "+linePayData.getAmount()+"$\n將跳回菜單頁").create();
        dialog.show();
    }

    public LinePayData getLinePayData() {
        return linePayData;
    }
}
