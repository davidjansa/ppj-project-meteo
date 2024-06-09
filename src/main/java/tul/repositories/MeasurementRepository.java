package tul.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import tul.data.City;
import tul.data.Measurement;

import java.sql.Timestamp;
import java.util.List;

/**
 * ORM on Measurement table.
 */
@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Integer> {

    boolean existsById(int id);

    void deleteById(int id);

    @Query("SELECT m FROM Measurement as m WHERE m.id=:id")
    public List<Measurement> findById(@Param("id") int id);

    @Query("SELECT m FROM Measurement as m WHERE m.city.id=:cityId AND m.mtime=:time")
    public List<Measurement> findByTime(@Param("cityId") String cityId, @Param("time") Timestamp time);

    // >= AND <=
    @Query("SELECT m FROM Measurement m WHERE m.city.id=:cityId AND m.mtime >= :timeStart AND m.mtime <= :timeEnd")
    public List<Measurement> findByTimes(@Param("cityId") String cityId, @Param("timeStart") Timestamp timeStart, @Param("timeEnd") Timestamp timeEnd);
}