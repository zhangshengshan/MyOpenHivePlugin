package plugin.basic;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import plugin.HiveLanguage;

/**
 * @author nobody
 */
public class HiveTokenType extends IElementType {
    public HiveTokenType(@NotNull String debugName) {
        super(debugName, HiveLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "HiveTokenType." + super.toString();
    }
}
