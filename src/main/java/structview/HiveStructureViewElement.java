package structview;

import antlr.g4.SqlBaseParser;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import plugin.basic.HiveTokenTypes;
import psi.node.IdentifierNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author nobody
 */
public class HiveStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final PsiElement element;

    public HiveStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof NavigationItem && ((NavigationItem) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof NavigationItem && ((NavigationItem) element).canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return element instanceof PsiNamedElement ? ((PsiNamedElement) element).getName() : "";
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return new HiveItemPresentation(element);
    }

    /**
     * TODO: 2022/9/28 flatten hierarchy here what if nested structure view is needed?
     */
    @NotNull
    @Override
    public TreeElement[] getChildren() {
        List<TreeElement> treeElements = new ArrayList<>();
        final PsiElement[] children = element.getChildren();
        for (PsiElement child : children) {
            final Collection<IdentifierNode> childrenOfType =
                    PsiTreeUtil.findChildrenOfType(child, IdentifierNode.class);
            for (IdentifierNode id : childrenOfType) {
                final IElementType parentType = id.getParent().getNode().getElementType();
                final IElementType grandFatherType = id.getParent().getParent().getNode().getElementType();
                if (grandFatherType
                        == HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_tableAlias)) {
                    treeElements.add(new HiveStructureViewElement(id));
                }
            }
        }
        return treeElements.toArray(new TreeElement[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HiveStructureViewElement that = (HiveStructureViewElement) o;

        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return element != null ? element.hashCode() : 0;
    }
}
