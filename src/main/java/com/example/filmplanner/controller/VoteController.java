package com.example.filmplanner.controller;

import com.example.filmplanner.dto.VoteRequest;
import com.example.filmplanner.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<String> vote(@Valid @RequestBody VoteRequest request) {
        voteService.vote(request);
        return ResponseEntity.ok("Vote registered successfully");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserVoted(@RequestParam Long pollId, @RequestParam String userIdentifier) {
        return ResponseEntity.ok(voteService.hasUserVoted(pollId, userIdentifier));
    }
}