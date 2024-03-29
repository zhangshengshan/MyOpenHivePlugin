package util;

import antlr.g4.SqlBaseParser;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.HiveFile;
import plugin.HiveLanguage;
import plugin.basic.HiveTokenTypes;
import psi.node.BaseRuleSpecNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangshengshan
 */
@SuppressWarnings("SimplifiableIfStatement")
public class MyPsiUtils {
    @Nullable
    @Deprecated
    public static PsiElement findFirstChildOfType(final PsiElement parent, IElementType type) {
        return findFirstChildOfType(parent, TokenSet.create(type));
    }

    /**
     * traverses the psi tree depth-first, returning the first it finds with the given types
     *
     * @param parent the element whose children will be searched
     * @param types  the types to search for
     * @return the first child, or null;
     */
    @Nullable
    public static PsiElement findFirstChildOfType(final PsiElement parent, final TokenSet types) {
        Iterator<PsiElement> iterator = findChildrenOfType(parent, types).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static Iterable<PsiElement> findChildrenOfType(
            final PsiElement parent, IElementType type) {
        return findChildrenOfType(parent, TokenSet.create(type));
    }

    /**
     * Like PsiTreeUtil.findChildrenOfType, except no collection is created and it does not use
     * recursion.
     *
     * @param parent the element whose children will be searched
     * @param types  the types to search for
     * @return an iterable that will traverse the psi tree depth-first, including only the elements
     * whose type is contained in the provided tocken sets.
     */
    public static Iterable<PsiElement> findChildrenOfType(
            final PsiElement parent, final TokenSet types) {
        PsiElement[] psiElements =
                PsiTreeUtil.collectElements(
                        parent,
                        input -> {
                            ASTNode node = input.getNode();
                            if (node == null) {
                                return false;
                            }
                            return types.contains(node.getElementType());
                        });
        return Arrays.asList(psiElements);
    }

    /**
     * Finds the first {@link BaseRuleSpecNode} or {@link } matching the {@code ruleName}
     * defined in the given {@code grammar}.
     *
     * <p>Rule specs can be either children of the {@link }, or under one of the {@code
     * mode}s defined in the grammar. This means we have to walk the whole grammar to find matching
     * candidates.
     */
    @Deprecated
    public static PsiElement createLeafFromText(
            Project project, PsiElement context, String text, IElementType type) {
        PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
        PsiElement el = factory.createElementFromText(text, HiveLanguage.INSTANCE, type, context);
        return PsiTreeUtil.getDeepestFirst(el);
    }

    @Deprecated
    public static void replacePsiFileFromText(
            final Project project, final PsiFile psiFile, String text) {
        final PsiFile newPsiFile = createFile(project, text);
        WriteCommandAction setTextAction =
                new WriteCommandAction(project) {
                    @Override
                    protected void run(@NotNull final Result result) {
                        psiFile.deleteChildRange(psiFile.getFirstChild(), psiFile.getLastChild());
                        psiFile.addRange(newPsiFile.getFirstChild(), newPsiFile.getLastChild());
                    }
                };
        setTextAction.execute();
    }

    public static PsiFile createFile(Project project, String text) {
        // random name but must be .g4
        String fileName = "a.g4";
        PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
        return factory.createFileFromText(fileName, HiveLanguage.INSTANCE, text, false, false);
    }

  /*
      public static PsiElement[] collectAtActions(PsiElement root, final String tokenText) {
          return PsiTreeUtil.collectElements(root, element -> {
              PsiElement p = element.getContext();
              if (p != null) p = p.getContext();
              return p instanceof AtAction &&
                      element instanceof ParserRuleRefNode &&
                      element.getText().equals(tokenText);
          });
      }
  */

    /**
     * Search all internal and leaf nodes looking for token or internal node with specific text. This
     * saves having to create lots of java classes just to identify psi nodes.
     */
    public static PsiElement[] collectNodesWithName(PsiElement root, final String tokenText) {
        return PsiTreeUtil.collectElements(
                root,
                element -> {
                    String tokenTypeName = element.getNode().getElementType().toString();
                    return tokenTypeName.equals(tokenText);
                });
    }

    @Deprecated
    public static PsiElement[] collectNodesWithText(PsiElement root, final String text) {
        return PsiTreeUtil.collectElements(root, element -> element.getText().equals(text));
    }

    public static PsiElement[] collectChildrenOfType(PsiElement root, final IElementType tokenType) {
        List<PsiElement> elems = new ArrayList<>();
        for (PsiElement child : root.getChildren()) {
            if (child.getNode().getElementType() == tokenType) {
                elems.add(child);
            }
        }
        return elems.toArray(new PsiElement[elems.size()]);
    }

    @Deprecated
    public static PsiElement findChildOfType(PsiElement root, final IElementType tokenType) {
        List<PsiElement> elems = new ArrayList<>();
        for (PsiElement child : root.getChildren()) {
            if (child.getNode().getElementType() == tokenType) {
                return child;
            }
        }
        return null;
    }

    public static PsiElement[] collectChildrenWithText(PsiElement root, final String text) {
        List<PsiElement> elems = new ArrayList<>();
        for (PsiElement child : root.getChildren()) {
            if (child.getText().equals(text)) {
                elems.add(child);
            }
        }
        return elems.toArray(new PsiElement[elems.size()]);
    }

    @Deprecated
    public static String findTokenVocabIfAny(HiveFile file) {
        String vocabName = null;
        PsiElement[] options = collectNodesWithName(file, "option");
        for (PsiElement o : options) {
            PsiElement[] tokenVocab = collectChildrenWithText(o, "tokenVocab");
            if (tokenVocab.length > 0) {
                // tokenVocab[0] is id node
                PsiElement optionNode = tokenVocab[0].getParent();
                PsiElement[] ids =
                        collectChildrenOfType(
                                optionNode,
                                HiveTokenTypes.RULE_ELEMENT_TYPES.get(SqlBaseParser.RULE_singleStatement));
                vocabName = ids[0].getText();
            }
        }
        return vocabName;
    }

    @Deprecated
    public static PsiElement findElement(PsiElement startNode, int offset) {
        PsiElement p = startNode;
        if (p == null) {
            return null;
        }

        PsiElement c = p.getFirstChild();
        while (c != null) {
            PsiElement result = findElement(c, offset);
            if (result != null) {
                return result;
            }
            c = c.getNextSibling();
        }
        return null;
    }
}
