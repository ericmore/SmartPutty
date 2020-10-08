package com.sp.Model;

import com.sp.Control.SmartPuttyVersion;
import org.springframework.core.io.ClassPathResource;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConstantValue {
    public static String MAIN_WINDOW_TITLE = "Smart Putty";
    public static String MAIN_WINDOW_VERSION = SmartPuttyVersion.getSmartPuttyVersion();

    public static String HOME_URL = System.getProperty("user.dir") + File.separator + "doc" + File.separator + "index.mht";
    public static String DEFAULT_PROTOCOL = "ssh";
    //	public static String DICT_URL_BASE = "http://m.iciba.com/";
    // Screen sizes:
    public static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    // XML configuration file to use:
    public static File CONFIG_FILE;
    public static File CONFIG_BATCH_FILE;
    public static File CONFIG_FEATURE_TOGGLE_FILE;
    static {
        try {
            CONFIG_FILE = new ClassPathResource("config/Configuration.xml").getFile();
            CONFIG_BATCH_FILE = new ClassPathResource("config/BatchConfig.xml").getFile();
            CONFIG_FEATURE_TOGGLE_FILE = new ClassPathResource("config/FeatureToggle.properties").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static enum ConfigSessionTypeEnum {
        SMART_PUTTY_SESSION,
        PURE_PUTTY_SESSION
    }

    public ConstantValue() {


    }

}

