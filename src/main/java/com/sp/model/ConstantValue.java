package com.sp.model;

import com.sp.control.SmartPuttyVersion;

import java.awt.Toolkit;
import java.io.File;

public class ConstantValue {
    public static String MAIN_WINDOW_TITLE = "Smart Putty";
    public static String MAIN_WINDOW_VERSION = SmartPuttyVersion.getSmartPuttyVersion();

    public static String HOME_URL = System.getProperty("user.dir") + File.separator + "doc" + File.separator + "index.mht";
    public static String DEFAULT_PROTOCOL = "ssh";
    //	public static String DICT_URL_BASE = "http://m.iciba.com/";
    // Screen sizes:
    public static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;


    public static int PURE_PUTTY_SESSION = 1;
    public static int SMART_PUTTY_SESSION = 2;



}

