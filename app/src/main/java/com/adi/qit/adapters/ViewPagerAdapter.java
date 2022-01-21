package com.adi.qit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.qit.R;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.OfferHolder> {

    String images[] = {"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTiUDeMCRTOHqLX6AqoNiCkIROjSW-QJmDQ1g&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9D-v1_9MZudg6pqj3cQlkmRNijBllEZSs9Lb9MQ7e1kcLVBHUx8S4otR2ze4_gFloVvs&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSfnlhmFaiSb0RvD5OFSh55ISj9ZIsZ6MIaw&usqp=CAU"};

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewpager_holder,
                parent, false
        );
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolder holder, int position) {
        Picasso.get()
                .load(images[position])
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.offerImg);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class OfferHolder extends RecyclerView.ViewHolder{
        ImageView offerImg;
        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            offerImg = itemView.findViewById(R.id.offer_img_viewpager_holder);
        }
    }

}
