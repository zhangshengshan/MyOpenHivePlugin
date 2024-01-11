package config;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 * @Author zhangshengshan
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField myUserNameText = new JBTextField();
    private final JBCheckBox myIdeaUserStatus = new JBCheckBox("Do you use IntelliJ IDEA? ");
    private final JBTextField sessionId = new JBTextField();
    private final JBCheckBox columnChoose = new JBCheckBox("Input Or Choose from List?");
    private final JColorChooser colorChooser = new JColorChooser();
    //  private final JBColor colorChooser = new JBColor(0xF90202, 999999);

    public AppSettingsComponent() {
        myMainPanel =
                FormBuilder.createFormBuilder()
                        .addLabeledComponent(new JBLabel("Enter user name: "), myUserNameText, 1, false)
                        .addLabeledComponent(new JBLabel("Enter you sessionId : "), sessionId, 1, false)
                        .addComponent(myIdeaUserStatus, 1)
                        .addComponent(columnChoose, 1)
                        .addComponent(colorChooser, 1)
                        .addComponentFillVertically(new JPanel(), 0)
                        .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myUserNameText;
    }

    @NotNull
    public String getUserNameText() {
        return myUserNameText.getText();
    }

    public void setUserNameText(@NotNull String newText) {
        myUserNameText.setText(newText);
    }

    @NotNull
    public String getSessionId() {
        return sessionId.getText();
    }

    public void setSessionId(String sessionId) {
        this.sessionId.setText(sessionId);
    }

    public boolean getColumnChoose() {
        return columnChoose.isSelected();
    }

    public void setColumnChoose(boolean columnChoose) {
        this.columnChoose.setSelected(columnChoose);
    }

    public boolean getIdeaUserStatus() {
        return myIdeaUserStatus.isSelected();
    }

    public void setIdeaUserStatus(boolean newStatus) {
        myIdeaUserStatus.setSelected(newStatus);
    }
}
