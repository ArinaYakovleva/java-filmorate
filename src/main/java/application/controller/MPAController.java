package application.controller;

import application.model.AgeRestriction;
import application.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {
    private final IFilmService filmService;

    @GetMapping
    public Collection<AgeRestriction> getAllRestrictions() {
        return filmService.getAgeRestrictions();
    }

    @GetMapping("/{id}")
    public AgeRestriction getRestrictionById(@PathVariable int id) {
        return filmService.getAgeRestrictionById(id);
    }
}
