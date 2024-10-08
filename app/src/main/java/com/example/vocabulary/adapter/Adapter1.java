package com.example.vocabulary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabulary.R;
import com.example.vocabulary.WordAdd;

import java.util.ArrayList;
import java.util.List;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.MyViewHolder> {
    private Context context;
    private List<String> mainArray;

    public Adapter1(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void setData(String title) {
            tvTitle.setText(WordAdd.firstUpperCase(title));
            itemView.setBackgroundResource(R.drawable.layout_bg1);
        }
    }

    public void  updateAdapter(List<String> newList) {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }
}
