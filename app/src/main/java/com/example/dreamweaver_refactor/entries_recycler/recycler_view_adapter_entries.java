package com.example.dreamweaver_refactor.entries_recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamweaver_refactor.R;

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
        holder.title.setText(this.entries.get(position).getTitle());
        holder.date.setText(this.entries.get(position).getDate());
        holder.tag.setText(this.entries.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }



public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView date;
    private TextView tag;

    public ViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.date);
        tag = itemView.findViewById(R.id.tag);

    }
}

public void setContacts(ArrayList<dream_entry_components> contacts2) {
    this.entries= contacts2;
    notifyDataSetChanged();
}}
