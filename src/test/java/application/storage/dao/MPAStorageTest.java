package application.storage.dao;

import application.model.AgeRestriction;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPAStorageTest {
    private final IMPAStorage mpaStorage;
    @Test
    public void getRestrictions() {
        Collection<AgeRestriction> restrictions = mpaStorage.getAgeRestrictions();
        Assertions.assertEquals(5, restrictions.size());
    }

    @Test
    public void getRestrictionById() {
        AgeRestriction testRestriction = new AgeRestriction(1);
        Optional<AgeRestriction> restriction = mpaStorage.getAgeRestrictionById(1);
        Assertions.assertEquals(testRestriction, restriction.get());
    }

    @Test
    public void getRestrictionIfNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            mpaStorage.getAgeRestrictionById(20);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 20 не найдена", exception.getMessage());
    }
}
