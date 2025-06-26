package com.example.filmplanner.controller;

import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@Valid @RequestBody PollRequest request) {
        return ResponseEntity.ok(pollService.createPoll(request));
    }

    @GetMapping("/{uniqueLink}")
    public ResponseEntity<Poll> getPoll(@PathVariable String uniqueLink) {
        return ResponseEntity.ok(pollService.getPollByUniqueLink(uniqueLink));
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }
}
