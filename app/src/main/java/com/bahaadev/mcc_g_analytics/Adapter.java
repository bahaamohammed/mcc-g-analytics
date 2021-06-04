package com.bahaadev.mcc_g_analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<ItemModle> items;

    private OnMyClickListener listener;

    public interface OnMyClickListener{
        void OnItemClick(int position);
    }

    public void OnMyClickListener (OnMyClickListener listener){
        this.listener = listener;
    }

    public Adapter(ArrayList<ItemModle> items){
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent,false);
        return new MyViewHolder(view,listener);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModle itemModle = items.get(position);
        holder.tvTitle.setText(itemModle.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        public MyViewHolder(@NonNull View itemView,OnMyClickListener listener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.OnItemClick(getAbsoluteAdapterPosition());
                    }
                }
            });
        }
    }
}
