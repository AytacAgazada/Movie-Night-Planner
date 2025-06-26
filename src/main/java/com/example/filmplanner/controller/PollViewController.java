package com.example.filmplanner.controller;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.repository.FilmRepository;
import com.example.filmplanner.repository.PollRepository;
import com.example.filmplanner.service.CommentService;
import com.example.filmplanner.service.PollService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/polls")
public class PollViewController {

    private final PollRepository pollRepository;
    private final FilmRepository filmRepository;
    private final PollService pollService;
    private final CommentService commentService;

    public PollViewController(PollRepository pollRepository, FilmRepository filmRepository, PollService pollService, CommentService commentService) {
        this.pollRepository = pollRepository;
        this.filmRepository = filmRepository;
        this.pollService = pollService;
        this.commentService = commentService;
    }

    // --- Səsvermə Yaratma Metodları ---
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("pollRequest", new PollRequest());
        return "create-poll";
    }

    @PostMapping("/create")
    public String createPoll(@Valid @ModelAttribute("pollRequest") PollRequest pollRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-poll";
        }
        Poll poll = pollService.createPoll(pollRequest);
        return "redirect:/polls/" + poll.getUniqueLink();
    }

    // --- Səsvermə Baxış Metodları ---
    @GetMapping("/{uniqueLink}")
    public String viewPoll(@PathVariable String uniqueLink, Model model, HttpServletRequest request) {
        Optional<Poll> pollOpt = pollRepository.findByUniqueLink(uniqueLink);
        if (pollOpt.isEmpty()) {
            return "redirect:/polls/create";
        }
        Poll poll = pollOpt.get();
        model.addAttribute("poll", poll);

        List<Film> films = filmRepository.findByPollId(poll.getId());
        films.forEach(film -> film.getComments().size()); // Şərhləri yükləmək üçün lazy loading-i tetikləyirik
        films.sort(Comparator.comparing(film -> film.getTitle() != null ? film.getTitle() : ""));
        model.addAttribute("films", films);

        model.addAttribute("newFilm", new Film());
        model.addAttribute("newComment", new CommentDto());

        // BURADA DƏYİŞİKLİK: HttpServletRequest-dən URL-i götürüb modelə əlavə edirik
        String currentUrl = request.getRequestURL().toString();
        model.addAttribute("currentPollUrl", currentUrl); // Yeni atribut əlavə edildi

        return "poll";
    }

    // --- Film Metodları ---
    @PostMapping("/{uniqueLink}/films")
    public String addFilm(@PathVariable String uniqueLink, @ModelAttribute("newFilm") Film newFilm) {
        pollRepository.findByUniqueLink(uniqueLink).ifPresent(poll -> {
            newFilm.setPoll(poll);
            newFilm.setVotes(0);
            filmRepository.save(newFilm);
        });
        return "redirect:/polls/" + uniqueLink;
    }

    @PostMapping("/{uniqueLink}/vote/{filmId:\\d+}")
    public String vote(@PathVariable String uniqueLink, @PathVariable Long filmId) {
        filmRepository.findById(filmId).ifPresent(film -> {
            film.setVotes(film.getVotes() + 1);
            filmRepository.save(film);
        });
        return "redirect:/polls/" + uniqueLink;
    }

    // --- Şərh Metodları ---
    @PostMapping("/{uniqueLink}/films/{filmId:\\d+}/comments")
    public String addComment(@PathVariable String uniqueLink,
                             @PathVariable Long filmId,
                             @Valid @ModelAttribute("newComment") CommentDto commentDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/polls/" + uniqueLink + "#film-" + filmId;
        }

        if (commentDto.getUserIdentifier() == null || commentDto.getUserIdentifier().trim().isEmpty()) {
            commentDto.setUserIdentifier("Anonymous-" + UUID.randomUUID().toString().substring(0, 8));
        }

        try {
            commentService.addComment(filmId, commentDto);
        } catch (RuntimeException e) {
            System.err.println("Error adding comment: " + e.getMessage());
        }
        return "redirect:/polls/" + uniqueLink + "#film-" + filmId;
    }

    // --- Nəticə Metodu ---
    @GetMapping("/{uniqueLink}/result")
    public String showResult(@PathVariable String uniqueLink, Model model) {
        Optional<Poll> pollOpt = pollRepository.findByUniqueLink(uniqueLink);
        if (pollOpt.isEmpty()) {
            return "redirect:/polls/create";
        }
        Poll poll = pollOpt.get();
        model.addAttribute("poll", poll);

        List<Film> films = filmRepository.findByPollId(poll.getId());
        Film winner = films.stream()
                .max(Comparator.comparingInt(Film::getVotes))
                .orElse(null);
        model.addAttribute("winner", winner);

        return "result";
    }
}