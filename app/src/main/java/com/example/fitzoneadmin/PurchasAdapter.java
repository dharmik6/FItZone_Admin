package com.example.fitzoneadmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasAdapter extends RecyclerView.Adapter<PurchasAdapter.ViewHolder> {
    private List<MemberList> purchasList;
    Context context;

    public PurchasAdapter(Context context, List<MemberList> purchasList){
        this.purchasList = purchasList;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchases_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberList purchas = purchasList.get(position);
        holder.textProductName.setText(purchas.getName());
        holder.textProductPrice.setText(purchas.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MemberList item = purchasList.get(position);

                    // Create an intent to start the AcceptedBookingDetail activity
                    Intent intent = new Intent(context, PurchaseDetails.class);
                    // Pass data to the intent
                    intent.putExtra("package", item.getEmail());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("id", item.getId());
                    // Start the activity
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchasList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textProductName;
        public TextView textProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.name);
            textProductPrice = itemView.findViewById(R.id.package_name);
        }
    }
}
