package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.VoteRequest;
import com.example.filmplanner.entity.Vote;

public class VoteMapper {
    public static Vote toEntity(VoteRequest request) {
        if (request == null) return null;
        Vote vote = new Vote();
        vote.setUserIdentifier(request.getUserIdentifier());
        return vote;
    }
}