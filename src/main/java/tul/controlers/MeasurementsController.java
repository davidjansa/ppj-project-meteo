package tul.controlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit.http.Path;
import tul.client.ServerApi;
import tul.comparators.MeasurementComparator;
import tul.data.City;
import tul.data.Country;
import tul.data.Measurement;
import tul.openweathermap.OWMApiManager;
import tul.services.CityService;
import tul.services.CountryService;
import tul.services.MeasurementService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Measurements REST API controller.
 */
@RestController
public class MeasurementsController {

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

    @RequestMapping(value = ServerApi.MEASUREMENTS_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> showMeasurements() {
        List<Measurement> measurements = measurementService.getAllMeasurements();
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_DAY, method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getLastDayMeasures(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        List<Measurement> measurements = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 1);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_WEEK, method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getLastWeekMeasures(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        List<Measurement> measurements = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 7);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_WEEKS, method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getLastWeeksMeasures(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        // last week data
        List<Measurement> measurements1 = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 7);

        // before last week data
        Measurement lastMeasurement = measurements1.get(0);
        List<Measurement> measurements2 = getMeasurements(country, city, lastMeasurement.getmMtime(), 7);

        // merge two weeks
        List<Measurement> measurements = new ArrayList<Measurement>(measurements1);
        measurements.addAll(measurements2);
        measurements.sort(new MeasurementComparator());

        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_AVG_DAY, method = RequestMethod.GET)
    public ResponseEntity<Measurement> getLastDayMeasuresAvg(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        List<Measurement> measurements = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 1);

        // calculate avg
        Measurement measurement = calculateAverage(measurements);

        return new ResponseEntity<>(measurement, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_AVG_WEEK, method = RequestMethod.GET)
    public ResponseEntity<Measurement> getLastWeekMeasuresAvg(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        List<Measurement> measurements = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 7);

        // calculate avg
        Measurement measurement = calculateAverage(measurements);

        return new ResponseEntity<>(measurement, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_AVG_WEEKS, method = RequestMethod.GET)
    public ResponseEntity<Measurement> getLastWeeksMeasuresAvg(@PathVariable("country") String country, @PathVariable("city") String city) throws Exception {
        // last week data
        List<Measurement> measurements1 = getMeasurements(country, city, new Timestamp(System.currentTimeMillis()), 7);

        // before last week data
        Measurement lastMeasurement = measurements1.get(0);
        List<Measurement> measurements2 = getMeasurements(country, city, lastMeasurement.getmMtime(), 7);

        // merge two weeks
        List<Measurement> measurements = new ArrayList<Measurement>(measurements1);
        measurements.addAll(measurements2);

        // calculate avg
        Measurement measurement = calculateAverage(measurements);

        return new ResponseEntity<>(measurement, HttpStatus.OK);
    }

    @RequestMapping(value = ServerApi.MEASUREMENT_PATH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMeasurementById(@PathVariable("id") String id) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (Exception e) {
            return new ResponseEntity<String>(String.format("Invalid id '%s'.", id), HttpStatus.BAD_REQUEST);
        }

        if (measurementService.exists(idInt)) {
            measurementService.delete(idInt);
            if (!measurementService.exists(idInt)) {
                return new ResponseEntity<>(String.format("Measurement '%s' was deleted.", id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format("Measurement '%s' could not be deleted.", id), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(String.format("Measurement '%s' does not exist.", id), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = ServerApi.MEASUREMENTS_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Measurement> addMeasurement(@RequestBody Measurement measurement) {
        if (!measurementService.exists(measurement.getId())) {
            measurementService.saveMeasurement(measurement);
            return new ResponseEntity<>(measurement,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Based on inserted params, firstly load measurements from db and then (if its needed) download data from API and insert them in db.
     * Then returns list of measurements.
     * @param country country code
     * @param city city id
     * @param now now timestamp
     * @param days number of days to be loaded
     * @return list of measurements
     * @throws Exception multiple exceptions
     */
    private List<Measurement> getMeasurements(String country, String city, Timestamp now, int days) throws Exception {
        Timestamp end = measurementService.getEndOfPreviousDay(now);
        Timestamp start = measurementService.subtractAndGetTimestamp(end, 23+24*(days-1));

        // get queried measurements from db
        List<Measurement> measurements = new ArrayList<Measurement>();
        int numberOfMeasurements = 0;
        List<City> cities = cityService.getCitiesByName(city);
        if (!cities.isEmpty()) {
            measurements = measurementService.getMeasurementsByTimes(cities.get(0).getId(), start, end);
            numberOfMeasurements = measurements.size();
        }

        // if there is not enough measurements -> download new ones
        if (numberOfMeasurements < 24*days) {
            OWMApiManager am = new OWMApiManager();

            String content = null;
            try {
                content = am.getData(country, city, start, end);
            } catch (Exception e){

            }
            if (content == null) throw new Exception(String.format("Country '%s' or City '%s' does not exists.", country, city));

            // convert content into json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(content);

            // check if country exists in db
            String cityId = jsonNode.get("city_id").toString();
            City newCity = null;
            if (!countryService.exists(country)){
                Country newCountry = new Country(country);
                countryService.saveCountry(newCountry);

                newCity = new City(cityId, newCountry, city);
                cityService.saveCity(newCity);
            }
            // check if city exists in db
            if (!cityService.exists(cityId)){
                Country existingCountry = countryService.getCountryByCountryCode(country).get(0);
                newCity = new City(cityId, existingCountry, city);
                cityService.saveCity(newCity);
            }

            if (newCity == null) newCity = cityService.getCitiesById(cityId).get();

            // convert downloaded data into Measurements
            List<Measurement> downloadedMeasurements = null;
            if (newCity != null && content != null) {
                downloadedMeasurements = measurementService.convertDataIntoMeasurements(jsonNode, newCity);
            }

            if (downloadedMeasurements == null) throw new Exception("There are no downloaded measurements.");

            // add non-existing measurements into db
            int diff = 24*days - numberOfMeasurements;
            for (int i = 0; i < downloadedMeasurements.size(); i++) {
                Measurement newM = downloadedMeasurements.get(i);
                List<Measurement> existingCitites = measurementService.getMeasurementsByTime(city, newM.getmMtime());
                if (existingCitites.isEmpty()) {
                    measurementService.saveMeasurement(newM);
                    measurements.add(newM);

                    diff--;
                    if (diff == 0) break;
                }
            }

            measurements.sort(new MeasurementComparator());
        }

        return measurements;
    }

    /**
     * Calculates avg values for inserted list of measurements.
     * @param measurements list of measurements
     * @return one measurement with avg values of temperature, pressure and humidity
     */
    private Measurement calculateAverage(List<Measurement> measurements) {
        float avgTemp = 0;
        float avgPressure = 0;
        float avgHumidity = 0;

        int size = measurements.size();
        for (int i = 0; i < size; i++) {
            avgTemp += measurements.get(i).getTemp();
            avgPressure += measurements.get(i).getPressure();
            avgHumidity += measurements.get(i).getHumidity();
        }

        // calculate the averages
        avgTemp /= measurements.size();
        avgPressure /= measurements.size();
        avgHumidity /= measurements.size();

        // default values
        int id = 9999;
        City city = measurements.get(0).getCity();
        Timestamp mTime = measurements.get(0).getmMtime();

        return new Measurement(id, city, mTime, avgTemp, avgPressure, avgHumidity);
    }
}