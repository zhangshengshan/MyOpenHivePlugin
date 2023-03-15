package plugin;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import hilight.HiveSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class HiveColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS =
            new AttributesDescriptor[]{
                    new AttributesDescriptor("Identifier", HiveSyntaxHighlighter.ID),
                    new AttributesDescriptor("Keyword", HiveSyntaxHighlighter.KEYWORD),
                    new AttributesDescriptor("Literal", HiveSyntaxHighlighter.LITERAL),
                    new AttributesDescriptor("Operator", HiveSyntaxHighlighter.OPERATORS),
                    new AttributesDescriptor("Line comment", HiveSyntaxHighlighter.LINE_COMMENT),
                    new AttributesDescriptor("Block comment", HiveSyntaxHighlighter.BLOCK_COMMENT)
            };

    @Nullable
    @Override
    public Icon getIcon() {
        return HiveIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new HiveSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "--comment \n"
                + "SELECT \n"
                + "    a, \n"
                + "    b, \n"
                + "FROM \n"
                + "    table1 \n"
                + "LEFT OUTER JOIN \n"
                + "    table2 \n"
                + "ON \n"
                + "    table1.a = table2.a \n"
                + "WHERE \n"
                + "    table1.a > 10 \n"
                + "union all \n"
                + "select \n"
                + "    a, \n"
                + "    b, \n"
                + "from \n"
                + "    table3 \n"
                + "where \n"
                + "    table3.a > 10 \n"
                + ";";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Hive additions";
    }
}
