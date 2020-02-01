package com.aayushman.garbager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.aayushman.garbager.R;
import com.aayushman.garbager.adapters.LeadersAdapter;
import com.aayushman.garbager.adapters.TransactionsAdapter;
import com.aayushman.garbager.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeadersActivity extends AppCompatActivity {

    ArrayList<Pair<Integer, String>> leaders;
    RecyclerView listView;
    LeadersAdapter adapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        pd = new ProgressDialog(LeadersActivity.this);
        pd.show();
        leaders = new ArrayList<>();
        listView = findViewById(R.id.leaders_list);
        adapter = new LeadersAdapter(LeadersActivity.this, leaders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        getList();
    }

    private void getList() {
        leaders.clear();
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    pd.hide();
                    Integer i = new Integer(1);
                    leaders.add(Pair.create(Integer.parseInt((d.child("contributions").getValue().toString())), d.getKey()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
