package util;

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author nobody
 */
public class HiveSurround implements SurroundDescriptor {
    @Override
    public PsiElement @NotNull [] getElementsToSurround(
            PsiFile file, int startOffset, int endOffset) {
        final PsiElement elementAt = file.findElementAt(startOffset);
        return new PsiElement[]{elementAt};
    }

    @Override
    public Surrounder @NotNull [] getSurrounders() {
        return new Surrounder[]{new HiveTableSurrounder()};
    }

    @Override
    public boolean isExclusive() {
        return false;
    }
}
