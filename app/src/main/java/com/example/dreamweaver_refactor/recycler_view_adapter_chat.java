package com.example.dreamweaver_refactor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycler_view_adapter_chat extends RecyclerView.Adapter<recycler_view_adapter_chat.ViewHolder> {
    ArrayList<chat_componenets> messages_list = new ArrayList<>();
    Context context;

    public recycler_view_adapter_chat(Context context2) {
        this.context = context2;
    }

    @NonNull
    @Override
    public recycler_view_adapter_chat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_adapter_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        chat_componenets message = messages_list.get(position);
        if(message.getSent_by().equals(chat_componenets.sent_from_user)){
            holder.left_view.setVisibility(View.GONE);
            holder.left_view.setVisibility(View.GONE);
            holder.message_text_right.setText(message.getMessage());
        }else {
            holder.left_view.setVisibility(View.VISIBLE);
            holder.right_view.setVisibility(View.GONE);
            holder.message_text_left.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView message_text_right;
        private TextView message_text_left;
        LinearLayout right_view;
        LinearLayout left_view;

        public ViewHolder(View itemView) {
            super(itemView);
            message_text_right = itemView.findViewById(R.id.right_text_chat);
            message_text_left = itemView.findViewById(R.id.left_text_chat);
            right_view = itemView.findViewById(R.id.right_view);
            left_view = itemView.findViewById(R.id.left_view);

        }
    }

    public void setContacts(ArrayList<chat_componenets> contacts2) {
        this.messages_list = contacts2;
        notifyDataSetChanged();
    }
}