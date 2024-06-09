package tul.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tul.data.Country;
import tul.repositories.CountryRepository;

import java.util.List;

/**
 * Service for working with Country Repository.
 */
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return (List<Country>) countryRepository.findAll();
    }

    public List<Country> getCountryByCountryCode(String countryCode) {
        return countryRepository.findByCode(countryCode);
    }

    @Transactional
    public void saveCountry(Country country) {
        countryRepository.save(country);
    }

    @Transactional
    public void delete(String countryCode) {
        countryRepository.deleteByCode(countryCode);
    }

    public boolean exists(String countryCode) {return countryRepository.existsByCode(countryCode); }
}
