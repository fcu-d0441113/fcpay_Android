package com.example.root.fcpay.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.root.fcpay.MyStaticData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

public class OAuthLoginViewController extends AppCompatActivity {

    private WebView _webview;
    private SharedPreferences userProfileManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login_view_controller);

        initComponent();
        webviewsetting();
        userProfileManager = getSharedPreferences("userProfile",0);
        _webview.loadUrl("http://fcorder.fcudata.science/login.php");
    }

    private void initComponent()
    {
        _webview = (WebView) findViewById (R.id.webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webviewsetting()
    {
        WebSettings webSettings = _webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        _webview.setWebChromeClient(new WebChromeClient());
        _webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url.toString().startsWith("https://testapi.kid7.club/fcuOAuth/Login.aspx")){

                    _webview.evaluateJavascript("javascript:document.getElementsByName('nid')[0].value='"+userProfileManager.getString("NID","").replace("\"","")+"';" +
                            "document.getElementsByName('password')[0].value='1234';", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
                else if(url.toString().startsWith("http://"+ MyStaticData.IP+"/login_OAuth.php")){

                    _webview.evaluateJavascript("javascript:document.getElementById('nid').innerHTML;", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //Log.v("test",value);
                            userProfileManager.edit().putString("NID",value).commit();
                        }
                    });
                    _webview.evaluateJavascript("javascript:document.getElementById('token').innerHTML;", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //Log.v("test",value);
                            userProfileManager.edit().putString("token",value).commit();
                        }
                    });
                    Toast toast = Toast.makeText(OAuthLoginViewController.this,
                            "歡迎使用 FC Order!! "+userProfileManager.getString("NID","").replace("\"","")+"!!", Toast.LENGTH_LONG);
                    toast.show();
                    startActivity(new Intent(OAuthLoginViewController.this, OrderTableViewModel.class));
                }
            }
        });
    }
}
