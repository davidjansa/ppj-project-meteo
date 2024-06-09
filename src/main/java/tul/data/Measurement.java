package tul.data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
    private int id;

    @ManyToOne
    @JoinColumn(name="fk_city_id")
    private City city;

    @Column(name="mtime")
    private Timestamp mtime;

    @Column(name="temp")
    private float temp;

    @Column(name="pressure")
    private float pressure;

    @Column(name="humidity")
    private float humidity;

    public Measurement() {

    }

    public Measurement(City city, Timestamp mtime, float temp, float pressure, float humidity) {
        this.city = city;
        this.mtime = mtime;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Measurement(int id, City city, Timestamp mTime, float temp, float pressure, float humidity) {
        this.id = id;
        this.city = city;
        this.mtime = mTime;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Timestamp getmMtime() {
        return mtime;
    }

    public void setmMtime(Timestamp mTime) {
        this.mtime = mTime;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

}
