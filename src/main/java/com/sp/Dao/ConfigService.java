package com.sp.Dao;

import com.sp.Model.SystemConfig;
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
}
