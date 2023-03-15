package mydata.studio.service;

import config.AppSettingsState;

/**
 * @author nobody
 */
public class SessionService {
    public static String getCookieValue() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        return String.format("sessionid=%s", appSettingsState.sessionId);
    }

    public static String getSessionId() {
        return AppSettingsState.getInstance().sessionId;
    }
}
