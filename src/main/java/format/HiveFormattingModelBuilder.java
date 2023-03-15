package format;

import antlr.g4.SqlBaseLexer;
import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import plugin.HiveLanguage;
import plugin.basic.HiveTokenTypes;

/**
 * @author nobody@com
 */
public class HiveFormattingModelBuilder implements FormattingModelBuilder {
    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, HiveLanguage.INSTANCE)
                .around(HiveTokenTypes.TOKEN_ELEMENT_TYPES.get(SqlBaseLexer.SELECT))
                .spaces(10);
    }

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new HiveBlock(
                        formattingContext.getNode(),
                        Wrap.createWrap(WrapType.NONE, true),
                        Alignment.createAlignment(true),
                        createSpaceBuilder(codeStyleSettings)),
                codeStyleSettings);
    }
}
