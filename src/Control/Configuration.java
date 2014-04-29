package Control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Configuration {

	private static Configuration conf = null;
	private Properties prop = new Properties();
	private Configuration() {
		loadConfiguration();
	}

	public static Configuration getInstance() {
		if (conf == null)
			conf = new Configuration();
		return conf;
	}

//	private void saveConfiguration() {
//		try {
//			Properties prop = new Properties();
//			prop.setProperty("Timeout", "10000");
//			prop.setProperty("WaitForInitTime", "1500");
//			prop.setProperty("ProxyHost", "tipl01.swg.usma.ibm.com");
//			prop.setProperty("ProxyUser", "perfadmin");
//			prop.setProperty("ProxyPassword", "tipadmin");
//			prop.setProperty("ProxyPort", "7890");
//			FileOutputStream fos = new FileOutputStream("Configuration.xml");
//			prop.storeToXML(fos,"configuration file");
//			fos.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void loadConfiguration() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("Configuration.xml");
			prop.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prop.list(System.out);
	}

	public String getTimeout() {
		return (String) prop.get("Timeout");
	}

	public String getWaitForInitTime() {
		return (String) prop.get("WaitForInitTime");
	}

	public String getProxyHost() {
		return (String) prop.get("ProxyHost");
	}

	public String getProxyUser() {
		return (String) prop.get("ProxyUser");
	}

	public String getProxyPort() {
		return (String) prop.get("ProxyPort");
	}

	public String getProxyPassword() {
		return (String) prop.get("ProxyPassword");
	}

}
