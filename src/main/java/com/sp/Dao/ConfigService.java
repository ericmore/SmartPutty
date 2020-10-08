package com.sp.Dao;

import com.sp.Model.SystemConfig;
import org.apache.log4j.Logger;

import java.util.List;

public class ConfigService {
    private final static Logger logger = Logger.getLogger(ConfigService.class);
    private SmartSessionManager smartSessionManager;

    public ConfigService() {
        smartSessionManager = new SmartSessionManager();
    }

    public void initSystemConfig() {

        List<SystemConfig> allSystemConfigs = smartSessionManager.getAllSystemConfigs();
        if (!hasKey(allSystemConfigs, "KeyGeneratorExecutable")) {
            logger.debug("create system config KeyGeneratorExecutable");
            smartSessionManager.save(new SystemConfig("KeyGeneratorExecutable", "app\\putty\\puttygen.exe"));
        }
        if (!hasKey(allSystemConfigs, "PlinkExecutable")) {
            logger.debug("create system config PlinkExecutable");
            smartSessionManager.save(new SystemConfig("PlinkExecutable", "app\\putty\\plink.exe"));
        }
        if (!hasKey(allSystemConfigs, "PuttyExecutable")) {
            logger.debug("create system config PuttyExecutable");
            smartSessionManager.save(new SystemConfig("PuttyExecutable", "app\\putty\\putty.exe"));
        }
        if (!hasKey(allSystemConfigs, "WaitForInitTime")) {
            logger.debug("create system config WaitForInitTime");
            smartSessionManager.save(new SystemConfig("WaitForInitTime", "200"));
        }
        if (!hasKey(allSystemConfigs, "WindowPositionSize")) {
            logger.debug("create system config WindowPositionSize");
            smartSessionManager.save(new SystemConfig("WindowPositionSize", "1914,-6,1933,1053"));
        }
        if (!hasKey(allSystemConfigs, "ViewConnectionBar")) {
            logger.debug("create system config ViewConnectionBar");
            smartSessionManager.save(new SystemConfig("ViewConnectionBar", "true"));
        }
        if (!hasKey(allSystemConfigs, "ViewBottomQuickBar")) {
            logger.debug("create system config ViewBottomQuickBar");
            smartSessionManager.save(new SystemConfig("ViewBottomQuickBar", "true"));
        }
        if (!hasKey(allSystemConfigs, "ShowWelcomePage")) {
            logger.debug("create system config ShowWelcomePage");
            smartSessionManager.save(new SystemConfig("ShowWelcomePage", "false"));
        }
        if (!hasKey(allSystemConfigs, "Dictionary")) {
            logger.debug("create system config Dictionary");
            smartSessionManager.save(new SystemConfig("Dictionary", "http://dict.youdao.com/w/eng/"));
        }
        if (!hasKey(allSystemConfigs, "WindowsBaseDrive")) {
            logger.debug("create system config WindowsBaseDrive");
            smartSessionManager.save(new SystemConfig("WindowsBaseDrive", "c:"));
        }
        if (!hasKey(allSystemConfigs, "ViewUtilitiesBar")) {
            logger.debug("create system config ViewUtilitiesBar");
            smartSessionManager.save(new SystemConfig("ViewUtilitiesBar", "true"));
        }
        if (!hasKey(allSystemConfigs, "DefaultPuttyUsername")) {
            logger.debug("create system config DefaultPuttyUsername");
            smartSessionManager.save(new SystemConfig("DefaultPuttyUsername", "cstoverock"));
        }

    }

    public List<SystemConfig> loadAllConfig(){
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

    public String getSystemValue(String key){
        SystemConfig conf = smartSessionManager.getAllSystemConfigs().stream().filter(e->e.getKey().equals(key)).findAny().orElse(null);
        return conf.getValue();
    }

    public Boolean getSystemValueBoolean(String key){
        String val = getSystemValue(key);
        return Boolean.valueOf(val);
    }
}
