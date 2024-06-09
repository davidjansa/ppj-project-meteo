package tul.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import tul.data.Country;

import java.util.List;

/**
 * ORM on Country table.
 */
@Repository
public interface CountryRepository extends CrudRepository<Country, String> {

    boolean existsByCode(String country_code);

    void deleteByCode(String code);

    @Query("SELECT c FROM Country as c WHERE c.code=:code")
    public List<Country> findByCode(@Param("code") String code);
}
