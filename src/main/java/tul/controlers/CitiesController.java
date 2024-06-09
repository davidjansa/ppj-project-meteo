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
 * Cities REST API controller.
 */
@RestController
public class CitiesController {

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

    @RequestMapping(value = ServerApi.CITIES_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<City>> showCities() {
        List<City> cities = cityService.getAllCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.CITY_PATH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCityById(@PathVariable("id") String id) {
        if (cityService.exists(id)) {
            City c = cityService.getCitiesById(id).get();
            cityService.delete(id);
            if (!cityService.exists(id)) {
                return new ResponseEntity<>(String.format("City '%s'='%s' was deleted.", id, c.getName()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format("City '%s' could not be deleted.", id), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(String.format("City '%s' does not exist.", id), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = ServerApi.CITY_PATH_NAME, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCityByName(@PathVariable("name") String name) {
        List<City> citiesWithSameName = cityService.getCitiesByName(name);

        if (citiesWithSameName.isEmpty()) return new ResponseEntity<>(String.format("City '%s' does not exist.", name), HttpStatus.NOT_FOUND);
        if (citiesWithSameName.size() > 1) return new ResponseEntity<>(String.format("There is multiple cities with the same name '%s', try to delete it by id.", name), HttpStatus.BAD_REQUEST);

        City c = citiesWithSameName.get(0);

        return deleteCityById(c.getId());
    }

    @RequestMapping(value = ServerApi.CITIES_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> addCity(@RequestBody City city) {
        if (!cityService.exists(city.getId())) {
            cityService.saveCity(city);
            return new ResponseEntity<>(city,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}