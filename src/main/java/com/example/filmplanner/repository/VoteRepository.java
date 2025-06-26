package com.example.filmplanner.repository;

import com.example.filmplanner.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoterNameAndFilmId(String voterName, Long filmId);

    int countByFilmId(Long filmId);

}
