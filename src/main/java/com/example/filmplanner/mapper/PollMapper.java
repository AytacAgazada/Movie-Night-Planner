package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Poll;

public class PollMapper {
    public static Poll toEntity(PollRequest request) {
        if (request == null) return null;
        Poll poll = new Poll();
        poll.setTitle(request.getTitle());
        return poll;
    }
}