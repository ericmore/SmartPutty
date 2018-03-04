package Model;

import Control.SmartPuttyVersion;

import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ConstantValue {
	public final static String MAIN_WINDOW_TITLE = "Smart Putty";
	public final static String MAIN_WINDOW_VERSION = SmartPuttyVersion.getSmartPuttyVersion();
	
	public final static String HOME_URL = System.getProperty("user.dir")+File.separator+"doc"+File.separator+"index.mht";
	public final static String DEFAULT_PROTOCOL = "ssh";
//	public final static String DICT_URL_BASE = "http://m.iciba.com/";
	// Screen sizes:
	public final static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; 
	public final static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	// XML configuration file to use:
	public final static String CONFIG_FILE = Paths.get("config" ,"Configuration.xml").toString();
	public final static String CONFIG_BATCH_FILE = Paths.get("config" ,"BatchConfig.xml").toString();
	public final static String CONFIG_FEATURE_TOGGLE_FILE = Paths.get("config" ,"FeatureToggle.properties").toString();
	
	public static class ConfigSessionTypeEnum {
		public static String SMART_PUTTY_SESSION = "SMART_PUTTY_SESSION";
		public static String PURE_PUTTY_SESSION = "PURE_PUTTY_SESSION";
		public static String DIRECT_CONNECTION_SESSION = "DIRECT_CONNECTION_SESSION"; //for directly connection purpose, not from DB query, like connection bar for temp connection.
	}

    public static String MODE=System.getenv("MODE");


    /**
     * 	SSH2 ("SSH2", "-ssh -2"),
     SSH ("SSH", "-ssh -1"),
     TELNET ("Telnet", "-telnet"),
     RLOGIN ("Rlogin", "-rlogin"),
     RAW ("Raw", "-raw"),
     SERIAL ("Serial", "-serial");
     */
    public static Map<String, String> protocalKV = new TreeMap<String, String>() {{
        put("SSH2","-ssh -2");
        put("SSH","-ssh -1");
        put("Telnet","-telnet");
        put("Rlogin","-rlogin");
        put("Raw","-raw");
        put("Serial","-serial");
    }};

}

