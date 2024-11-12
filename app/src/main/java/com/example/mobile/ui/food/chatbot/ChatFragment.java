package com.example.mobile.ui.food.chatbot;

import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.api.ApiService;
import com.example.mobile.api.RetrofitClient;
import com.example.mobile.databinding.FragmentChatBinding;
import com.example.mobile.databinding.FragmentFoodBinding;
import com.example.mobile.dto.ChatMessage;
import com.example.mobile.dto.ChatResponse;
import com.example.mobile.dto.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private EditText userMessageEditText;
    private Button sendButton;
    private RecyclerView messagesRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userMessageEditText = binding.userMessageEditText;
        sendButton = binding.sendButton;
        messagesRecyclerView = binding.messagesRecyclerView;

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);

        // Configurer le RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true); // Lister les messages du bas vers le haut
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String userMessage = userMessageEditText.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                sendUserMessage(userMessage);
            }
        });

        return root;
    }

    private void sendUserMessage(String userMessage) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        UserMessage message = new UserMessage(userMessage);

        Call<ChatResponse> call = apiService.sendMessage(message);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();
                    if (chatResponse.getError() == null) {
                        // Ajouter les nouveaux messages à la liste et mettre à jour l'UI
                        messages.add(new ChatMessage(userMessage, chatResponse.getBot_response()));
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        // Gérer les erreurs
                    }
                } else {
                    // Gérer l'échec de la réponse
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                // Gérer l'échec de la requête
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
