package com.sp.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.sp.Dao.ConfigService;
import com.sp.Model.SystemConfig;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.graphics.Rectangle;

import com.sp.Model.ConstantValue;
import com.sp.Model.Program;
import com.sp.UI.MainFrame;
import com.sp.Utils.ReadXMLFile;
import org.apache.log4j.Logger;


public class SmartConfiguration {
	final static Logger logger = Logger.getLogger(SmartConfiguration.class);
//	private final Properties prop;
//	private final Properties featureToggleProps;

	ConfigService configService;
//	private final List<HashMap<String, String>> batchConfigListMap;

	// Constructor:
	public SmartConfiguration() {
		configService = new ConfigService();
//		this.prop = new Properties();
//		this.featureToggleProps = new Properties();
		loadConfiguration();
//		loadFeatureToggle();
//		this.batchConfigListMap = ReadXMLFile.parse(ConstantValue.CONFIG_BATCH_FILE);

	}

//	public void loadFeatureToggle(){
//		try {
//			featureToggleProps.load(new FileInputStream(ConstantValue.CONFIG_FEATURE_TOGGLE_FILE));
//		} catch (IOException ex){
//			logger.error(ex.getMessage());
//		}
//	}

	/**
	 * Save program configuration.
	 */
//	public void saveConfiguration(){
//		try {
//			FileOutputStream fos = new FileOutputStream(ConstantValue.CONFIG_FILE);
//
//			prop.setProperty("WaitForInitTime", prop.getProperty("WaitForInitTime"));
//			// Main window viewable toolbars:
//			prop.setProperty("ViewUtilitiesBar", String.valueOf(getUtilitiesBarVisible()));
//			prop.setProperty("ViewConnectionBar", String.valueOf(getConnectionBarVisible()));
//			// Putty and Plink paths:
//			prop.setProperty("PuttyExecutable", getPuttyExecutable());
//			prop.setProperty("PlinkExecutable", getPlinkExecutable());
//			prop.setProperty("KeyGeneratorExecutable", getKeyGeneratorExecutable());
//			// Main windows position and size:
//			prop.setProperty("WindowPositionSize", getWindowPositionSizeString());
//
//			prop.storeToXML(fos, "SmartPutty configuration file");
//			fos.close();
//		} catch (FileNotFoundException fex){
//			logger.error(fex.getMessage());
//		} catch (IOException iex){
//			logger.error(iex.getMessage());
//		}
//	}

	/**
	 * Load program configuration.
	 */
	private void loadConfiguration(){
		List<SystemConfig> systemConfigs = configService.loadAllConfig();

	}

	// Get methods:
//	public List<HashMap<String, String>> getBatchConfig() {
//		return this.batchConfigListMap;
//	}

//	public String getWaitForInitTime() {
//		return (String) prop.get("WaitForInitTime");
//	}

	/**
	 * Utilities bar must be visible?
	 * 
	 * @return
	 */
	public Boolean getUtilitiesBarVisible() {
		return configService.getSystemValueBoolean("ViewUtilitiesBar");
	}

	/**
	 * Connection bar must be visible?
	 * 
	 * @return
	 */
	public Boolean getConnectionBarVisible() {
		return configService.getSystemValueBoolean("ViewConnectionBar");
	}
	
	/**
	 * Bottom Quick Bar be Visible  ?
	 * @return
	 */
	public Boolean getBottomQuickBarVisible() {
		return configService.getSystemValueBoolean("ViewBottomQuickBar");
	}

