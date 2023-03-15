package structview;

import com.intellij.icons.AllIcons;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.SorterUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import plugin.HiveFile;
import psi.node.LexerKeyWordNode;
import psi.node.MyRuleSpecNode;

import java.util.Comparator;

public class HiveStructureViewModel extends StructureViewModelBase
        //	extends TextEditorBasedStructureViewModel
        implements StructureViewModel.ElementInfoProvider {
    private static final Sorter PARSER_LEXER_RULE_SORTER =
            new Sorter() {
                @Override
                public Comparator<?> getComparator() {
                    return (o1, o2) -> {
                        String s1 = SorterUtil.getStringPresentation(o1);
                        String s2 = SorterUtil.getStringPresentation(o2);
                        // flip case of char 0 so it puts parser rules first
                        if (Character.isLowerCase(s1.charAt(0))) {
                            s1 = Character.toUpperCase(s1.charAt(0)) + s1.substring(1);
                        } else {
                            s1 = Character.toLowerCase(s1.charAt(0)) + s1.substring(1);
                        }
                        if (Character.isLowerCase(s2.charAt(0))) {
                            s2 = Character.toUpperCase(s2.charAt(0)) + s2.substring(1);
                        } else {
                            s2 = Character.toLowerCase(s2.charAt(0)) + s2.substring(1);
                        }
                        return s1.compareTo(s2);
                    };
                }

                @Override
                public boolean isVisible() {
                    return true;
                }

                @Override
                @NotNull
                public ActionPresentation getPresentation() {
                    // how it's described in sort by dropdown in nav window.
                    String name = "Sort by rule type";
                    return new ActionPresentationData(name, name, AllIcons.ObjectBrowser.SortByType);
                }

                @Override
                @NotNull
                public String getName() {
                    return "PARSER_LEXER_RULE_SORTER";
                }
            };

    HiveFile rootElement;

    public HiveStructureViewModel(HiveFile rootElement) {
        super(rootElement, new HiveStructureViewElement(rootElement));
        this.rootElement = rootElement;
    }

    @Override
    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{PARSER_LEXER_RULE_SORTER, Sorter.ALPHA_SORTER};
    }

    @Override
    protected PsiFile getPsiFile() {
        return rootElement;
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof HiveFile;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof LexerKeyWordNode;
    }

    /**
     * Intellij: The implementation of StructureViewTreeElement.getChildren() needs to be matched by
     * TextEditorBasedStructureViewModel.getSuitableClasses(). The latter method returns an array of
     * PsiElement-derived classes which can be shown as structure view elements, and is used to select
     * the Structure View item matching the cursor position when the structure view is first opened or
     * when the "Autoscroll from source" option is used.
     */
    @Override
    @NotNull
    protected Class<?>[] getSuitableClasses() {
        return new Class[]{HiveFile.class, MyRuleSpecNode.class};
    }
}
