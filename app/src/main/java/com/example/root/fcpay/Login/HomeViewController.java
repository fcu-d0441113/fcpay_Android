package com.example.root.fcpay.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.R;

public class HomeViewController extends AppCompatActivity {

    Button test_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view_controller);
        test_bt = (Button)findViewById(R.id.test);


        test_bt.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent OrderTableViewModels = new Intent(HomeViewController.this, OrderRecordViewModel.class);
                startActivity(OrderTableViewModels);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(HomeViewController.this, OAuthLoginViewController.class ));
        return true;
    }

}
