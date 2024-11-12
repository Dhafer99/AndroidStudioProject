package com.example.mobile.ui.food.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.dto.ChatMessage;

import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.userMessageTextView.setText("Utilisateur: " + message.getUserMessage());
        holder.botResponseTextView.setText("Bot: " + message.getBotResponse());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView userMessageTextView;
        TextView botResponseTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.user_message);
            botResponseTextView = itemView.findViewById(R.id.bot_message);

        }
    }
}


