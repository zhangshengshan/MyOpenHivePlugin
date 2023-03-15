package codestyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class HiveCodeStyleSettings extends CustomCodeStyleSettings {

    protected HiveCodeStyleSettings(@NonNls @NotNull String tagName, CodeStyleSettings container) {
        super("HiveCodeStyle", container);
    }
}
