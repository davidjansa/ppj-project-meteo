package tul.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tul.data.City;
import tul.repositories.CityRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for working with City Repository.
 */
@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return (List<City>) cityRepository.findAll();
    }

    public List<City> getCitiesByCountryCode(String countryCode) {
        return cityRepository.findByCountryCode(countryCode);
    }

    public List<City> getCitiesByName(String cityName) {
        return cityRepository.findByName(cityName);
    }

    public Optional<City> getCitiesById(String cityId) {
        return cityRepository.findById(cityId);
    }

    @Transactional
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    @Transactional
    public void delete(String id) {
        cityRepository.deleteById(id);
    }

    public boolean exists(String id) { return cityRepository.existsById(id); }
}