package tul.openweathermap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import tul.data.City;
import tul.data.Country;
import tul.data.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for communication with OpenWeatherMap API (<a href="https://openweathermap.org/">...</a>)
 */
public class OWMApiManager {
    private final String API_KEY = System.getenv("owm-api-key");
    private final String URL = "https://history.openweathermap.org/data/2.5/history/";

    public OWMApiManager() {
    }

    /**
     * Takes multiple params and build specific URL for downloading data.
     * @param countryCode country code
     * @param cityId city id
     * @param start start timestamp
     * @param end end timestamp
     * @return URL
     * @throws MalformedURLException Malformed URL
     */
    private URL buildUrl(String countryCode, String cityId, Timestamp start, Timestamp end) throws MalformedURLException {
        String url = URL + String.format(
                "city?q=%s,%s&type=hour&start=%d&end=%d&appid=%s",
                cityId,
                countryCode,
                getUnixTime(start),
                getUnixTime(end),
                API_KEY
        );

        return new URL(url);
    }

    /**
     * Takes timestamp and convert it into UnixTime format.
     * @param ts timestamp
     * @return unix time format
     */
    private long getUnixTime(Timestamp ts) {
        return ts.getTime() / 1000L;
    }

    /**
     * Method for downloading data based on inserted params.
     * @param countryCode country code
     * @param cityId city id
     * @param start start timestamp
     * @param end end timestamp
     * @return String content
     * @throws IOException when connection is corrupted
     */
    public String getData(String countryCode, String cityId, Timestamp start, Timestamp end) throws IOException {
        // build url
        URL url = buildUrl(countryCode, cityId, start, end);

        // setup connection and request method
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        try {
            if (con.getResponseCode() == 200) {
                // read data
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // into string
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                return content.toString();
            }

        } finally {
            con.disconnect();
        }

        return null;
    }
}
