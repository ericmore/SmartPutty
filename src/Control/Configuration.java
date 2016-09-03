package Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.graphics.Rectangle;

import Model.ConstantValue;
import Model.Program;
import UI.MainFrame;
import Utils.ReadXMLFile;

public class Configuration {
	private final Properties prop;
	private final List<HashMap<String, String>> batchConfigListMap;

	// Constructor:
	public Configuration() {
		this.prop = new Properties();
		loadConfiguration();
		this.batchConfigListMap = ReadXMLFile.parse(ConstantValue.CONFIG_BATCH_FILE);
	}

	/**
	 * Save program configuration.
	 */
	public void saveConfiguration() {
		try {
			FileOutputStream fos = new FileOutputStream(ConstantValue.CONFIG_FILE);

			prop.setProperty("WaitForInitTime", prop.getProperty("WaitForInitTime"));
			// Main window viewable toolbars:
			prop.setProperty("ViewUtilitiesBar", String.valueOf(getUtilitiesBarVisible()));
			prop.setProperty("ViewConnectionBar", String.valueOf(getConnectionBarVisible()));
			// Putty and Plink paths:
			prop.setProperty("PuttyExecutable", getPuttyExecutable());
			prop.setProperty("PlinkExecutable", getPlinkExecutable());
			prop.setProperty("KeyGeneratorExecutable", getKeyGeneratorExecutable());
			// Main windows position and size:
			prop.setProperty("WindowPositionSize", getWindowPositionSizeString());

			prop.storeToXML(fos, "SmartPutty configuration file");
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load program configuration.
	 */
	private void loadConfiguration() {
		try {
			FileInputStream fis = new FileInputStream(ConstantValue.CONFIG_FILE);
			prop.loadFromXML(fis);
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// prop.list(System.out); //DEBUG
	}

	// Get methods:
	public List<HashMap<String, String>> getBatchConfig() {
		return this.batchConfigListMap;
	}

	public String getWaitForInitTime() {
		return (String) prop.get("WaitForInitTime");
	}

	/**
	 * Utilities bar must be visible?
	 * 
	 * @return
	 */
	public Boolean getUtilitiesBarVisible() {
		String value = (String) prop.get("ViewUtilitiesBar");
		return StringUtils.isEmpty(value) ? true : BooleanUtils.toBoolean(value);
	}

	/**
	 * Connection bar must be visible?
	 * 
	 * @return
	 */
	public Boolean getConnectionBarVisible() {
		String value = (String) prop.get("ViewConnectionBar");
		return StringUtils.isEmpty(value) ? true : BooleanUtils.toBoolean(value);
	}

	/**
	 * Get Putty/KiTTY executable path.
	 * 
	 * @return
	 */
	public String getPuttyExecutable() {
		String value = (String) prop.get("PuttyExecutable");
		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PUTTY.getPath() : value;
	}

	/**
	 * Get Plink/Klink executable path.
	 * 
	 * @return
	 */
	public String getPlinkExecutable() {
		String value = (String) prop.get("PlinkExecutable");
		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PLINK.getPath() : value;
	}

	/**
	 * Get key generator executable path.
	 * 
	 * @return
	 */
	public String getKeyGeneratorExecutable() {
		String value = (String) prop.get("KeyGeneratorExecutable");
		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_KEYGEN.getPath() : value;
	}


	/**
	 * Get main mindow position and size.
	 * 
	 * @return
	 */
	public Rectangle getWindowPositionSize() {
		// Split comma-separated values by x, y, width, height:
		String[] array = ((String) prop.get("WindowPositionSize")).split(",");

		// If there aren't enough pieces of information...
		if (array.length < 4) {
			array = new String[4];

			// Set default safety values:
			array[0] = String.valueOf(ConstantValue.screenWidth / 6);
			array[1] = String.valueOf(ConstantValue.screenHeight / 6);
			array[2] = String.valueOf(2 * ConstantValue.screenWidth / 3);
			array[3] = String.valueOf(2 * ConstantValue.screenHeight / 3);
		}

		return new Rectangle(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]),
				Integer.parseInt(array[3]));
	}

	/**
	 * Get main mindow position and size in String format.
	 * 
	 * @return
	 */
	public String getWindowPositionSizeString() {
		String x = String.valueOf(MainFrame.shell.getBounds().x);
		String y = String.valueOf(MainFrame.shell.getBounds().y);
		String width = String.valueOf(MainFrame.shell.getBounds().width);
		String height = String.valueOf(MainFrame.shell.getBounds().height);

		return x + "," + y + "," + width + "," + height;
	}

	/**
	 * Set utilities bar visible status.
	 * 
	 * @param visible
	 */
	public void setUtilitiesBarVisible(String visible) {
		prop.setProperty("ViewUtilitiesBar", visible);
	}

	/**
	 * Set connection bar visible status.
	 * 
	 * @param visible
	 */
	public void setConnectionBarVisible(String visible) {
		prop.setProperty("ViewConnectionBar", visible);
	}

	/**
	 * Set Putty/KiTTY executable path.
	 * 
	 * @param path
	 */
	public void setPuttyExecutable(String path) {
		prop.setProperty("PuttyExecutable", path);
	}

	/**
	 * Set Plink/Klink executable path.
	 * 
	 * @param path
	 */
	public void setPlinkExecutable(String path) {
		prop.setProperty("PlinkExecutable", path);
	}

	/**
	 * Set key generator executable path.
	 * 
	 * @param path
	 */
	public void setKeyGeneratorExecutable(String path) {
		prop.setProperty("KeyGeneratorExecutable", path);
	}

	/**
	 * Set if "Welcome Page" should must be visible on program startup.
	 * 
	 * @param visible
	 */
	public void setWelcomePageVisible(String visible) {
		prop.setProperty("ShowWelcomePage", visible);
	}
}
