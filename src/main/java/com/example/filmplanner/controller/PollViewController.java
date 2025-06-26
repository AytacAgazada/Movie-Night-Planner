package com.example.filmplanner.controller;

import com.example.filmplanner.dto.PollRequest; // Import PollRequest
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.repository.FilmRepository;
import com.example.filmplanner.repository.PollRepository;
import com.example.filmplanner.service.PollService; // Import PollService
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/polls")
public class PollViewController {

    private final PollRepository pollRepository;
    private final FilmRepository filmRepository;
    private final PollService pollService; // Inject PollService

    public PollViewController(PollRepository pollRepository, FilmRepository filmRepository, PollService pollService) {
        this.pollRepository = pollRepository;
        this.filmRepository = filmRepository;
        this.pollService = pollService; // Initialize PollService
    }

    // 1. Yeni poll yaratmaq üçün formu göstər
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("pollRequest", new PollRequest()); // Use PollRequest for form binding
        return "create-poll";
    }

    // 2. Formdan gələn POST sorğusunu qəbul edib poll yaradır
    @PostMapping("/create")
    public String createPoll(@Valid @ModelAttribute("pollRequest") PollRequest pollRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-poll";
        }
        Poll poll = pollService.createPoll(pollRequest); // Use PollService to create the poll
        return "redirect:/polls/" + poll.getId();
    }

    // 3. Poll səhifəsini göstərir (yalnız rəqəmləri qəbul edir)
    @GetMapping("/{id:\\d+}")
    public String viewPoll(@PathVariable Long id, Model model) {
        Optional<Poll> pollOpt = pollRepository.findById(id);
        if (pollOpt.isEmpty()) {
            return "redirect:/polls/create";
        }
        Poll poll = pollOpt.get();
        model.addAttribute("poll", poll);

        List<Film> films = filmRepository.findByPollId(id);
        films.sort(Comparator.comparing(film -> film.getTitle() != null ? film.getTitle() : ""));
        model.addAttribute("films", films);

        model.addAttribute("newFilm", new Film());
        return "poll";
    }

    // 4. Polla yeni film əlavə edir
    @PostMapping("/{pollId:\\d+}/films")
    public String addFilm(@PathVariable Long pollId, @ModelAttribute("newFilm") Film newFilm) {
        pollRepository.findById(pollId).ifPresent(poll -> {
            newFilm.setPoll(poll);
            newFilm.setVotes(0);
            filmRepository.save(newFilm);
        });
        return "redirect:/polls/" + pollId;
    }

    // 5. Filmə səs verir
    @PostMapping("/{pollId:\\d+}/vote/{filmId:\\d+}")
    public String vote(@PathVariable Long pollId, @PathVariable Long filmId) {
        filmRepository.findById(filmId).ifPresent(film -> {
            film.setVotes(film.getVotes() + 1);
            filmRepository.save(film);
        });
        return "redirect:/polls/" + pollId;
    }

    // 6. Poll nəticəsini göstərir
    @GetMapping("/{pollId:\\d+}/result")
    public String showResult(@PathVariable Long pollId, Model model) {
        Optional<Poll> pollOpt = pollRepository.findById(pollId);
        if (pollOpt.isEmpty()) {
            return "redirect:/polls/create";
        }
        Poll poll = pollOpt.get();
        model.addAttribute("poll", poll);

        List<Film> films = filmRepository.findByPollId(pollId);
        Film winner = films.stream()
                .max(Comparator.comparingInt(Film::getVotes))
                .orElse(null);
        model.addAttribute("winner", winner);

        return "result";
    }
}
        