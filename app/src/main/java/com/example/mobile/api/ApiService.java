package com.example.mobile.api;

import com.example.mobile.dto.ChatResponse;
import com.example.mobile.dto.UserMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("chat") // Assure-toi que c'est le bon endpoint
    Call<ChatResponse> sendMessage(@Body UserMessage userMessage);
}
