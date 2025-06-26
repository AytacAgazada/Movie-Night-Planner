package com.example.filmplanner.repository;

import com.example.filmplanner.entity.Film;
import com.example.filmplanner.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByPollId(Long pollId);

    List<Film> findByGenre(Genre genre);

}
