package application.controller;

import application.model.AgeRestriction;
import application.service.IMPAService;
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
    private final IMPAService mpaService;

    @GetMapping
    public Collection<AgeRestriction> getAllRestrictions() {
        return mpaService.getAgeRestrictions();
    }

    @GetMapping("/{id}")
    public AgeRestriction getRestrictionById(@PathVariable int id) {
        return mpaService.getAgeRestrictionById(id);
    }
}
