package match;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static antlr.g4.SqlBaseLexer.*;
import static plugin.basic.HiveTokenTypes.getTokenElementType;

/**
 * @author nobody
 */
public class HiveBraceMatcher implements PairedBraceMatcher {

    @NotNull
    @Override
    public BracePair @NotNull [] getPairs() {
        return new BracePair[]{
                new BracePair(getTokenElementType(LEFT_PAREN), getTokenElementType(RIGHT_PAREN), true),
                new BracePair(getTokenElementType(LEFT_BRACKET), getTokenElementType(RIGHT_BRACKET), true),
                new BracePair(getTokenElementType(SELECT), getTokenElementType(FROM), false),
                new BracePair(getTokenElementType(CASE), getTokenElementType(END), false), new BracePair(getTokenElementType(CASE), getTokenElementType(END), false),
        };
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(
            @NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
