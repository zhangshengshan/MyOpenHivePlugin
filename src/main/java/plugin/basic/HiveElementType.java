package plugin.basic;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import plugin.HiveLanguage;

public class HiveElementType extends IElementType {
    public HiveElementType(@NonNls @NotNull String debugName) {
        super(debugName, HiveLanguage.INSTANCE);
    }
}
