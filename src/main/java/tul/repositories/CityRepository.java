package tul.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tul.data.City;

import java.util.List;
import java.util.Optional;

/**
 * ORM on City table.
 */
@Repository
public interface CityRepository extends CrudRepository<City, String> {

    boolean existsById(String id);

    void deleteById(String id);

    @Query("SELECT c FROM City as c WHERE c.country.code=:countryCode")
    public List<City> findByCountryCode(@Param("countryCode") String countryCode);

    @Query("SELECT c FROM City as c WHERE c.name=:name")
    public List<City> findByName(@Param("name") String name);

    @Query("SELECT c FROM City as c WHERE c.id=:id")
    public Optional<City> findById(@Param("id") String id);
}
