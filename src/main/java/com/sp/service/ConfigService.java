package com.sp.service;

import com.sp.dao.SmartSessionManager;
import com.sp.model.ConstantValue;
import com.sp.entity.SystemConfig;
import com.sp.ui.MainFrame;
import org.eclipse.swt.graphics.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigService {
    private static final Logger logger= LoggerFactory.getLogger(ConfigService.class);
    public static final String KEYGENERATOREXECUTABLE = "KeyGeneratorExecutable";
    public static final String PLINKEXECUTABLE = "PlinkExecutable";
    public static final String PUTTYEXECUTABLE = "PuttyExecutable";
    public static final String WAITFORINITTIME = "WaitForInitTime";
    public static final String WINDOWPOSITIONSIZE = "WindowPositionSize";
    public static final String VIEWCONNECTIONBAR = "ViewConnectionBar";
    public static final String VIEWBOTTOMQUICKBAR = "ViewBottomQuickBar";
    public static final String SHOWWELCOMEPAGE = "ShowWelcomePage";
    public static final String DICTIONARY = "Dictionary";
    public static final String WINDOWSBASEDRIVE = "WindowsBaseDrive";
    public static final String VIEWUTILITIESBAR = "ViewUtilitiesBar";
    public static final String DEFAULTPUTTYUSERNAME = "DefaultPuttyUsername";


    private SmartSessionManager smartSessionManager;

    public ConfigService() {
        smartSessionManager = new SmartSessionManager();
    }

    public void initSystemConfig() {

        List<SystemConfig> allSystemConfigs = smartSessionManager.getAllSystemConfigs();
        if (!hasKey(allSystemConfigs, KEYGENERATOREXECUTABLE)) {
            logger.debug("create system config" + KEYGENERATOREXECUTABLE);
            smartSessionManager.save(new SystemConfig(KEYGENERATOREXECUTABLE, "app\\putty\\puttygen.exe"));
        }
        if (!hasKey(allSystemConfigs, PLINKEXECUTABLE)) {
            logger.debug("create system config " + PLINKEXECUTABLE);
            smartSessionManager.save(new SystemConfig(PLINKEXECUTABLE, "app\\putty\\plink.exe"));
        }
        if (!hasKey(allSystemConfigs, PUTTYEXECUTABLE)) {
            logger.debug("create system config " + PUTTYEXECUTABLE);
            smartSessionManager.save(new SystemConfig(PUTTYEXECUTABLE, "app\\putty\\putty.exe"));
        }
        if (!hasKey(allSystemConfigs, WAITFORINITTIME)) {
            logger.debug("create system config " + WAITFORINITTIME);
            smartSessionManager.save(new SystemConfig(WAITFORINITTIME, "200"));
        }
        if (!hasKey(allSystemConfigs, WINDOWPOSITIONSIZE)) {
            logger.debug("create system config " + WINDOWPOSITIONSIZE);
            smartSessionManager.save(new SystemConfig(WINDOWPOSITIONSIZE, "1914,-6,1933,1053"));
        }
        if (!hasKey(allSystemConfigs, VIEWCONNECTIONBAR)) {
            logger.debug("create system config " + VIEWCONNECTIONBAR);
            smartSessionManager.save(new SystemConfig(VIEWCONNECTIONBAR, "true"));
        }
        if (!hasKey(allSystemConfigs, VIEWBOTTOMQUICKBAR)) {
            logger.debug("create system config " + VIEWBOTTOMQUICKBAR);
            smartSessionManager.save(new SystemConfig(VIEWBOTTOMQUICKBAR, "true"));
        }
        if (!hasKey(allSystemConfigs, SHOWWELCOMEPAGE)) {
            logger.debug("create system config " + SHOWWELCOMEPAGE);
            smartSessionManager.save(new SystemConfig(SHOWWELCOMEPAGE, "false"));
        }
        if (!hasKey(allSystemConfigs, DICTIONARY)) {
            logger.debug("create system config " + DICTIONARY);
            smartSessionManager.save(new SystemConfig(DICTIONARY, "http://dict.youdao.com/w/eng/"));
        }
        if (!hasKey(allSystemConfigs, WINDOWSBASEDRIVE)) {
            logger.debug("create system config " + WINDOWSBASEDRIVE);
            smartSessionManager.save(new SystemConfig(WINDOWSBASEDRIVE, "c:"));
        }
        if (!hasKey(allSystemConfigs, VIEWUTILITIESBAR)) {
            logger.debug("create system config " + VIEWUTILITIESBAR);
            smartSessionManager.save(new SystemConfig(VIEWUTILITIESBAR, "true"));
        }
        if (!hasKey(allSystemConfigs, DEFAULTPUTTYUSERNAME)) {
            logger.debug("create system config " + DEFAULTPUTTYUSERNAME);
            smartSessionManager.save(new SystemConfig(DEFAULTPUTTYUSERNAME, "cstoverock"));
        }

    }

    public List<SystemConfig> loadAllConfig() {
        return smartSessionManager.getAllSystemConfigs();
    }

    public boolean hasKey(List<SystemConfig> list, String key) {
        for (SystemConfig config : list) {
            if (config.getKey().equals(key)) {
                return true;
            }
        }

        return false;
    }

    public String getSystemValue(String key) {
        SystemConfig conf = smartSessionManager.getAllSystemConfigs().stream().filter(e -> e.getKey().equals(key)).findAny().orElse(null);
        return conf.getValue();
    }

    public Boolean getSystemValueBoolean(String key) {
        String val = getSystemValue(key);
        return Boolean.valueOf(val);
    }

    /**
     * Utilities bar must be visible?
     *
     * @return
     */
    public Boolean getUtilitiesBarVisible() {
        return getSystemValueBoolean("ViewUtilitiesBar");
    }

    /**
     * Connection bar must be visible?
     *
     * @return
     */
    public Boolean getConnectionBarVisible() {
        return getSystemValueBoolean("ViewConnectionBar");
    }

    /**
     * Bottom Quick Bar be Visible  ?
     * @return
     */
    public Boolean getBottomQuickBarVisible() {
        return getSystemValueBoolean("ViewBottomQuickBar");
    }

    /**
     * Get Putty/KiTTY executable path.
     *
     * @return
     */
    public String getPuttyExecutable() {
        return getSystemValue("PuttyExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PUTTY.getPath() : value;
    }

    /**
     * Get Plink/Klink executable path.
     *
     * @return
     */
    public String getPlinkExecutable() {
        return getSystemValue("PlinkExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PLINK.getPath() : value;
    }

    /**
     * Get key generator executable path.
     *
     * @return
     */
    public String getKeyGeneratorExecutable() {
        return getSystemValue("KeyGeneratorExecutable");
//		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_KEYGEN.getPath() : value;
    }

    /**
     * Get dictionary baseUrl, I put dict.youdao.com as a chines-english dictionary. User can customize it as to his own dict url
     * @return
     */
    public String getDictionaryBaseUrl(){
        return getSystemValue("Dictionary");
    }

    /**
     * user can customize his username, in most case user may using his own username to login multiple linux, so provide a centralized username entry for user
     * @return
     */
    public String getDefaultPuttyUsername(){
        return getSystemValue("DefaultPuttyUsername");
    }



    /**
     * customize win path base prefix when converting path from linux and windows
     * @return
     */
    public String getWinPathBaseDrive(){
        return getSystemValue("WindowsBaseDrive");
    }

    /**
     * get welcome visible config
     * @return
     */
    public Boolean getWelcomePageVisible(){
        return getSystemValueBoolean("ShowWelcomePage");
    }

    /**
     * Get main mindow position and size.
     *
     * @return
     */
    public Rectangle getWindowPositionSize() {
        // Split comma-separated values by x, y, width, height:
        String[] array = getSystemValue("WindowPositionSize").split(",");
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
}
