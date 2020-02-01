package com.aayushman.garbager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aayushman.garbager.R;
import com.aayushman.garbager.adapters.TransactionsAdapter;
import com.aayushman.garbager.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aayushman.garbager.util.Constants.user;

public class WalletActivity extends AppCompatActivity {
    TextView tFunds;
    ArrayList<Transaction> transactions;
    RecyclerView listView;
    TransactionsAdapter adapter;
    ProgressDialog pd;
    Double bal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        pd = new ProgressDialog(WalletActivity.this);
        pd.show();
        transactions = new ArrayList<>();
        tFunds = findViewById(R.id.tv_funds);
        listView = findViewById(R.id.transactions_list);
        adapter = new TransactionsAdapter(WalletActivity.this, transactions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        bal = 0.0;
        tFunds.setText("Rs. "+bal);
        getFunds();
    }

    private void getFunds() {
        transactions.clear();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.child("transactions").getChildren()){
                    if(d.child("name").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){
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
                            bal+=Double.parseDouble(d.child("amount").getValue().toString());
                            tFunds.setText("Rs. "+bal);
                            transactions.add(transaction);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
