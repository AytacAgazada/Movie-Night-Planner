package com.example.filmplanner.service;

import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.repository.PollRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PollService {

    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll createPoll(PollRequest request) {
        Poll poll = new Poll();
        poll.setTitle(request.getTitle());
        poll.setCreatedAt(LocalDateTime.now());
        poll.setUniqueLink(UUID.randomUUID().toString());
        return pollRepository.save(poll);
    }

    public Poll getPollByUniqueLink(String uniqueLink) {
        return pollRepository.findByUniqueLink(uniqueLink)
                .orElseThrow(() -> new RuntimeException("Poll not found"));
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }
}