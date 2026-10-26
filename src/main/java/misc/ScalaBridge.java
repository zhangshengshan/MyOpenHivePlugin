package misc;

/**
 * Java -> Scala 的调用桥。
 *
 * <p>Gradle 的编译顺序是 javac 先、scalac 后，所以 Java 源码不能直接引用 Scala 的
 * class/object（否则会形成 compileJava 与 compileScala 的任务环）。Java 侧只依赖这个
 * 接口，真正的实现由 Scala 提供（{@code misc.ScalaBridgeImpl}），并在 plugin.xml 中
 * 注册为 applicationService。</p>
 *
 * @author nobody
 */
public interface ScalaBridge {

    /**
     * 生成 SELECT 片段。
     *
     * @see openbrowser.GenCodeOp#genSelectCodePiece(String, String)
     */
    String genSelectCodePiece(String param, String sessionStr);

    /**
     * 生成"榫卯"（表关系）结构图。
     *
     * @see openbrowser.TenonStructureOp#genTenonStructureGraph(String)
     */
    void genTenonStructureGraph(String param);

    /**
     * 把文本写入系统剪贴板，并记录到插件的剪贴板历史队列。
     *
     * <p>等价于 main 分支 {@code HiveFormatAction} 直接调用
     * {@code misc.ClipBoardUtil.copyToClipBoard}：除了写入系统剪贴板，还会把该值
     * 存进 {@code ClipBoardUtil} 内部的 100 条历史队列（供 BatchProcessGroup 使用）。</p>
     *
     * @see misc.ClipBoardUtil#copyToClipBoard(String)
     */
    void copyToClipBoard(String text);
}
