package templates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class HiveLiveTemplateContext extends TemplateContextType {
    protected HiveLiveTemplateContext() {
        super("Hive", "Hive");
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getName().endsWith(".hql")
                || templateActionContext.getFile().getName().endsWith(".sql");
    }
}
