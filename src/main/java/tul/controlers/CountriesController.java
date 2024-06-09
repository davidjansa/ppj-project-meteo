package tul.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tul.client.ServerApi;
import tul.data.City;
import tul.data.Country;
import tul.services.CityService;
import tul.services.CountryService;
import tul.services.MeasurementService;

import java.util.List;

/**
 * Countries REST API controller.
 */
@RestController
public class CountriesController {

    private CityService cityService;
    private CountryService countryService;
    private MeasurementService measurementService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Autowired
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @RequestMapping(value = ServerApi.COUNTRIES_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<Country>> showCountries() {
        List<Country> countries = countryService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.COUNTRY_PATH, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCountry(@PathVariable("code") String code) {
        code = code.toUpperCase();
        if (countryService.exists(code)) {
            countryService.delete(code);
            if (!countryService.exists(code)) {
                return new ResponseEntity<>(String.format("Country '%s' was deleted.", code), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format("Country '%s' could not be deleted.", code), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(String.format("Country '%s' does not exist.", code), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = ServerApi.COUNTRIES_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country> addCountry(@RequestBody Country country) {
        if (!countryService.exists(country.getCode())) {
            countryService.saveCountry(country);
            return new ResponseEntity<>(country,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}