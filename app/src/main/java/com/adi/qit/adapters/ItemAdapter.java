package com.adi.qit.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.qit.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    String items[] = {};

    public ItemAdapter(String[] items){
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d("aditya", "onBindViewHolder: " + items[position]);
        holder.itemImg.setBackgroundColor(Color.parseColor(items[position]));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img_item_viewholder);
        }
    }

}
