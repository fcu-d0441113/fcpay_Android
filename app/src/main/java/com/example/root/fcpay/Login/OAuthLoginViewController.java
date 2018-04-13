package com.example.root.fcpay.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

public class OAuthLoginViewController extends AppCompatActivity {

    private WebView _webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login_view_controller);

        findviews();
        webviewsetting();
        _webview.loadUrl("http://fcorder.fcudata.science/login.php");
    }

    private void findviews()
    {
        _webview = (WebView) findViewById (R.id.webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webviewsetting()
    {
        WebSettings webSettings = _webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        _webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HTMLOUT");
        _webview.setWebChromeClient(new WebChromeClient());
        _webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.toString().startsWith("http://fcorder.fcudata.science/login_OAuth.php")){
                    _webview.loadUrl("javascript:window.HTMLOUT.showHTML" +
                            "('登入成功 NID: '+document.getElementsByTagName('body')[0].innerHTML);");
                }
            }
        });
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {

            /*new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();*/
            Intent OrderTableViewModel = new Intent(OAuthLoginViewController.this, OrderTableViewModel.class);
            startActivity(OrderTableViewModel);
        }

    }
}
