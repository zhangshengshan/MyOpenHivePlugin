package plugin;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author nobody
 */
public class HiveFile extends PsiFileBase {
    protected HiveFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, HiveLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return HiveFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Hive File";
    }
}
