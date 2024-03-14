package com.example.fitzoneadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationItemList> notificationItemLists;
    Context context;

    public NotificationAdapter(Context context, List<NotificationItemList> notificationItemLists) {
        this.notificationItemLists = notificationItemLists;
        this.context = context;

    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationItemList member = notificationItemLists.get(position);
        holder.show_feed_name.setText(member.getFeedback_name());
        holder.show_feed_back.setText(member.getFeedback());



        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    NotificationItemList item = notificationItemLists.get(position);

                    // Create an intent to start the MembersProfile activity
//                    Intent intent = new Intent(context, Diet.class);
                    // Pass data to the intent
//                    intent.putExtra("name", item.getName());
//                    intent.putExtra("Description", item.getDescription());

                    // Start the activity
//                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationItemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_feed_name;
        public TextView show_feed_back;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_feed_name = itemView.findViewById(R.id.show_feed_name);
            show_feed_back = itemView.findViewById(R.id.show_feed_back);
        }
    }
}