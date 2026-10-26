package mydata.studio.op;

import com.intellij.openapi.application.ApplicationManager;
import misc.ScalaBridge;
import mydata.studio.service.SessionService;

/**
 * 生成 SELECT 片段。
 *
 * <p>内部通过 {@link misc.ScalaBridge}（Scala 侧实现）转发到
 * {@code openbrowser.GenCodeOp#genSelectCodePiece}。Java 源码不再直接引用
 * Scala object，以免形成 compileJava &lt;-&gt; compileScala 的编译任务环。</p>
 *
 * @author nobody
 */
public class GenOp {

    public static String getSelectPart(String param) {
        ScalaBridge bridge = ApplicationManager.getApplication().getService(ScalaBridge.class);
        return bridge.genSelectCodePiece(param, SessionService.getSessionId());
    }
}