	/**
	 * Get Putty/KiTTY executable path.
	 * 
	 * @return
	 */
	public String getPuttyExecutable() {
		return configService.getSystemValue("PuttyExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PUTTY.getPath() : value;
	}

	/**
	 * Get Plink/Klink executable path.
	 * 
	 * @return
	 */
	public String getPlinkExecutable() {
		return configService.getSystemValue("PlinkExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PLINK.getPath() : value;
	}

	/**
	 * Get key generator executable path.
	 * 
	 * @return
	 */
	public String getKeyGeneratorExecutable() {
		return configService.getSystemValue("KeyGeneratorExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_KEYGEN.getPath() : value;
	}
	
	/**
	 * Get dictionary baseUrl, I put dict.youdao.com as a chines-english dictionary. User can customize it as to his own dict url
	 * @return
	 */
	public String getDictionaryBaseUrl(){
		return configService.getSystemValue("Dictionary");
//		String value = (String) prop.get("Dictionary");
//		return StringUtils.isEmpty(value) ? "http://dict.youdao.com/w/eng/" : value;
	}
	
	/**
	 * user can customize his username, in most case user may using his own username to login multiple linux, so provide a centralized username entry for user
	 * @return
	 */
	public String getDefaultPuttyUsername(){
		return configService.getSystemValue("DefaultPuttyUsername");
//		String value = (String) prop.get("DefaultPuttyUsername");
//		return StringUtils.isEmpty(value) ? "" : value;
	}
	

	/**
	 * get feature toggle config, we can config to enable/disable features by editing config/FeatureToggle.properties
	 * @return
	 */
//	public Properties getFeatureToggleProps() {
//		return featureToggleProps;
//	}
	
	/**
	 * customize win path base prefix when converting path from linux and windows
	 * @return
	 */
	public String getWinPathBaseDrive(){
		return configService.getSystemValue("WindowsBaseDrive");
//		String value = (String) prop.get("WindowsBaseDrive");
//		return StringUtils.isEmpty(value) ? "C:/" : value;
	}
	
	/**
	 * get welcome visible config
	 * @return
	 */
	public Boolean getWelcomePageVisible(){
		return configService.getSystemValueBoolean("ShowWelcomePage");
//		String value = (String) prop.get("ShowWelcomePage");
//		return StringUtils.isEmpty(value) ? true : BooleanUtils.toBoolean(value);
	}

	/**
	 * Get main mindow position and size.
	 * 
	 * @return
	 */
	public Rectangle getWindowPositionSize() {
		// Split comma-separated values by x, y, width, height:
		String[] array = configService.getSystemValue("WindowPositionSize").split(",");
//		String[] array = ((String) prop.get("WindowPositionSize")).split(",");

		// If there aren't enough pieces of information...
		if (array.length < 4) {
			array = new String[4];

			// Set default safety values:
			array[0] = String.valueOf(ConstantValue.SCREEN_WIDTH / 6);
			array[1] = String.valueOf(ConstantValue.SCREEN_HEIGHT / 6);
			array[2] = String.valueOf(2 * ConstantValue.SCREEN_WIDTH / 3);
			array[3] = String.valueOf(2 * ConstantValue.SCREEN_HEIGHT / 3);
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
//	public void setUtilitiesBarVisible(String visible) {
//		prop.setProperty("ViewUtilitiesBar", visible);
//	}

	/**
	 * Set connection bar visible status.
	 * 
	 * @param visible
	 */
//	public void setConnectionBarVisible(String visible) {
//		prop.setProperty("ViewConnectionBar", visible);
//	}
	
//	public void setBottomQuickBarVisible(String visible){
//		prop.setProperty("ViewBottomQuickBar", visible);
//	}

	/**
	 * Set Putty/KiTTY executable path.
	 * 
	 * @param path
	 */
//	public void setPuttyExecutable(String path) {
//		prop.setProperty("PuttyExecutable", path);
//	}

	/**
	 * Set Plink/Klink executable path.
	 * 
	 * @param path
	 */
//	public void setPlinkExecutable(String path) {
//		prop.setProperty("PlinkExecutable", path);
//	}

	/**
	 * Set key generator executable path.
	 * 
	 * @param path
	 */
//	public void setKeyGeneratorExecutable(String path) {
//		prop.setProperty("KeyGeneratorExecutable", path);
//	}

	/**
	 * Set if "Welcome Page" should must be visible on program startup.
	 * 
	 * @param visible
	 */
//	public void setWelcomePageVisible(String visible) {
//		prop.setProperty("ShowWelcomePage", visible);
//	}
}
