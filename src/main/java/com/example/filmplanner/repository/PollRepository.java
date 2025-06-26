package com.example.filmplanner.repository;

import com.example.filmplanner.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findByUniqueLink(String uniqueLink);

}
