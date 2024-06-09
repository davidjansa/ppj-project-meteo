package tul.provisioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import tul.Main;

import javax.sql.DataSource;
import java.util.List;

/**
 * Class for db initialization.
 */
public class Provisioner {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public void doProvision() {

        List<String> allTables;

        allTables = namedParameterJdbcOperations.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM  INFORMATION_SCHEMA.TABLES", String.class);
        if (!allTables.contains("country")) {
            log.warn("DB Provisioner: No tables exist and will be created");

            if (activeProfile.equals("devel")) {
                createDbDevel();
            } else if (activeProfile.equals("test")) {
                createDbTest();
            }

            allTables = namedParameterJdbcOperations.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM  INFORMATION_SCHEMA.TABLES", String.class);
            System.out.println(allTables);
        } else
            log.info("DB Provisioner: Table 'country' exists, all existing tables: " + allTables);
    }

    @Profile("devel")
    public void createDbDevel() {
        Resource rc = new ClassPathResource("create_tables.hsql");
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), rc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Profile("test")
    public void createDbTest() {
        Resource rc = new ClassPathResource("create_tables_insert_data.hsql");
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), rc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}