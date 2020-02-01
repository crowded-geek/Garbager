package com.aayushman.garbager.adapters;

import android.content.Context;
import android.util.Pair;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.MyViewHolder> {

    ArrayList<Pair<Integer, String>> leaders;
    Context context;

    public LeadersAdapter(Context context, ArrayList<Pair<Integer, String>> leaders) {

        this.context = context;
        this.leaders = leaders;
        Arrays.sort(leaders.toArray());
        Collections.reverse(leaders);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaders_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.pos.setText((leaders.get(i).first) + " | ");
        myViewHolder.name.setText(leaders.get(i).second);
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
        return leaders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, pos;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username_tv);
            pos = itemView.findViewById(R.id.pos_tv);
        }
    }

}
