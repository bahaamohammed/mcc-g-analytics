package com.bahaadev.mcc_g_analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private ArrayList<ProductsModle> products;

    private OnMyClickListener listener;

    public void OnMyClickListener(ProductsAdapter.OnMyClickListener listener) {
        this.listener = listener;
    }

    public interface OnMyClickListener{
        void OnItemClick(int position);
    }


    public ProductsAdapter(ArrayList<ProductsModle> products){
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent,false);
        return new MyViewHolder(view,listener);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductsModle productsModle = products.get(position);
        holder.tvTitle.setText(productsModle.getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
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
