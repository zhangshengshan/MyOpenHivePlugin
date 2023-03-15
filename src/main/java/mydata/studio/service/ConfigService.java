package mydata.studio.service;

import config.AppSettingsState;

public class ConfigService {
    public static boolean getHierachyColumnChooseStyle() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        return appSettingsState.columnChoose;
    }
}
