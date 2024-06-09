import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tul.Main;
import tul.controlers.CitiesController;
import tul.controlers.CountriesController;
import tul.controlers.MeasurementsController;
import tul.data.City;
import tul.data.Country;
import tul.data.Measurement;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
public class RestApiTests {

    @Autowired
    private CountriesController crc;

    @Autowired
    private CitiesController cc;

    @Autowired
    private MeasurementsController mc;

    @Test
    public void testShowAddDeleteCountries() {
        // show
        ResponseEntity<List<Country>> response = crc.showCountries();
        assertEquals("Error in number of countries", 1, Objects.requireNonNull(response.getBody()).size());
        // add
        Country c = new Country("YZ");
        crc.addCountry(c);
        response = crc.showCountries();
        assertEquals("Error in number of countries after add", 2, Objects.requireNonNull(response.getBody()).size());
        // delete
        crc.deleteCountry("YZ");
        response = crc.showCountries();
        assertEquals("Error in number of countries after delete", 1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testShowAddDeleteCities() {
        // show
        ResponseEntity<List<City>> response = cc.showCities();
        assertEquals("Error in number of cities", 1, Objects.requireNonNull(response.getBody()).size());
        // add
        City c = new City("ID99", new Country("CZ"), "TestingName");
        cc.addCity(c);
        response = cc.showCities();
        assertEquals("Error in number of cities after add", 2, Objects.requireNonNull(response.getBody()).size());
        // delete
        cc.deleteCityById("ID99");
        response = cc.showCities();
        assertEquals("Error in number of cities after delete", 1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testShowAddDeleteMeasurements() throws Exception {
        // show
        ResponseEntity<List<Measurement>> response = mc.showMeasurements();
        assertEquals("Error in number of measurements", 3, Objects.requireNonNull(response.getBody()).size());
        // add
        Measurement m = new Measurement(3, new City("id1", new Country("CZ"), "CityName1"), new Timestamp(1780972211), 100, 100, 100);
        mc.addMeasurement(m);
        response = mc.showMeasurements();
        assertEquals("Error in number of measurements after add", 4, Objects.requireNonNull(response.getBody()).size());
        // delete
        mc.deleteMeasurementById("3");
        response = mc.showMeasurements();
        assertEquals("Error in number of measurements after delete", 3, Objects.requireNonNull(response.getBody()).size());

        // actual data
        testGetData();

        // avg data
        testGetAvgData();
    }

    public void testGetData() throws Exception {
        // last 2 weeks
        ResponseEntity<List<Measurement>> response = mc.getLastWeeksMeasures("CZ", "Prague");
        assertEquals("Error in number oof last 2 weeks measurements", 336, Objects.requireNonNull(response.getBody()).size());

        // last week
        response = mc.getLastWeekMeasures("CZ", "Prague");
        assertEquals("Error in number of last week measurements", 168, Objects.requireNonNull(response.getBody()).size());

        // last day
        response = mc.getLastDayMeasures("CZ", "Prague");
        assertEquals("Error in number of last day measurements", 24, Objects.requireNonNull(response.getBody()).size());
    }

    public void testGetAvgData() throws Exception {
        // last day avg
        ResponseEntity<Measurement> response = mc.getLastDayMeasuresAvg("CZ", "Prague");
        assertEquals("Error in measurement id", 9999, Objects.requireNonNull(response.getBody()).getId());

        // last week avg
        response = mc.getLastDayMeasuresAvg("CZ", "Prague");
        assertEquals("Error in measurement id", 9999, Objects.requireNonNull(response.getBody()).getId());

        // last 2 weeks avg
        response = mc.getLastDayMeasuresAvg("CZ", "Prague");
        assertEquals("Error in measurement id", 9999, Objects.requireNonNull(response.getBody()).getId());
    }
}
