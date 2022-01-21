package com.adi.qit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.qit.R;

public class HomeRAdapter extends RecyclerView.Adapter<HomeRAdapter.HomeRcHolder>{

    String titles[] = {"Best sellers", "Women", "Men"};
    Context context;
    String[][] items = {
            {
                "#FF3855",
                    "#FB4D46",
                    "#FA5B3D",
                    "#FD3A4A"
            },
            {
                "#9C2542",
                    "#BF4F51",
                    "#A57164"
            },
            {
                    "#AD4379",
                    "#B768A2",
                    "#8BA8B7"
            }
    };

    public HomeRAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public HomeRcHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_rv_holder, parent, false);
        return new HomeRcHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRcHolder holder, int position) {
        holder.titleTv.setText(titles[position]);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new ItemAdapter(items[position]));
        holder.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class HomeRcHolder extends RecyclerView.ViewHolder{
        TextView titleTv;
        RecyclerView recyclerView;

        public HomeRcHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.title_rc_home_holder);
            recyclerView = itemView.findViewById(R.id.recycler_view_rc_home_holder);

        }
    }

}
