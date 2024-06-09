package tul.data;

import javax.persistence.*;

@Entity
@Table(name="city")
public class City {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name="fk_country_code")
    private Country country;

    @Column(name="name")
    private String name;

    public City() {
        this.country = new Country();
    }

    public City(String id, Country country, String name) {
        this.id = id;
        this.country = country;
        this.name = name;
    }

    public City(Country country, String name) {
        this.country = country;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
