package folding;

import antlr.g4.SqlBaseLexer;
import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.CustomFoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import plugin.HiveFile;
import util.MyPsiUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static plugin.basic.HiveTokenTypes.*;

/**
 * @author zhangshengshan
 */
public class HiveFoldingBuilder extends CustomFoldingBuilder {

    public static final RuleIElementType JOIN = getRuleElementType(SqlBaseParser.RULE_joinCriteria);

    public static final RuleIElementType SELECT_CLAUSE =
            getRuleElementType(SqlBaseParser.RULE_namedExpressionSeq);

    public static final RuleIElementType GROUPBY =
            getRuleElementType(SqlBaseParser.RULE_aggregationClause);

    public static final RuleIElementType CTES =
            getRuleElementType(SqlBaseParser.RULE_ctes);

    public static final RuleIElementType NAMED_QUERY = getRuleElementType(SqlBaseParser.RULE_namedQuery);

    public static final RuleIElementType WHERE_CLAUSE =
            getRuleElementType(SqlBaseParser.RULE_whereClause);
    private static final TokenIElementType DOC_COMMENT_TOKEN =
            getTokenElementType(SqlBaseLexer.COMMENT);
    private static final TokenIElementType BLOCK_COMMENT_TOKEN =
            getTokenElementType(SqlBaseLexer.BRACKETED_COMMENT);
    private static final TokenIElementType LINE_COMMENT_TOKEN =
            getTokenElementType(SqlBaseLexer.SIMPLE_COMMENT);
    private static final TokenIElementType TOKENS = getTokenElementType(SqlBaseLexer.IDENTIFIER);
    private static final TokenSet RULE_BLOCKS =
            TokenSet.create(
                    getRuleElementType(SqlBaseParser.RULE_selectClause),
                    getRuleElementType(SqlBaseParser.RULE_whereClause));

    private static void addTokensFoldingDescriptor(
            List<FoldingDescriptor> descriptors, PsiElement root) {
        Iterable<PsiElement> childrenOfType = MyPsiUtils.findChildrenOfType(root, SELECT_CLAUSE);
        childrenOfType.forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });
        Iterable<PsiElement> whereParts = MyPsiUtils.findChildrenOfType(root, WHERE_CLAUSE);
        whereParts.forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });

        Iterable<PsiElement> joinParts = MyPsiUtils.findChildrenOfType(root, JOIN);
        joinParts.forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });

        final Iterable<PsiElement> groupParts = MyPsiUtils.findChildrenOfType(root, GROUPBY);
        groupParts.forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });

        MyPsiUtils.findChildrenOfType(root, CTES).forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });

        MyPsiUtils.findChildrenOfType(root, NAMED_QUERY).forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });

        MyPsiUtils.findChildrenOfType(root, getRuleElementType(SqlBaseParser.RULE_relationPrimary)).forEach(action -> {
            if (action != null) {
                descriptors.add(new FoldingDescriptor(action.getNode(), action.getTextRange()));
            }
        });
    }

    private static void addCommentDescriptors(List<FoldingDescriptor> descriptors, PsiElement root) {
        /*
        processedComments 永远没有被更新
         */
        Set<PsiElement> processedComments = new HashSet<>();
        for (PsiElement comment : MyPsiUtils.findChildrenOfType(root, COMMENTS)) {
            IElementType type = comment.getNode().getElementType();
            if (processedComments.contains(comment)) {
                continue;
            }
            if (type == DOC_COMMENT_TOKEN || type == BLOCK_COMMENT_TOKEN || type == LINE_COMMENT_TOKEN) {
                descriptors.add(new FoldingDescriptor(comment, comment.getTextRange()));
            }
        }
    }

    private static boolean isComment(PsiElement element) {
        IElementType type = element.getNode().getElementType();
        return COMMENTS.contains(type);
    }

    /**
     * TODO: 2022/10/18 PlaceHolder中的换行符不能添加
     */
    private static String getPlaceholderText(PsiElement element) {

        if (element.getNode().getElementType() == JOIN) {
            return "JOIN...\r\n" + System.lineSeparator();
        } else if (element.getNode().getElementType() == LINE_COMMENT_TOKEN) {
            return "...";
        } else if (element.getNode().getElementType() == SELECT_CLAUSE) {
            return "字段列表...\r\n" + System.lineSeparator();
        } else if (element.getNode().getElementType() == WHERE_CLAUSE) {
            return "WHERE...\r\n" + System.lineSeparator();
        } else if (element.getNode().getElementType() == GROUPBY) {
            return "GROUP BY...\r\n" + System.lineSeparator();
        } else if (element.getNode().getElementType()
                == getRuleElementType(SqlBaseParser.RULE_relationPrimary)) {
            String text = "";

            final PsiElement[] children = element.getChildren();
            for (PsiElement child : children) {
                if (child.getNode().getElementType()
                        == getRuleElementType(SqlBaseParser.RULE_tableAlias)) {
                    if (child.getLastChild() != null) {
                        text = child.getLastChild().getText();
                    }
                    break;
                }
            }
            return "表(块)" + " " + text + System.lineSeparator();
        } else if (element.getNode().getElementType() == getRuleElementType(SqlBaseParser.RULE_namedQuery)) {
            return "block " + element.getFirstChild().getText() + " ";
        } else if (element.getNode().getElementType() == getRuleElementType(SqlBaseParser.RULE_ctes)) {
            return "cites" + element.getFirstChild().getText() + "...";
        }
        return "...";
    }

    @Override
    protected void buildLanguageFoldRegions(
            @NotNull List<FoldingDescriptor> descriptors,
            @NotNull PsiElement root,
            @NotNull Document document,
            boolean quick) {
        if (root instanceof HiveFile) {
            addCommentDescriptors(descriptors, root);
            addTokensFoldingDescriptor(descriptors, root);
        }
    }

    @Override
    protected String getLanguagePlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
        return getPlaceholderText(SourceTreeToPsiMap.treeElementToPsi(node));
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    protected boolean isRegionCollapsedByDefault(@NotNull ASTNode node) {
        final PsiElement element = SourceTreeToPsiMap.treeElementToPsi(node);
        if (element == null) {
            return false;
        }

        if (RULE_BLOCKS.contains(node.getElementType())) {
            return true;
        }

        if (element instanceof HiveFile) {
            return true;
        }

        if (node.getElementType() == DOC_COMMENT_TOKEN) {
            PsiElement parent = element.getParent();

            if (parent instanceof HiveFile) {
                PsiElement firstChild = parent.getFirstChild();
                if (firstChild instanceof PsiWhiteSpace) {
                    firstChild = firstChild.getNextSibling();
                }
                if (element.equals(firstChild)) {
                    return true;
                }
            }
            return true;
        }
        return isComment(element);
    }
}
