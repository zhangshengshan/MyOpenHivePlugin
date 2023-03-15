package structview;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author nobody
 */
public class HiveItemPresentation implements ItemPresentation {
    protected final PsiElement element;

    protected HiveItemPresentation(PsiElement element) {
        this.element = element;
    }

    @Override
    @Nullable
    public String getLocationString() {
        return null;
    }

    @Override
    public String getPresentableText() {
        ASTNode node = element.getNode();
        return node.getText();
    }

    @Override
    @Nullable
    public Icon getIcon(boolean open) {
        return element.getIcon(0);
    }
}
