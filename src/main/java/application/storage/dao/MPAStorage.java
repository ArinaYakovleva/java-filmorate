package application.storage.dao;

import application.model.AgeRestriction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class MPAStorage extends CommonDbStorage implements IMPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPAStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<AgeRestriction> getAgeRestrictions() {
        String restrictionsQuery = "select * from age_restrictions";
        return jdbcTemplate.query(restrictionsQuery, (rs, rowNum) ->
                new AgeRestriction(
                        rs.getInt("restriction_id"),
                        rs.getString("restriction_name"))
        );
    }

    @Override
    public Optional<AgeRestriction> getAgeRestrictionById(int id) {
        SqlRowSet restrictionsQuery = jdbcTemplate.queryForRowSet("select  * from AGE_RESTRICTIONS " +
                "where RESTRICTION_ID=?", id);
        if (restrictionsQuery.next()) {
            AgeRestriction ageRestriction = new AgeRestriction(
                    restrictionsQuery.getInt("restriction_id"),
                    restrictionsQuery.getString("restriction_name")
            );
            return Optional.of(ageRestriction);
        } else {
            throw getNotFoundError(id);
        }
    }
}
