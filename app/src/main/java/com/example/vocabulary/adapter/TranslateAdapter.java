package com.example.vocabulary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabulary.R;
import com.example.vocabulary.WordAdd;
import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;
import java.util.List;

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.MyViewHolder> {
    private Context context;
    private List<ListItem2> mainArray;
    public static int pos;

    public TranslateAdapter(Context context) {
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
        if (position == 0) {
            holder.setData(mainArray.get(position).getValue() + ":", position, mainArray.get(0).getKey());
        } else {
            holder.setData(mainArray.get(position).getValue(), position, mainArray.get(0).getKey());
        }
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        int nowPosition = TranslateAdapter.pos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
        public void setData(String title, int nowPosition, String one) {
            if (title.equals(one + ":")) {
                tvTitle.setText(WordAdd.firstUpperCase(title));
                itemView.setBackgroundResource(R.drawable.layout_bg2);
            } else {
                tvTitle.setText(title);
                itemView.setBackgroundResource(R.drawable.layout_bg1);
            }
        }
    }

    public void updateAdapter(List<ListItem2> newList) {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int pos, MyDbManager dbManager) {
        dbManager.deleteEdgeFromTwoWords(mainArray.get(pos).getKey(), mainArray.get(pos).getValue());
        mainArray.remove(pos);
        notifyItemRangeChanged(0, mainArray.size());
        notifyItemRemoved(pos);
    }

    public ArrayList<String> getKeyAnaValue(int pos) {
        ArrayList<String> ans = new ArrayList<>();
        ans.add(mainArray.get(pos).getKey());
        ans.add(mainArray.get(pos).getValue());
        return ans;
    }
}
