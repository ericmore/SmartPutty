package Control;

import UI.MainFrame;
import static UI.MainFrame.shell;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class Configuration {
	// XML configuration file to use:
	private static final String CONFIG_FILE = "Configuration.xml";

	private final Properties prop;
	private final MainFrame mainFrame;

	// Constructor:
	public Configuration(MainFrame mainFrame){
		this.mainFrame = mainFrame;

		prop = new Properties();
		loadConfiguration();
	}

	/**
	 * Save program configuration.
	 */
	public void saveConfiguration(){
		try {
			FileOutputStream fos = new FileOutputStream(CONFIG_FILE);

			// Proxy settings:
			prop.setProperty("Timeout", "10000");
			prop.setProperty("WaitForInitTime", "1500");
			prop.setProperty("ProxyHost", "tipl01.swg.usma.ibm.com");
			prop.setProperty("ProxyUser", "perfadmin");
			prop.setProperty("ProxyPassword", "tipadmin");
			prop.setProperty("ProxyPort", "7890");
			// TODO: this can be moved to specific methods!
			// Main window view toolbars:
			prop.setProperty("ViewUtilitiesBar", String.valueOf(mainFrame.utilitiesBarMenuItem.getSelection()));
			prop.setProperty("ViewConnectionBar", String.valueOf(mainFrame.connectionBarMenuItem.getSelection()));
			// Putty and Plink paths:
			prop.setProperty("PuttyExecutable", "app\\putty\\putty.exe");
			prop.setProperty("PlinkExecutable", "app\\putty\\plink.exe");

			prop.storeToXML(fos, "SmartPutty configuration file");
			fos.close();
		} catch (FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load program configuration.
	 */
	private void loadConfiguration(){
		try {
			FileInputStream fis = new FileInputStream(CONFIG_FILE);
			prop.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// prop.list(System.out); //DEBUG
	}

	// Get methods: ////////////////////////////////////////////////////////
	public String getTimeout(){
		return (String) prop.get("Timeout");
	}

	public String getWaitForInitTime(){
		return (String) prop.get("WaitForInitTime");
	}

	public String getProxyHost(){
		return (String) prop.get("ProxyHost");
	}

	public String getProxyUser(){
		return (String) prop.get("ProxyUser");
	}

	public String getProxyPort(){
		return (String) prop.get("ProxyPort");
	}

	public String getProxyPassword(){
		return (String) prop.get("ProxyPassword");
	}

	/**
	 * Utilities bar must be visible?
	 * @return 
	 */
	public Boolean getUtilitiesVisible(){
		return Boolean.valueOf((String) prop.get("ViewUtilitiesBar"));
	}

	/**
	 * Connection bar must be visible?
	 * @return 
	 */
	public Boolean getConnectionVisible(){
		return Boolean.valueOf((String) prop.get("ViewConnectionBar"));
	}

	/**
	 * Get Putty/KiTTY executable path.
	 * @return 
	 */
	public String getPuttyExecutable(){
		String value = (String) prop.get("PuttyExecutable");

		if (value == null){
			showMessageEmptyValue("PuttyExecutable");
			value = ""; // Put any value to avoid "NullPointerException" error.
		}

		return value;
	}

	/**
	 * Get Plink/Klink executable path.
	 * @return 
	 */
	public String getPlinkExecutable(){
		String value = (String) prop.get("PlinkExecutable");

		if (value == null){
			showMessageEmptyValue("PlinkExecutable");
			value = ""; // Put any value to avoid "NullPointerException" error.
		}

		return value;
	}

	/**
	 * Get key generator executable path.
	 * @return 
	 */
	public String getKeyGeneratorExecutable(){
		String value = (String) prop.get("KeyGeneratorExecutable");

		if (value == null){
			showMessageEmptyValue("KeyGeneratorExecutable");
			value = ""; // Put any value to avoid "NullPointerException" error.
		}

		return value;
	}

	// Set methods: ////////////////////////////////////////////////////////
	/**
	 * Set Putty/KiTTY executable path.
	 * @param path 
	 */
	public void setPuttyExecutable(String path){
		prop.setProperty("PuttyExecutable", path);
	}

	/**
	 * Set Plink/Klink executable path.
	 * @param path 
	 */
	public void setPlinkExecutable(String path){
		prop.setProperty("PlinkExecutable", path);
	}

	/**
	 * Set key generator executable path.
	 * @param path 
	 */
	public void setKeyGeneratorExecutable(String path){
		prop.setProperty("KeyGeneratorExecutable", path);
	}

	// Other methods: ////////////////////////////////////////////////////////

	/**
	 * Shows an error message if a vital parameter is null.
	 * Usefull to avoid avoid "NullPointerException" errors which can close main program.
	 */
	private void showMessageEmptyValue(String parameter){
		MessageBox messagebox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		messagebox.setText("Configuration error");
		messagebox.setMessage("Seems \"" + parameter + "\" property isn't defined!\nPlease, check configuration file.");
		messagebox.open();
		System.err.println("Error - Missing configuration property: " + parameter);
	}
}
