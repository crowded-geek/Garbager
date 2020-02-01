package com.aayushman.garbager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aayushman.garbager.R;
import com.aayushman.garbager.adapters.TransactionsAdapter;
import com.aayushman.garbager.model.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonationActivity extends AppCompatActivity {
    FloatingActionButton donate;
    TextView tFunds;
    ArrayList<Transaction> transactions;
    RecyclerView listView;
    ProgressDialog pd;
    TransactionsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        transactions = new ArrayList<>();
        pd = new ProgressDialog(this);
        tFunds = findViewById(R.id.tv_funds);
        donate = findViewById(R.id.donate_fab);
        pd.show();
        listView = findViewById(R.id.transactions_list);
        adapter = new TransactionsAdapter(DonationActivity.this, transactions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DonationActivity.this);
                final EditText e = new EditText(DonationActivity.this);
                e.setHint("Amount");
                builder.setView(e);
                builder.setPositiveButton("Donate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Transaction s = new Transaction(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), Integer.parseInt(e.getText().toString()),true );
                        FirebaseDatabase.getInstance().getReference().child("transactions").push().setValue(s);
                        FirebaseDatabase.getInstance().getReference().child("totalfunds").setValue(Integer.parseInt(tFunds.getText().toString())+Integer.parseInt(e.getText().toString()));
                        tFunds.setText(Integer.parseInt(tFunds.getText().toString())+Integer.parseInt(e.getText().toString()));
                    }
                });
                builder.show();
            }
        });
        getTotalFunds();

    }

    private void getTotalFunds() {
        transactions.clear();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tFunds.setText("Rs. "+  dataSnapshot.child("totalfunds").getValue().toString());
                for(DataSnapshot d : dataSnapshot.child("transactions").getChildren()){
                    pd.hide();
                    if(Boolean.parseBoolean(d.child("received").getValue().toString())){
                    Transaction transaction = new Transaction(
                            d.child("name").getValue().toString(),
                            Double.parseDouble(d.child("amount").getValue().toString()),
                                    true);
                    transactions.add(transaction);
                    adapter.notifyDataSetChanged();
                    } else {
                        Transaction transaction = new Transaction(
                                d.child("name").getValue().toString(),
                                Double.parseDouble(d.child("amount").getValue().toString()),
                                false);
                        transactions.add(transaction);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
