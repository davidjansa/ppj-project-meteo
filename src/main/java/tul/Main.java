package tul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import tul.provisioning.Provisioner;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
public class Main {

    @Profile({"devel", "test"})
    @Bean(initMethod = "doProvision")
    public Provisioner provisioner() {
        return new Provisioner();
    }

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    public void init(){
        // set time zone
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Prague"));
    }

}