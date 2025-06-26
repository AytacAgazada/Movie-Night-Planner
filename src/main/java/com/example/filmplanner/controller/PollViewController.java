package com.example.filmplanner.controller;

import com.example.filmplanner.dto.CommentDto;
import com.example.filmplanner.dto.PollRequest;
import com.example.filmplanner.entity.Film;
import com.example.filmplanner.entity.Poll;
import com.example.filmplanner.enums.Genre;
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
import java.util.stream.Collectors;
import java.util.Collections;

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

    @GetMapping("/") // Əsas giriş yolu
    public String showStartPage() {
        return "start-page";
    }


    @GetMapping("/new") // Yeni səsvermə forması üçün
    public String showCreateNewPollForm(Model model) {
        model.addAttribute("pollRequest", new PollRequest());
        return "new-poll-form";
    }


    @PostMapping("/create") // Bu yol dəyişmir, formdan gəlir
    public String createPoll(@Valid @ModelAttribute("pollRequest") PollRequest pollRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-poll-form"; // Xəta olarsa yeni form səhifəsinə qayıt
        }
        Poll poll = pollService.createPoll(pollRequest);
        return "redirect:/polls/" + poll.getUniqueLink();
    }

    // --- Səsvermə Baxış Metodları ---
    @GetMapping("/{uniqueLink}")
    public String viewPoll(@PathVariable String uniqueLink, Model model, HttpServletRequest request) {
        Optional<Poll> pollOpt = pollRepository.findByUniqueLink(uniqueLink);
        if (pollOpt.isEmpty()) {
            return "redirect:/polls/"; // Tapılmazsa əsas səhifəyə yönləndiririk
        }
        Poll poll = pollOpt.get();
        model.addAttribute("poll", poll);

        List<Film> films = filmRepository.findByPollId(poll.getId());
        films.forEach(film -> {
            if (film.getComments() != null) { // NullPointer xətasının qarşısını almaq üçün əlavə yoxlama
                film.getComments().size(); // Şərhləri yükləmək üçün lazy loading-i tetikləyirik
            }
        });
        films.sort(Comparator.comparing(film -> film.getTitle() != null ? film.getTitle() : ""));
        model.addAttribute("films", films);

        model.addAttribute("newFilm", new Film());
        model.addAttribute("newComment", new CommentDto());

        String currentUrl = request.getRequestURL().toString();
        model.addAttribute("currentPollUrl", currentUrl);

        // --- Tövsiyələr hissəsi ---
        if (!films.isEmpty()) {
            Film topFilm = films.stream()
                    .max(Comparator.comparingInt(Film::getVotes))
                    .orElse(null);

            if (topFilm != null && topFilm.getGenre() != null) {
                List<Film> allFilmsOfSameGenre = filmRepository.findByGenre(topFilm.getGenre());

                List<Film> recommendedFilms = allFilmsOfSameGenre.stream()
                        .filter(rec -> rec.getPoll() != null && !rec.getPoll().getUniqueLink().equals(uniqueLink))
                        .collect(Collectors.toList());

                Collections.shuffle(recommendedFilms);
                model.addAttribute("recommendedFilms", recommendedFilms.stream().limit(5).collect(Collectors.toList()));
            }
        }

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
            return "redirect:/polls/"; // Tapılmazsa əsas səhifəyə yönləndiririk
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

    // --- Film Tövsiyələri Səhifəsi ---

    @GetMapping("/recommendations")
    public String showRecommendations(@RequestParam(required = false) Genre genre, Model model) {
        List<Film> films;
        if (genre != null) {
            films = filmRepository.findByGenre(genre);
            model.addAttribute("selectedGenre", genre); // Seçilmiş janrı modelə əlavə edirik
        } else {
            // Əgər janr seçilməyibsə, bütün janrlardan filmləri götürüb qarışdırırıq
            films = filmRepository.findAll();
        }

        Collections.shuffle(films); // Filmləri qarışdırırıq
        model.addAttribute("recommendedFilms", films.stream().limit(20).collect(Collectors.toList())); // İlk 20-ni göstəririk
        model.addAttribute("genres", Genre.values());

        return "recommendations";
    }

}