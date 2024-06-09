package tul.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tul.data.City;
import tul.data.Measurement;
import tul.repositories.MeasurementRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Service for working with Measurement Repository.
 */
@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> getAllMeasurements() {
        return (List<Measurement>) measurementRepository.findAll();
    }

    public List<Measurement> getMeasurementsById(int measurementId) {
        return measurementRepository.findById(measurementId);
    }

    public List<Measurement> getMeasurementsByTime(String mCityId, Timestamp mTime) {
        return measurementRepository.findByTime(mCityId, mTime);
    }

    public List<Measurement> getMeasurementsByTimes(String mCityId, Timestamp mTimeStart, Timestamp mTimeEnd) {
        return measurementRepository.findByTimes(mCityId, mTimeStart, mTimeEnd);
    }

    @Transactional
    public Measurement saveMeasurement(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    @Transactional
    public void delete(int id) {
        measurementRepository.deleteById(id);
    }

    public boolean exists(int id) { return measurementRepository.existsById(id); }

    /**
     * Get end of previous day of inserted timestamp.
     * @param now actual timestamp
     * @return timestamp that is set to one day before of 'now' param and set to 23rd hour.
     */
    public Timestamp getEndOfPreviousDay(Timestamp now) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * Subtract hours from inserted timestamp and return new timestamp.
     * @param timestamp timestamp to be subtracted from
     * @param hours hours to be subtracted
     * @return
     */
    public Timestamp subtractAndGetTimestamp(Timestamp timestamp, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.HOUR, -hours);

        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * Convert json data into list of Measurements
     * @param jsonNode content from REST-API in json format
     * @param city measurements will be attached to this city
     * @return list of Measurements for inserted city
     * @throws IOException
     */
    public List<Measurement> convertDataIntoMeasurements(JsonNode jsonNode, City city) throws IOException {
        List<Measurement> result = new ArrayList<>();
        try {
            ArrayNode measures = (ArrayNode) jsonNode.get("list");
            for (int i = 0; i < measures.size(); i++) {
                Timestamp mTime = new Timestamp(measures.get(i).get("dt").asLong()*1000);
                float temp = (float) measures.get(i).get("main").get("temp").asDouble();
                float pressure = (float) measures.get(i).get("main").get("pressure").asDouble();
                float humidity = (float) measures.get(i).get("main").get("humidity").asDouble();
                result.add(new Measurement(city, mTime, temp, pressure, humidity));
            }
        } catch (Exception e) {
            return null;
        }

        return result;
    }
}
