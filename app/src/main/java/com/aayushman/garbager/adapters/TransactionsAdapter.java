package com.aayushman.garbager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aayushman.garbager.R;
import com.aayushman.garbager.model.Transaction;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyViewHolder> {

    ArrayList<Transaction> transactions;
    Context context;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.name.setText(transactions.get(i).getName());
        myViewHolder.amount.setText(Double.toString(transactions.get(i).getAmount()));
        if(!transactions.get(i).isReceived()){
            myViewHolder.fromTv.setText("To: ");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, amount, fromTv;

        MyViewHolder(View itemView) {
            super(itemView);
            fromTv = itemView.findViewById(R.id.from_tv);
            name = itemView.findViewById(R.id.username_tv);
            amount = itemView.findViewById(R.id.amount_tv);
        }
    }

}
