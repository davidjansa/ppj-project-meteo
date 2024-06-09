package tul.client;

import org.springframework.web.bind.annotation.RequestParam;
import retrofit.http.*;
import tul.data.City;
import tul.data.Country;
import tul.data.Measurement;

import java.util.List;

/**
 * Interface defining REST API paths.
 */
public interface ServerApi {

    // countries
    public static final String COUNTRIES_PATH = "/countries";
    public static final String COUNTRY_PATH = COUNTRIES_PATH + "/{code}";

    @GET(COUNTRIES_PATH)
    public List<Country> showCountries();

    @DELETE(COUNTRY_PATH)
    public void deleteCountry(@Path("code") String code);

    @POST(COUNTRIES_PATH)
    public void addCountry(@Body Country country);

    // cities

    public static final String CITIES_PATH = "/cities";
    public static final String CITY_PATH_ID = CITIES_PATH + "/{id}";
    public static final String CITY_PATH_NAME = CITIES_PATH + "/name/{name}";

    @GET(CITIES_PATH)
    public List<City> showCities();

    @DELETE(CITY_PATH_ID)
    public void deleteCityById(@Path("id") String id);

    @DELETE(CITY_PATH_NAME)
    public void deleteCityByName(@Path("name") String name);

    @POST(CITIES_PATH)
    public void addCity(@Body City city);

    // measurements

    public static final String MEASUREMENTS_PATH = "/measurements";
    public static final String MEASUREMENT_PATH_ID = MEASUREMENTS_PATH + "/{id}";

    public static final String MEASUREMENT_DAY = MEASUREMENTS_PATH + "/day/{country}/{city}";

    public static final String MEASUREMENT_WEEK = MEASUREMENTS_PATH + "/week/{country}/{city}";

    public static final String MEASUREMENT_WEEKS = MEASUREMENTS_PATH + "/weeks/{country}/{city}";

    @GET(MEASUREMENTS_PATH)
    public List<Measurement> showMeasurements();

    @GET(MEASUREMENT_DAY)
    public List<Measurement> getLastDayMeasures(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @GET(MEASUREMENT_WEEK)
    public List<Measurement> getLastWeekMeasures(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @GET(MEASUREMENT_WEEKS)
    public List<Measurement> getLastTwoWeeksMeasures(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @POST(MEASUREMENTS_PATH)
    public void addMeasurement(@Body Measurement measurement);

    public static final String MEASUREMENTS_AVG_PATH = "/measurementsg";

    public static final String MEASUREMENT_AVG_DAY = MEASUREMENTS_AVG_PATH + "/day/{country}/{city}";

    public static final String MEASUREMENT_AVG_WEEK = MEASUREMENTS_AVG_PATH + "/week/{country}/{city}";

    public static final String MEASUREMENT_AVG_WEEKS = MEASUREMENTS_AVG_PATH + "/weeks/{country}/{city}";

    @GET(MEASUREMENT_AVG_DAY)
    public List<Measurement> getLastDayMeasuresAvg(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @GET(MEASUREMENT_AVG_WEEK)
    public List<Measurement> getLastWeekMeasuresAvg(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @GET(MEASUREMENT_AVG_WEEKS)
    public List<Measurement> getLastTwoWeeksMeasuresAvg(@RequestParam("country") String country, @RequestParam("city") String cityId);

    @DELETE(MEASUREMENT_PATH_ID)
    public void deleteMeasurementById(@RequestParam("id") String id);


}
