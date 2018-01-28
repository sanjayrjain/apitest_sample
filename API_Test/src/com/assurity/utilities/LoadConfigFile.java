/**
 *
 */
/**
 * @author sanjay.jain
 *
 */

package com.assurity.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadConfigFile {

    private static LoadConfigFile instance = null;
    private Properties properties;
    private Properties propertiesLender;

    protected LoadConfigFile() throws IOException{

       properties = new Properties();
       propertiesLender = new Properties();
       String path = new File(System.getProperty("user.dir")+"\\resources\\globalConfig.properties")
		.getAbsolutePath();
        FileInputStream inFile = new FileInputStream(path);
        properties.load(inFile);
    }

    public static LoadConfigFile getInstance() {
        if(instance == null) {
            try {
               synchronized (LoadConfigFile.class) {
                   if ( instance == null){
                       instance = new LoadConfigFile();
                   }
               }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return instance;
    }

    public String getValue(String key) {
        try{
            String value = properties.getProperty(key).trim();
            return value;
        }catch (Exception e){
            return "";
        }
    }
}