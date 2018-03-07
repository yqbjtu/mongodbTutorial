package com.yq.mongo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.yq.mongo.common.DBConstants;


public class DBConfig {
    //**************************************************************************
    // CLASS
    //**************************************************************************
    private static final Logger log = Logger.getLogger(DBConfig.class);
    private static DBConfig instance = new DBConfig();
    private Properties prop = new Properties();
    String cfgFileName = "config.properties";

    private DBConfig () {
        readDBConfiguration();
        prop.putIfAbsent(DBConstants.DB_HOST, "127.0.0.1");
        prop.putIfAbsent(DBConstants.DB_PORT, "27017");
        prop.putIfAbsent(DBConstants.DB_NAME, "db1");
    }

    public static DBConfig getInstance() {
        return instance;
    }

    private void readDBConfiguration() {
        FileInputStream in;
        File file = new File(cfgFileName);
        if (!file.exists()) {
            log.warn(file.getAbsolutePath()  + " does not exist.'");
        }
        else {
            try {
                in = new FileInputStream(cfgFileName);
                prop.load(in);
                in.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getConfiguration(String configName) {
        return prop.getProperty(configName);
    }

    public Properties getAllConfiguration() {
        return prop;
    }

}
