package icu.wwj.jmh.jdbc.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class Jdbcs {
    
    private Jdbcs() {
        throw new RuntimeException();
    }
    
    public static Connection getConnection() {
        InputStream in = null;
        String profile = System.getProperty("profile");
        String configurationFile = System.getProperty("conf");
        try {
            in = null == profile || profile.trim().isEmpty() ? new FileInputStream(configurationFile) : Jdbcs.class.getResourceAsStream(String.format("/META-INF/profiles/%s.properties", profile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Fail to get profile %s or conf %s", profile, configurationFile), e);
        }
        return getConnection(in);
    }
    
    public static Connection getConnection(final InputStream in) {
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            return DriverManager.getConnection(props.getProperty("jdbc-url"), props.getProperty("username"), props.getProperty("password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
