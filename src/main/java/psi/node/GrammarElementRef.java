package psi.node;

import antlr.g4.SqlBaseLexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import completion.HiveCompletionContributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.basic.HiveTokenTypes;
import psi.MyPsiUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A reference to a grammar element (parser rule, lexer rule or lexical mode).
 */
public class GrammarElementRef extends PsiReferenceBase<GrammarElementRefNode> {
    private final String ruleName;
    Logger log = Logger.getInstance(GrammarElementRef.class);

    public GrammarElementRef(GrammarElementRefNode idNode, String ruleName) {
        super(idNode, new TextRange(0, ruleName.length()));
        this.ruleName = ruleName;
    }

    /**
     * Using for completion. Returns list of rules and tokens; the prefix of current element is used
     * as filter by IDEA later.
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        PsiElement father = myElement.getParent();
        while (father != null && father.getParent() != null) {
            father = father.getParent();
        }
        // TODO: 2022/10/8 如果father为null则需要进行特殊处理
        if (father == null) {
            return new Object[0];
        }
        final PsiElement[] psiElements =
                PsiTreeUtil.collectElements(father, element -> element instanceof LeafElement);
        final Stream<String> stringStream =
                Arrays.stream(psiElements)
                        .map(x -> x.getText())
                        .filter(HiveCompletionContributor::isNotKeyword)
                        .distinct();
        return stringStream.toArray();
    }

    // TODO: 2022/10/10 如果selectClause下面有子selectClause查询且能够查到相同的字段名称，则需要resolve得到
    // 对应的PsiElement
    // TODO: 2022/10/10 Union ALL 语句中的字段可能可能有两个Declaration的PsiElement 这种情况也需要处理

    @Override
    public @Nullable PsiElement resolve() {

        final PsiFile root = PsiTreeUtil.getTopmostParentOfType(this.myElement, PsiFile.class);

        final PsiElement[] psiElements =
                PsiTreeUtil.collectElements(
                        root,
                        element -> {
                            if (element instanceof IdentifierNode
                                    && element.getText() == this.myElement.getText()) {
                                return true;
                            }
                            return false;
                        });

        final Stream<PsiElement> psiElementStream =
                Arrays.asList(psiElements).stream().filter(x -> x != this.myElement);

        final List<PsiElement> collect = psiElementStream.collect(Collectors.toList());

        final int i = collect.size() - 1;

        int max_depth = 0;
        PsiElement max_depth_element = null;

        for (int i1 = 0; i1 < collect.size(); i1++) {
            int curDepth = PsiTreeUtil.getDepth(root, collect.get(i1));
      /*
            System.out.println(
                "curDepth = " + curDepth + " max_depth = " + max_depth + " " + collect.get(i1));
      */
            if (curDepth > max_depth) {
                max_depth = PsiTreeUtil.getDepth(root, collect.get(i1));
                max_depth_element = collect.get(i1);
            }
        }
        return max_depth_element;
    }

    // TODO: 2022/9/29 rename处理的存在问题, 不能生效
    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        System.out.println("text:" + myElement + " offset" + myElement.getStartOffset());
        System.out.println(myElement.getReference().resolve().getTextOffset());

        final PsiElement resolve = myElement.getReference().resolve();

        final PsiElement root = PsiTreeUtil.getTopmostParentOfType(myElement, PsiElement.class);
        final PsiElement[] psiElements =
                PsiTreeUtil.collectElements(
                        root,
                        element ->
                                element instanceof IdentifierNode && element.getText().equals(myElement.getText()));

        Project project = getElement().getProject();
        final PsiElement leafFromText =
                MyPsiUtils.createLeafFromText(
                        project,
                        myElement.getContext(),
                        newElementName,
                        HiveTokenTypes.TOKEN_ELEMENT_TYPES.get(SqlBaseLexer.IDENTIFIER));
        for (PsiElement psiElement : psiElements) {
            psiElement.replace(leafFromText);
        }
        // TODO: 2022/10/14 原来此处直接进行myElement.replace
    /*
        myElement.replace(leafFromText);
        resolve.replace(leafFromText);
    */

        return myElement;
    }

  /*
    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {

      System.out.println(this.getElement().getTextOffset());

      final PsiFile root = PsiTreeUtil.getTopmostParentOfType(this.myElement, PsiFile.class);
      final PsiElement[] psiElements = PsiTreeUtil.collectElements(
              root,
              element -> element instanceof IdentifierNode && element.getText() == myElement.getText());

      final long count = Arrays.stream(psiElements).count();

      final ResolveResult[] resolveResults = new ResolveResult[(int) count];

      for (int i = 0 ; i < psiElements.length; i++) {
        resolveResults[i] = new PsiElementResolveResult(psiElements[i]);
        System.out.println(psiElements[i].getTextOffset() + " " + psiElements[i].getTextLength());
      }

      System.out.println("length is " + psiElements.length);
      return resolveResults;
    }
  */
}
