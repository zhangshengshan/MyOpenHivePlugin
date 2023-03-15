import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.HiveLanguage;

public class HiveRefactoringSupport extends RefactoringSupportProvider {

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return context.getLanguage().isKindOf(HiveLanguage.INSTANCE);
    }

    @Override
    public boolean isMemberInplaceRenameAvailable(
            @NotNull PsiElement element, @Nullable PsiElement context) {
        return true;
    }
}
