package application.service;

import application.model.AgeRestriction;

import java.util.Collection;

public interface IMPAService {
    Collection<AgeRestriction> getAgeRestrictions();
    AgeRestriction getAgeRestrictionById(int id);
}
