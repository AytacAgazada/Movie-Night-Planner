package com.example.filmplanner.repository;

import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByPollIdAndUserIdentifier(Long pollId, String userIdentifier);

    Optional<Vote> findByPollIdAndUserIdentifier(Long pollId, String userIdentifier);

}
