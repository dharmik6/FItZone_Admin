package com.example.fitzoneadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<MemberList> memberList;
//    Context context;

    public MemberAdapter(List<MemberList> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberList member = memberList.get(position);
        holder.textName.setText(member.getName());
        holder.textEmail.setText(member.getEmail());

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); // Use holder.getAdapterPosition() instead of getAdapterPosition()

                if (position != RecyclerView.NO_POSITION) {
                    MemberList item = memberList.get(position);

//                    String name = item.getName();
//                    String email = item.getEmail();
//                    String age = item.getAge();
//                    String number = item.getNumber();
//                    String gender = item.getGender();
//                    String address = item.getAddress();
//                    String acticity = item.getActicity(); // Typo corrected from hiegth to height
//                    String joidate = item.getJoidate(); // Typo corrected from wirgth to weight
//                    String imageUrl = item.getUserImageResourceId();

                    Intent intent=new Intent(context,MembersProfile.class);
                    intent.putExtra("name", item.getName());
                    intent.putExtra("email", item.getEmail());
                    intent.putExtra("age", item.getAge());
                    intent.putExtra("number", item.getNumber());
                    intent.putExtra("gender", item.getGender());
                    intent.putExtra("address", item.getAddress());
                    intent.putExtra("activity", item.getActicity()); // corrected typo
                    intent.putExtra("joidate", item.getJoidate()); // corrected typo
                    context.startActivity(intent);
//                    intent.putExtra("name", name);
//                    intent.putExtra("email", email);
//                    intent.putExtra("age", age);
//                    intent.putExtra("number", number);
//                    intent.putExtra("gender", gender);
//                    intent.putExtra("address", address);
//                    intent.putExtra("acticity", acticity);
//                    intent.putExtra("joidate", joidate);
//                    intent.putExtra("userimage", imageUrl);

//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                } else {
                    // Handle the case where the position is invalid or the view holder is detached.
                    // You can log an error or display a message to the user.
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

