package com.timr.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyManager {
  static final Logger logger = Logger.getLogger(PropertyManager.class);

  private static Properties props = null;
  private String filename;

  public PropertyManager(String filename) {
    this.filename = filename;
  }

  public String getProperty(String key) {
    String value = System.getProperty(key);
    if (props == null) {
      props = new Properties();
      try {
        InputStream fis = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(filename);
        props.load(fis);
      } catch (FileNotFoundException fne) {
        logger.error(fne.getMessage());
        fne.printStackTrace();
      } catch (IOException ie) {
        logger.error(ie.getMessage());
        ie.printStackTrace();
      }
    }

    if (value == null) {
      value = props.getProperty(key);
    }
    
    logger.debug("key: " + key + " value: " + (key.equalsIgnoreCase("password")? "*****" : value));
    return value;
  }

  /**
   * Insert or update an existing property, such as the browser
   * @param key
   * @param value
   */
  public void setProperty(String key, String value) {
    if (props == null) {
      props = new Properties();
      try {
        InputStream fis = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(filename);
        props.load(fis);
      } catch (FileNotFoundException fne) {
        logger.error(fne.getMessage());
        fne.printStackTrace();
      } catch (IOException ie) {
        logger.error(ie.getMessage());
        ie.printStackTrace();
      }
    }
    props.setProperty(key,value);
    logger.trace("key: " + key + " value: " + value);
    return;
  }

}
