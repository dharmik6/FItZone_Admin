package com.example.fitzoneadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<MemberList> memberList;

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
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

