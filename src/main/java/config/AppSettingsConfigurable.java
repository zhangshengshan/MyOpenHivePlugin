package config;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 * @author zhangshengshan
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "MyDataStudio";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getUserNameText().equals(settings.userId);
        modified |= mySettingsComponent.getIdeaUserStatus() != settings.ideaStatus;
        modified |= !mySettingsComponent.getSessionId().equals(settings.sessionId);
        modified |= mySettingsComponent.getColumnChoose() != settings.columnChoose;
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.userId = mySettingsComponent.getUserNameText();
        settings.ideaStatus = mySettingsComponent.getIdeaUserStatus();
        settings.sessionId = mySettingsComponent.getSessionId();
        settings.columnChoose = mySettingsComponent.getColumnChoose();
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setUserNameText(settings.userId);
        mySettingsComponent.setIdeaUserStatus(settings.ideaStatus);
        mySettingsComponent.setSessionId(settings.sessionId);
        mySettingsComponent.setColumnChoose(settings.columnChoose);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
