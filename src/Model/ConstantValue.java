package Model;

import java.awt.Toolkit;
import java.io.File;

public class ConstantValue {
	public final static String mainWindowTitle = "Smart Putty";
	public final static String HOME_URL = System.getProperty("user.dir")+File.separator+"doc"+File.separator+"index.htm";
	public final static String baseUrlBaidu = "http://www.baidu.com/";
	public final static String baseUrlGoogle = "https://www.google.com/";
	public final static String defaultProtocol = "ssh";
	
	public final static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height; 
	public final static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
}
