package application.service;

import application.model.AgeRestriction;
import application.storage.dao.IMPAStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;

@Service
public class MPAService implements IMPAService {
    private final IMPAStorage storage;

    @Autowired
    public MPAService(IMPAStorage storage) {
        this.storage = storage;
    }
    @Override
    public Collection<AgeRestriction> getAgeRestrictions() {
        return storage.getAgeRestrictions();
    }

    @Override
    public AgeRestriction getAgeRestrictionById(int id) {
        return storage.getAgeRestrictionById(id)
                .orElseThrow(() -> new NotFoundException(String.format("MPA c ID %d не найден", id)));
    }
}
