package com.example.filmplanner.mapper;

import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Poll;
import org.springframework.stereotype.Component;

@Component
public class PollMapper {

    public Poll toEntity(PollRequest dto) {
        if (dto == null) return null;

        Poll poll = new Poll();
        poll.setTitle(dto.getTitle());
        poll.setCreatedAt(java.time.LocalDateTime.now());
        return poll;
    }

    public PollRequest toDto(Poll poll) {
        if (poll == null) return null;

        PollRequest dto = new PollRequest();
        dto.setTitle(poll.getTitle());
        return dto;
    }
}
