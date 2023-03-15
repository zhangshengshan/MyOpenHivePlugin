package codestyle; // Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this
// source code is governed by the Apache 2.0 license that can be found in the
// LICENSE file.

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import plugin.HiveLanguage;

public class HiveLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return HiveLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(
            @NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS");
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
        } else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showStandardOptions("RIGHT_MARGIN");
        } else if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions("INDENT_SIZE", "TAB_SIZE");
        }
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return "SELECT\n"
                + "a AS a,\n"
                + "b    AS b\n"
                + "\n"
                + "FROM\n"
                + "a.b\n"
                + "WHERE\n"
                + "id IS NOT NULL\n";
    }
}
