package application.storage.dao;

import application.model.AgeRestriction;

import java.util.Collection;
import java.util.Optional;

public interface IMPAStorage {
    Collection<AgeRestriction> getAgeRestrictions();
    Optional<AgeRestriction> getAgeRestrictionById(int id);
}
