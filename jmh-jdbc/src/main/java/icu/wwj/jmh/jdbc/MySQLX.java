package icu.wwj.jmh.jdbc;

import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MySQLX {
    
    private static final SessionFactory SESSION_FACTORY = new SessionFactory();
    
    public static Session getSession() {
        InputStream in;
        String profile = System.getProperty("profile");
        String configurationFile = System.getProperty("conf");
        try {
            in = null == profile || profile.trim().isEmpty() ? new FileInputStream(configurationFile) : Jdbcs.class.getResourceAsStream(String.format("/META-INF/profiles/%s.properties", profile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Fail to get profile %s or conf %s", profile, configurationFile), e);
        }
        return getSession(in);
    }
    
    private static Session getSession(final InputStream in) {
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            return SESSION_FACTORY.getSession(props.getProperty("url"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
