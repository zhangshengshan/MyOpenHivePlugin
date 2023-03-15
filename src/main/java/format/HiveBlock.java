package format;

import antlr.g4.SqlBaseLexer;
import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.basic.HiveTokenTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 */
public class HiveBlock extends AbstractBlock {

    private final SpacingBuilder spaceBuilder;

    public HiveBlock(ASTNode node, Wrap wrap, Alignment alignment, SpacingBuilder spaceBuilder) {
        super(node, wrap, alignment);
        this.spaceBuilder = spaceBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (child.getElementType() != HiveTokenTypes.getTokenElementType(SqlBaseLexer.WS)) {
                Block block =
                        new HiveBlock(
                                child,
                                Wrap.createWrap(WrapType.NONE, true),
                                Alignment.createAlignment(true),
                                spaceBuilder);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spaceBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public Indent getIndent() {
        return Indent.getLabelIndent();
    }

    @Override
    public @Nullable Wrap getWrap() {
        return super.getWrap();
    }

    @Override
    public Alignment getAlignment() {
        return super.getAlignment();
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
