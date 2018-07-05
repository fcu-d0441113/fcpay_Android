package com.example.root.fcpay.Payment.Type;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.root.fcpay.Payment.LinePay.LinePayPaymentVM;
import com.example.root.fcpay.Payment.iSunny.SunnyBankPaymentVM;
import com.example.root.fcpay.R;

public class PaymentTypeController extends AppCompatActivity {

    private Toolbar toolBar;
    private Button iSunny;
    private Button linePay;
    private int paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type_controller);
        initComponent();
        iSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentType = 1;
                showDialog();
            }
        });
        linePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentType = 3;
                showDialog();
            }
        });
    }

    private void initComponent(){
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        iSunny = (Button) findViewById(R.id.isunny);
        linePay = (Button) findViewById(R.id.Linepay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payment_type, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //不顯示標題
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toPayment:
                return true;
            case android.R.id.home:
                finish();   //關閉頁面
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("訂餐確認")
                .setNegativeButton("否", null).setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("PAYMENTTYPE", paymentType);
                        if (paymentType == 1) {
                            intent.setClass(PaymentTypeController.this, SunnyBankPaymentVM.class);
                        } else if (paymentType == 2) {

                        } else if (paymentType == 3) {
                            intent.setClass(PaymentTypeController.this, LinePayPaymentVM.class);
                        } else {

                        }
                        startActivity(intent);
                        finish();
                    }

                }).setMessage("是否送出訂單？").create();
        dialog.show();
    }
}
