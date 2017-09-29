package Model;

import Control.SmartPuttyVersion;

import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Paths;

public class ConstantValue {
	public final static String mainWindowTitle = "Smart Putty";
	public final static String mainWindowVersion = SmartPuttyVersion.getSmartPuttyVersion();
	
	public final static String HOME_URL = System.getProperty("user.dir")+File.separator+"doc"+File.separator+"index.htm";
	public final static String defaultProtocol = "ssh";
//	public final static String DICT_URL_BASE = "http://m.iciba.com/";
	// Screen sizes:
	public final static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height; 
	public final static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	// XML configuration file to use:
	public final static String CONFIG_FILE = Paths.get("config" ,"Configuration.xml").toString();
	public final static String CONFIG_BATCH_FILE = Paths.get("config" ,"BatchConfig.xml").toString();
	public final static String CONFIG_FEATURE_TOGGLE_FILE = Paths.get("config" ,"FeatureToggle.properties").toString();
	
	public static enum ConfigSessionTypeEnum {
		SMART_PUTTY_SESSION,
		PURE_PUTTY_SESSION
	}

}

