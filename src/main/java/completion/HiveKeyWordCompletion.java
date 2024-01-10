package completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author nobody
 */
public class HiveKeyWordCompletion extends CompletionProvider<CompletionParameters> {

    private final String[] keywords;
    private Icon icon;

    @Deprecated
    public HiveKeyWordCompletion(String[] keywords) {
        this.keywords = keywords;
    }

    public HiveKeyWordCompletion(String[] keywords, Icon icon) {
        this.keywords = keywords;
        this.icon = icon;
    }

    @Override
    protected void addCompletions(
            @NotNull CompletionParameters parameters,
            @NotNull ProcessingContext context,
            @NotNull CompletionResultSet result) {
        for (String keyword : keywords) {
            result.addElement(LookupElementBuilder.create(keyword).bold().withIcon(icon));
        }
    }
}
