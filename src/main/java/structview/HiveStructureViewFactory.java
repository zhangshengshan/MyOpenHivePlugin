package structview;

import com.intellij.ide.structureView.*;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.HiveFile;

import javax.swing.Icon;
import java.util.Collections;

/**
 * TODO: 2022/9/28 RENAME TO HIVE
 */
public class HiveStructureViewFactory implements PsiStructureViewFactory {
    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            @Override
            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                VirtualFile grammarFile = psiFile.getVirtualFile();
                if (grammarFile == null || !grammarFile.getName().endsWith(".hql")) {
                    return new StructureViewModelBase(psiFile, new DummyViewTreeElement(psiFile));
                }
                return new HiveStructureViewModel((HiveFile) psiFile);
            }
        };
    }

    /**
     * fake a blank Treeview with a warning
     * <p>
     * 2026.2 起 {@code com.intellij.ide.structureView.impl.common.PsiTreeElementBase}
     * 被移到了 platform-structureView-plugin 模块里（不再位于 IDE 的 lib 下），
     * 这里改为直接实现 {@link StructureViewTreeElement}，避免依赖内部实现类。
     */
    public static class DummyViewTreeElement implements StructureViewTreeElement, ItemPresentation {
        private final PsiElement myElement;

        public DummyViewTreeElement(PsiElement psiElement) {
            this.myElement = psiElement;
        }

        @Override
        public Object getValue() {
            return myElement;
        }

        @NotNull
        @Override
        public ItemPresentation getPresentation() {
            return this;
        }

        @NotNull
        @Override
        public TreeElement[] getChildren() {
            return StructureViewTreeElement.EMPTY_ARRAY;
        }

        @Nullable
        @Override
        public String getPresentableText() {
            return "Hive";
        }

        @Nullable
        @Override
        public String getLocationString() {
            return null;
        }

        @Nullable
        @Override
        public Icon getIcon(boolean unused) {
            return null;
        }

        @Override
        public void navigate(boolean requestFocus) {
        }

        @Override
        public boolean canNavigate() {
            return false;
        }

        @Override
        public boolean canNavigateToSource() {
            return false;
        }
    }
}
