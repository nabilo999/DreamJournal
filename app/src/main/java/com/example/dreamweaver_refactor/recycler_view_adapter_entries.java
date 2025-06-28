package com.example.dreamweaver_refactor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycler_view_adapter_entries extends RecyclerView.Adapter<recycler_view_adapter_entries.ViewHolder>{

    ArrayList<dream_entry_components> entries = new ArrayList<>();
    Context context;

    public recycler_view_adapter_entries(Context context2) {
        this.context = context2;
    }

    @NonNull
    @Override
    public recycler_view_adapter_entries.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dream_entries, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_view_adapter_entries.ViewHolder holder, int position) {
        holder.entry_title.setText(this.entries.get(position).getTitle());
        holder.entry_time.setText(this.entries.get(position).getDate());
        holder.entry_tag.setText(this.entries.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }



public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView entry_title;
    private TextView entry_time;
    private TextView entry_tag;

    public ViewHolder(View itemView) {
        super(itemView);
        entry_title = itemView.findViewById(R.id.title);
        entry_time = itemView.findViewById(R.id.date);
        entry_tag = itemView.findViewById(R.id.tag);

    }
}

public void setContacts(ArrayList<dream_entry_components> contacts2) {
    this.entries= contacts2;
    notifyDataSetChanged();
}}
