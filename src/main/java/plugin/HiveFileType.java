package plugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author nobody
 */
public class HiveFileType extends LanguageFileType {

    public static final HiveFileType INSTANCE = new HiveFileType();

    private HiveFileType() {
        super(HiveLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Hive File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Hive language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "hql";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return HiveIcons.FILE;
    }
}
