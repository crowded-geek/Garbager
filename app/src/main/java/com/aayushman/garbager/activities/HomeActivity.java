package com.aayushman.garbager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aayushman.garbager.R;

public class HomeActivity extends AppCompatActivity {

    CardView btnBin, btnDonate, btnLeader, btnReport;
    ImageView btnWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        btnBin = findViewById(R.id.btn_bin);
        btnDonate = findViewById(R.id.btn_donate);
        btnWallet = findViewById(R.id.wallet_btn);
        btnLeader = findViewById(R.id.btn_leader);
        btnReport = findViewById(R.id.btn_report);
        btnLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, LeadersActivity.class);
                startActivity(i);
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(i);
            }
        });
        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, WalletActivity.class);
                startActivity(i);
            }
        });
        btnBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, BinActivity.class);
                startActivity(i);
            }
        });
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DonationActivity.class);
                startActivity(i);
            }
        });

    }
}
