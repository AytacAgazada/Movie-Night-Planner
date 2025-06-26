package com.example.filmplanner.service;

import com.example.filmplanner.dto.VoteRequest;
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Vote;
import com.example.filmplanner.repository.FilmRepository;
import com.example.filmplanner.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final FilmRepository filmRepository;

    public VoteService(VoteRepository voteRepository, FilmRepository filmRepository) {
        this.voteRepository = voteRepository;
        this.filmRepository = filmRepository;
    }

    public void vote(VoteRequest voteRequest) {
        Film film = filmRepository.findById(voteRequest.getFilmId())
                .orElseThrow(() -> new RuntimeException("Film not found"));

        if (voteRepository.existsByPollIdAndUserIdentifier(film.getPoll().getId(), voteRequest.getUserIdentifier())) {
            throw new RuntimeException("User already voted in this poll");
        }

        Vote vote = new Vote();
        vote.setFilm(film);
        vote.setPoll(film.getPoll());
        vote.setUserIdentifier(voteRequest.getUserIdentifier());

        voteRepository.save(vote);
    }

    public boolean hasUserVoted(Long pollId, String userIdentifier) {
        return voteRepository.existsByPollIdAndUserIdentifier(pollId, userIdentifier);
    }
}
